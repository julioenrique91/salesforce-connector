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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mule.api.transformer.DataType;
import org.mule.common.bulk.BulkItem;
import org.mule.common.bulk.BulkItem.BulkItemBuilder;
import org.mule.common.bulk.BulkOperationResult;
import org.mule.common.bulk.BulkOperationResult.BulkOperationResultBuilder;
import org.mule.modules.salesforce.bulk.EnrichedSaveResult;
import org.mule.modules.salesforce.bulk.EnrichedUpsertResult;
import org.mule.modules.salesforce.exception.SalesforceBulkException;
import org.mule.transformer.types.DataTypeFactory;

import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.UpsertResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.bind.XmlObject;

/**
 * @author Mulesoft, Inc
 */
public class SalesforceUtils {

	@SuppressWarnings("rawtypes")
	public static final DataType<BulkOperationResult> BULK_OPERATION_RESULT_DATA_TYPE = DataTypeFactory.create(BulkOperationResult.class);
	
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
                } else if( child.getChildren().hasNext() ) {
                    putToMultiMap(map, child.getName().getLocalPart(), toMap(child));
                } else {
                    map.put(child.getName().getLocalPart(), null);
                }
            }
        }

        return map;
    }

    /**
     * Check whether the key exists, if so creates an array with the key and add the corresponding object.
     * Apache Commons MultiMap is not suitable because it creates an array for every value, no matter if it is a single one.
     *
     * @param map destination map
     * @param key key to add/populate
     * @param newValue value to add
     */
    @SuppressWarnings("unchecked")
    private static void putToMultiMap(Map<String, Object> map, String key, Object newValue) {
        if(map.containsKey(key)) {
            Object value = map.get(key);

            if(value instanceof List) {
                ((List<Object>) value).add(newValue);
            }
            else {
                map.put(key, new ArrayList<Object>(Arrays.asList(value, newValue)));
            }
        }
        else {
            map.put(key, newValue);
        }
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
