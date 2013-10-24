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

import com.sforce.ws.bind.XmlObject;

import java.util.*;

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
}
