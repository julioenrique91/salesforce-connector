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

import com.sforce.soap.metadata.AuthenticationProtocol;
import com.sforce.soap.metadata.ExternalDataSourceType;
import com.sforce.soap.metadata.ExternalPrincipalType;

/**
 * @author cristian.ambrozie
 * 
 */
public class ExternalDataSourceRequestProcessor {

	public static List<Map<String, Object>> processRequest(
			List<Map<String, Object>> request) {

		for (Map<String, Object> requestObj : request){
			if (requestObj.containsKey("type")) {
				requestObj = setExternalDataSourceType(requestObj);
			}
	
			if (requestObj.containsKey("principalType")) {
				requestObj = setExternalDataSourcePrincipalType(requestObj);
			}
	
			if (requestObj.containsKey("protocol")) {
				requestObj = setExternalDataSourceProtocol(requestObj);
			}
		}

		return request;
	}

	private static Map<String, Object> setExternalDataSourceType(Map<String, Object> requestObject) {

		if (requestObject.get("type").toString().equalsIgnoreCase("OData")) {
			requestObject.put("type", ExternalDataSourceType.OData);
		}
		
		return requestObject;
	}

	private static Map<String, Object> setExternalDataSourcePrincipalType(Map<String, Object> requestObject) {
		if (requestObject.get("principalType").toString().equalsIgnoreCase("Anonymous")) {
			requestObject.put("principalType", ExternalPrincipalType.Anonymous);
		} 
		else if (requestObject.get("principalType").toString().equalsIgnoreCase("PerUser")) {
			requestObject.put("principalType", ExternalPrincipalType.PerUser);
		} 
		else if (requestObject.get("principalType").toString().equalsIgnoreCase("NamedUser")) {
			requestObject.put("principalType", ExternalPrincipalType.NamedUser);
		}
		
		return requestObject;
	}

	private static Map<String, Object> setExternalDataSourceProtocol(Map<String, Object> requestObject) {
		if (requestObject.get("protocol").toString().equalsIgnoreCase("NoAuthentication")) {
			requestObject.put("protocol",
					AuthenticationProtocol.NoAuthentication);
		} 
		else if (requestObject.get("protocol").toString().equalsIgnoreCase("Oauth")) {
			requestObject.put("protocol",
					AuthenticationProtocol.Oauth);
		}
		else if (requestObject.get("protocol").toString().equalsIgnoreCase("Password")) {
			requestObject.put("protocol",
					AuthenticationProtocol.Password);
		}
		
		return requestObject;
	}
}
