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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jdto.DTOBinderFactory;
import org.mule.modules.salesforce.metadata.type.MetadataOperationType;
import org.mule.modules.salesforce.metadata.type.MetadataType;

import com.sforce.soap.metadata.DeleteResult;
import com.sforce.soap.metadata.Metadata;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.metadata.ReadResult;
import com.sforce.soap.metadata.SaveResult;
import com.sforce.soap.metadata.UpsertResult;

/**
 * @author cristian.ambrozie
 * 
 */
public class MetadataService {

	public static List<SaveResult> callCreateUpdateService(
			MetadataConnection connection, String type,
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
			MetadataConnection connection, String type,
			List<Map<String, Object>> request) throws Exception {

			MetadataType metadataType = MetadataType.valueOf(type);
			return Arrays.asList(connection.upsertMetadata(getMetadataObjects(
					metadataType, request)));
	}

	public static List<DeleteResult> callDeleteService(
			MetadataConnection connection, String type, List<String> fullNames)
			throws Exception {

			MetadataType metadataType = MetadataType.valueOf(type);
			return Arrays.asList(connection.deleteMetadata(
					metadataType.getDisplayName(),
					fullNames.toArray(new String[fullNames.size()])));
	}

	public static ReadResult callReadService(MetadataConnection connection,
			String type, List<String> fullNames) throws Exception {

			MetadataType metadataType = MetadataType.valueOf(type);
			return connection.readMetadata(metadataType.getDisplayName(),
					fullNames.toArray(new String[fullNames.size()]));
	}

	public static SaveResult callRenameService(MetadataConnection connection,
			String type, String oldFullName, String newFullName)
			throws Exception {

			MetadataType metadataType = MetadataType.valueOf(type);
			return connection.renameMetadata(metadataType.getDisplayName(),
					oldFullName, newFullName);
	}

	private static Metadata[] getMetadataObjects(MetadataType metadataType,
			List<Map<String, Object>> entities)
			throws InvocationTargetException, IllegalAccessException,
			ClassNotFoundException, InstantiationException {
		Metadata[] metadata = new Metadata[entities.size()];
		for (int i = 0; i < entities.size(); i++) {
			Metadata metadataObject = (Metadata) getMetadataObject(
					metadataType, entities.get(i));
			metadata[i] = metadataObject;

		}
		return metadata;
	}

	private static Object getMetadataObject(MetadataType metadataType,
			Map<String, Object> entity) throws InvocationTargetException,
			IllegalAccessException, ClassNotFoundException,
			InstantiationException {
		return DTOBinderFactory.getBinder().bindFromBusinessObject(
				metadataType.getMetadataEntityClass(), entity);
	}
}
