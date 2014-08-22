/**
 * Mule Salesforce Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.salesforce;

import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.UpsertResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.bind.XmlObject;
import org.joda.time.DateTime;
import org.mule.api.transformer.DataType;
import org.mule.common.bulk.BulkItem;
import org.mule.common.bulk.BulkItem.BulkItemBuilder;
import org.mule.common.bulk.BulkOperationResult;
import org.mule.common.bulk.BulkOperationResult.BulkOperationResultBuilder;
import org.mule.modules.salesforce.bulk.EnrichedSaveResult;
import org.mule.modules.salesforce.bulk.EnrichedUpsertResult;
import org.mule.modules.salesforce.exception.SalesforceBulkException;
import org.mule.transformer.types.DataTypeFactory;

import java.util.*;

/**
 * @author Mulesoft, Inc
 */
public class SalesforceUtils {

    @SuppressWarnings("rawtypes")
    public static final DataType<BulkOperationResult> BULK_OPERATION_RESULT_DATA_TYPE = DataTypeFactory.create(BulkOperationResult.class);

    /**
     * Converts a Salesforce XML Object to Map
     *
     * @param xmlObject Salesforce XML Object
     * @return
     */
    public static Map<String, Object> toMap(XmlObject xmlObject) {
        Map<String, Object> map = new HashMap<String, Object>();
        Object value = xmlObject.getValue();

        if (value == null && xmlObject.hasChildren()) {
            XmlObject child;
            Iterator<XmlObject> childrenIterator = xmlObject.getChildren();

            while (childrenIterator.hasNext()) {
                child = childrenIterator.next();
                if (child.getValue() != null) {
                    map.put(child.getName().getLocalPart(), child.getValue());
                } else if (child.getChildren().hasNext()) {
                    putToMultiMap(map, child.getName().getLocalPart(), toMap(child));
                } else {
                    map.put(child.getName().getLocalPart(), null);
                }
            }
        }

        return map;
    }

    /**
     * Converts a Salesforce object map to Bulk API SObject
     *
     * @param map                  Salesforce object fields
     * @param batchSobjectMaxDepth Async SObject recursive MAX_DEPTH check
     * @return Async SObject
     */
    public static com.sforce.async.SObject toAsyncSObject(Map<String, Object> map, Integer batchSobjectMaxDepth) {
        com.sforce.async.SObject sObject = batchSobjectMaxDepth != null && batchSobjectMaxDepth != 0 ?
                new com.sforce.async.SObject(batchSobjectMaxDepth) : new com.sforce.async.SObject();
        for (String key : map.keySet()) {
            Object object = map.get(key);

            if (object != null) {
                if (object instanceof Map) {
                    sObject.setFieldReference(key, toAsyncSObject(toSObjectMap((Map) object), batchSobjectMaxDepth));
                } else if (isDateField(object)) {
                    sObject.setField(key, convertDateToString(object));
                } else {
                    sObject.setField(key, object.toString());
                }
            } else {
                sObject.setField(key, null);
            }
        }
        return sObject;
    }

