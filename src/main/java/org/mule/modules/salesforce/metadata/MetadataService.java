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

import java.util.Map;

import org.jdto.DTOBinderFactory;
import org.mule.modules.salesforce.metadata.type.MetadataOperationType;
import org.mule.modules.salesforce.metadata.type.MetadataType;

import com.sforce.soap.metadata.Metadata;
import com.sforce.soap.metadata.MetadataConnection;


/**
 * @author cristian.ambrozie
 * 
 */
public class MetadataService {

	public static Object callService(MetadataConnection connection, MetadataType metadataType,
			Map<String, Object> request, MetadataOperationType metadataOperation) throws Exception {

		switch (metadataOperation) {
		case CREATE:
			return connection.createMetadata(new Metadata[] {(Metadata) getMetadataObject(metadataType, request)});
		default:
			return null;
		}
	}
	
	private static Object getMetadataObject(MetadataType metadataType, Map<String, Object> entity) {
		return DTOBinderFactory.getBinder().bindFromBusinessObject(metadataType.getMetadataEntityClass(), entity);
	}

}
