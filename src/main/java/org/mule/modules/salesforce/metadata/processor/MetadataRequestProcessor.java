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
package org.mule.modules.salesforce.metadata.processor;

import java.util.List;
import java.util.Map;

import org.mule.modules.salesforce.metadata.type.MetadataType;

/**
 * @author cristian.ambrozie
 * 
 */
public class MetadataRequestProcessor {

	public static List<Map<String, Object>> processRequest(
			List<Map<String, Object>> request, String type) {
		
		MetadataType metadataType = MetadataType.valueOf(type);
		
		switch(metadataType) {
			case ExternalDataSource:
				request =  ExternalDataSourceRequestProcessor.processRequest(request);
				break;
			case RemoteSiteSetting:
				request = RemoteSiteSettingRequestProcessor.processRequest(request);
				break;
			default:
				return request;
		}
		return request;
	}
}
