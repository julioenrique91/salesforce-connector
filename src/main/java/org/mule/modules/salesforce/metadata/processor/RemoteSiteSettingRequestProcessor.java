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

/**
 * @author cristian.ambrozie
 * 
 */
public class RemoteSiteSettingRequestProcessor {

	public static List<Map<String, Object>> processRequest(
			List<Map<String, Object>> request) {

		for (Map<String, Object> requestObj : request){
			if (requestObj.containsKey("isActive")) {
				requestObj = setRemoteSiteSettingsisActive(requestObj);
			}
	
			if (requestObj.containsKey("disableProtocolSecurity")) {
				requestObj = setRemoteSiteSettingsDisableProtocolSecurity(requestObj);
			}
		}

		return request;
	}

	private static Map<String, Object> setRemoteSiteSettingsisActive(Map<String, Object> requestObject) {

		if (requestObject.get("isActive").toString().equalsIgnoreCase("true")) {
			requestObject.put("isActive", true);
		}
		else {
			requestObject.put("isActive", false);
		}
		
		return requestObject;
	}

	private static Map<String, Object> setRemoteSiteSettingsDisableProtocolSecurity(Map<String, Object> requestObject) {
		if (requestObject.get("disableProtocolSecurity").toString().equalsIgnoreCase("true")) {
			requestObject.put("disableProtocolSecurity", true);
		} 
		else {
			requestObject.put("disableProtocolSecurity", false);
		}
		
		return requestObject;
	}
}
