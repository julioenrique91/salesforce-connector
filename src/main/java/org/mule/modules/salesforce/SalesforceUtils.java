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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mule.common.bulk.BulkItem;
import org.mule.common.bulk.BulkItem.BulkItemBuilder;
import org.mule.common.bulk.BulkOperationResult;
import org.mule.common.bulk.BulkOperationResult.BulkOperationResultBuilder;
import org.mule.modules.salesforce.exception.SalesforceBulkException;

import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.UpsertResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.bind.XmlObject;

/**
 * @author Mulesoft, Inc
 */
public class SalesforceUtils {

    public static Map<String, Object> toMap(XmlObject xmlObject) {
        Map<String, Object> map = new HashMap<String, Object>();
        Object value = xmlObject.getValue();

        if (value == null && xmlObject.hasChildren()) {
            XmlObject child;
            Iterator childrenIterator = xmlObject.getChildren();

            while (childrenIterator.hasNext()) {
                child = (XmlObject) childrenIterator.next();
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
                ((List) value).add(newValue);
            }
            else {
                map.put(key, new ArrayList(Arrays.asList(value, newValue)));
            }
        }
        else {
            map.put(key, newValue);
        }
    }
    
    public static BulkOperationResult<SObject> toOperationResult(SObject[] list, SaveResult[] results) {
    	BulkOperationResultBuilder<SObject> builder = BulkOperationResult.builder();
    	assertResultLength(list, results);
    	
    	for (int i = 0; i < list.length; i++) {
    		SaveResult sr = results[i];

    		BulkItemBuilder<SObject> itemBuilder = BulkItem.<SObject>builder();
    		itemBuilder.setPayload(list[i]);
    		
    		if (!sr.isSuccess()) {
    			itemBuilder.setException(new SalesforceBulkException(sr.getErrors()));
    		}
    		
    		builder.addItem(itemBuilder);
    	}
    	
    	return builder.build();
    }
    
    public static BulkOperationResult<SObject> toOperationResult(SObject[] list, UpsertResult[] results) {
    	BulkOperationResultBuilder<SObject> builder = BulkOperationResult.builder();
    	assertResultLength(list, results);
    	
    	for (int i = 0; i < list.length; i++) {
    		UpsertResult ur = results[i];
    		
    		BulkItemBuilder<SObject> itemBuilder = BulkItem.<SObject>builder();
    		itemBuilder.setPayload(list[i]);
    		
    		if (ur.isSuccess()) {
    			itemBuilder.setMessage(ur.isCreated() ? "Created" : "Updated");
    		} else {
    			itemBuilder.setException(new SalesforceBulkException(ur.getErrors()));
    		}
    		
    		builder.addItem(itemBuilder);
    	}
    	
    	return builder.build();
    }
    
    private static void assertResultLength(SObject[] list, Object[] results) {
    	if (list.length != results.length) {
    		throw new IllegalStateException(String.format("Protocol exception: Objects and results lists should have the same lenghts. %d and %d found instead", list.length, results.length));
    	}
    }

}