    /**
     * Converts a Salesforce object map to SObject using the given type
     *
     * @param type Salesforce Object Type
     * @param map  object fields
     * @return SObject
     */
    public static SObject toSObject(String type, Map<String, Object> map) {
        SObject sObject = new SObject();
        sObject.setType(type);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            if (key.equals("fieldsToNull")) {
                sObject.setFieldsToNull((String[]) entry.getValue());
            } else if (entry.getValue() instanceof Map) {
                sObject.setField(key, toSObject(key, toSObjectMap((Map) entry.getValue())));
            } else {
                sObject.setField(key, entry.getValue());
            }
        }
        return sObject;
    }

    /**
     * Enforce map keys are converted to String to comply with generic signature in toSObject
     */
    public static Map<String, Object> toSObjectMap(Map map) {
        Map<String, Object> sObjectMap = new HashMap<String, Object>();
        for (Object key : map.keySet()) {
            sObjectMap.put(key.toString(), map.get(key));
        }
        return sObjectMap;
    }

    /**
     * Creates a Salesforce Async Sobject (Bulk API) array from a List of Maps
     *
     * @param objects              list of maps with Salesforce objects fields
     * @param batchSobjectMaxDepth Async SObject recursive MAX_DEPTH check
     * @return
     */
    public static com.sforce.async.SObject[] toAsyncSObjectList(List<Map<String, Object>> objects, Integer batchSobjectMaxDepth) {
        com.sforce.async.SObject[] sobjects = new com.sforce.async.SObject[objects.size()];
        int s = 0;
        for (Map<String, Object> map : objects) {
            sobjects[s] = toAsyncSObject(map, batchSobjectMaxDepth);
            s++;
        }
        return sobjects;
    }

    /**
     * Creates a Salesforce SObject array from a List of Maps
     *
     * @param type    Salesforce Object type
     * @param objects list of maps with Salesforce objects fields
     * @return
     */
    public static SObject[] toSObjectList(String type, List<Map<String, Object>> objects) {
        SObject[] sobjects = new SObject[objects.size()];
        int s = 0;
        for (Map<String, Object> map : objects) {
            sobjects[s] = toSObject(type, map);
            s++;
        }
        return sobjects;
    }

    /**
     * Check whether the key exists, if so creates an array with the key and add the corresponding object.
     * Apache Commons MultiMap is not suitable because it creates an array for every value, no matter if it is a single one.
     *
     * @param map      destination map
     * @param key      key to add/populate
     * @param newValue value to add
     */
    @SuppressWarnings("unchecked")
    private static void putToMultiMap(Map<String, Object> map, String key, Object newValue) {
        if (map.containsKey(key)) {
            Object value = map.get(key);

            if (value instanceof List) {
                ((List<Object>) value).add(newValue);
            } else {
                map.put(key, new ArrayList<Object>(Arrays.asList(value, newValue)));
            }
        } else {
            map.put(key, newValue);
        }
    }

    protected static boolean isDateField(Object object) {
        return object instanceof Date || object instanceof GregorianCalendar
                || object instanceof Calendar;
    }

    protected static String convertDateToString(Object object) {
        return new DateTime(object).toString();
    }

    public static BulkOperationResult<SObject> saveResultToBulkOperationResult(Collection<SaveResult> results) {
        BulkOperationResultBuilder<SObject> builder = BulkOperationResult.builder();

        for (SaveResult sr : results) {
            BulkItemBuilder<SObject> itemBuilder = BulkItem.<SObject>builder();
            if (sr instanceof EnrichedSaveResult) {
                itemBuilder.setPayload(((EnrichedSaveResult) sr).getPayload());
            }

            if (!sr.isSuccess()) {
                itemBuilder.setException(new SalesforceBulkException(sr.getErrors()));
            }

            builder.addItem(itemBuilder);
        }

        return builder.build();
    }

    public static BulkOperationResult<SObject> upsertResultToBulkOperationResult(Collection<UpsertResult> results) {
        BulkOperationResultBuilder<SObject> builder = BulkOperationResult.builder();

        for (UpsertResult ur : results) {
            BulkItemBuilder<SObject> itemBuilder = BulkItem.<SObject>builder();

            if (ur instanceof EnrichedUpsertResult) {
                itemBuilder.setPayload(((EnrichedUpsertResult) ur).getPayload());
            }

            if (ur.isSuccess()) {
                itemBuilder.setMessage(ur.isCreated() ? "Created" : "Updated");
            } else {
                itemBuilder.setException(new SalesforceBulkException(ur.getErrors()));
            }

            builder.addItem(itemBuilder);
        }

        return builder.build();
    }

    public static EnrichedSaveResult enrich(SaveResult saveResut) {
        return new EnrichedSaveResult(saveResut);
    }

    public static EnrichedUpsertResult enrich(UpsertResult upsertResult) {
        return new EnrichedUpsertResult(upsertResult);
    }


    public static EnrichedSaveResult enrichWithPayload(SaveResult saveResult, SObject payload) {
        EnrichedSaveResult enriched = enrich(saveResult);
        enriched.setPayload(payload);

        return enriched;
    }

    public static EnrichedUpsertResult enrichWithPayload(UpsertResult upsertResult, SObject payload) {
        EnrichedUpsertResult enriched = enrich(upsertResult);
        enriched.setPayload(payload);

        return enriched;
    }

    public static List<SaveResult> enrichWithPayload(SObject[] objects, SaveResult[] results) {
        assertResultLength(objects, results);
        List<SaveResult> enriched = new ArrayList<SaveResult>(results.length);

        for (int i = 0; i < results.length; i++) {
            enriched.add(enrichWithPayload(results[i], objects[i]));
        }

        return enriched;
    }

    public static List<UpsertResult> enrichWithPayload(SObject[] objects, UpsertResult[] results) {
        assertResultLength(objects, results);
        List<UpsertResult> enriched = new ArrayList<UpsertResult>(results.length);

        for (int i = 0; i < results.length; i++) {
            enriched.add(enrichWithPayload(results[i], objects[i]));
        }

        return enriched;
    }

    private static void assertResultLength(SObject[] list, Object[] results) {
        if (list.length != results.length) {
            throw new IllegalStateException(String.format("Protocol exception: Objects and results lists should have the same lenghts. %d and %d found instead", list.length, results.length));
        }
    }

}
