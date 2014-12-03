/**
 * Mule Salesforce Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

/**
 * 
 */
package org.mule.modules.salesforce.metadata;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.mule.modules.salesforce.connection.CustomMetadataConnection;
import org.mule.modules.salesforce.metadata.type.MetadataOperationType;
import org.mule.modules.salesforce.metadata.type.MetadataType;

import com.sforce.soap.metadata.DeleteResult;
import com.sforce.soap.metadata.Metadata;
import com.sforce.soap.metadata.ReadResult;
import com.sforce.soap.metadata.SaveResult;
import com.sforce.soap.metadata.UpsertResult;

/**
 * @author cristian.ambrozie
 * 
 */
public class MetadataService {

	public static List<SaveResult> callCreateUpdateService(
			CustomMetadataConnection connection, String type,
			List<Map<String, Object>> request,
			MetadataOperationType metadataOperation) throws Exception {

			List<SaveResult> results;
			MetadataType metadataType = MetadataType.valueOf(type);
			switch (metadataOperation) {
			case CREATE:
				results = Arrays.asList(connection
						.createMetadata(getMetadataObjects(metadataType,
								request)));
				break;
			case UPDATE:
				results = Arrays.asList(connection
						.updateMetadata(getMetadataObjects(metadataType,
								request)));
				break;
			default:
				results = new ArrayList<SaveResult>();
			}
			return results;
	}

	public static List<UpsertResult> callUpsertService(
			CustomMetadataConnection connection, String type,
			List<Map<String, Object>> request) throws Exception {

			MetadataType metadataType = MetadataType.valueOf(type);
			return Arrays.asList(connection.upsertMetadata(getMetadataObjects(
					metadataType, request)));
	}

	public static List<DeleteResult> callDeleteService(
			CustomMetadataConnection connection, String type, List<String> fullNames)
			throws Exception {

			MetadataType metadataType = MetadataType.valueOf(type);
			return Arrays.asList(connection.deleteMetadata(
					metadataType.getDisplayName(),
					fullNames.toArray(new String[fullNames.size()])));
	}

	public static ReadResult callReadService(CustomMetadataConnection connection,
			String type, List<String> fullNames) throws Exception {

			MetadataType metadataType = MetadataType.valueOf(type);
			return connection.readMetadata(metadataType.getDisplayName(),
					fullNames.toArray(new String[fullNames.size()]));
	}

	public static SaveResult callRenameService(CustomMetadataConnection connection,
			String type, String oldFullName, String newFullName)
			throws Exception {

			MetadataType metadataType = MetadataType.valueOf(type);
			return connection.renameMetadata(metadataType.getDisplayName(),
					oldFullName, newFullName);
	}

    public static Metadata[] getMetadataObjects(MetadataType metadataType, List<Map<String, Object>> objects) throws Exception {
    	
    	BeanUtilsBean beanUtilsBean = new BeanUtilsBean(new ConvertUtilsBean() {
			@Override
			public Object convert(String value, Class clazz) {
				if (clazz.isEnum()) {
					return Enum.valueOf(clazz, value);
				} 
				else {
					return super.convert(value, clazz);
				}
			}
		});
    	
        Metadata[] mobjects = new Metadata[objects.size()];
        int s = 0;
        for (Map<String, Object> map : objects) {
        	mobjects[s] = toMetadataObject(beanUtilsBean, metadataType, map);
            s++;
        }
        return mobjects;
    }
    
    private static <T> Metadata toMetadataObject(BeanUtilsBean beanUtilsBean, MetadataType metadataType, Map<String, Object> map) throws Exception {
    	Metadata metadataObject = metadataType.getMetadataObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
           String key = entry.getKey();
           
           // if the object is a map, transform to object
           if (entry.getValue() instanceof Map) {
        	   PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        	   Class childClass = propertyUtilsBean.getPropertyType(metadataObject, key);
        	   MetadataType childType = MetadataType.getByClass(childClass);
        	   if (childType != null){
        	   		beanUtilsBean.setProperty(metadataObject, key, toMetadataObject(beanUtilsBean, childType, toMObjectMap((Map) entry.getValue())));
        	   }
           }
           
           //if the object is a list, transform to array
           else if (entry.getValue() instanceof List) {
        	   List<Object> objectsList = (List) entry.getValue();
        	   Metadata[] mobjects = new Metadata[objectsList.size()];
        	   int s=0;
        	   Class metadataClass = null;
        	   for (Object objectEntry : objectsList ) {
        		   if (objectEntry instanceof Map) {
        			   PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
                	   Class childClass = propertyUtilsBean.getPropertyType(metadataObject, key);
                	   if (childClass != null && childClass.isArray()) {
                		   childClass = childClass.getComponentType();
                	   }
                	   if (metadataClass == null) {
                		   metadataClass = childClass;
                	   }
                	   MetadataType childType = MetadataType.getByClass(childClass);
                	   mobjects[s] = toMetadataObject(beanUtilsBean, childType, toMObjectMap((Map) objectEntry));
                	   s++;
        		   }
        	   }
        	   if (metadataClass != null) {
        		   Object[] metadataChildObject = (T[]) Array.newInstance(metadataClass, mobjects.length);
        		   for (int i=0;i<mobjects.length;i++) {
        			   metadataChildObject[i] = metadataClass.cast(mobjects[i]);
        		   }
        		   beanUtilsBean.setProperty(metadataObject, key, metadataChildObject);
        	   }
           }
            else {
            	beanUtilsBean.setProperty(metadataObject, key, entry.getValue());
           }
        }
        return metadataObject;
    }
    
    public static Map<String, Object> toMObjectMap(Map<Object, Object> map) {
        Map<String, Object> mObjectMap = new HashMap<String, Object>();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
        	mObjectMap.put(entry.getKey().toString(), entry.getValue());
        }
        return mObjectMap;
    }
}
