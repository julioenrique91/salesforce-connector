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

import com.sforce.soap.metadata.AuthenticationProtocol;
import com.sforce.soap.metadata.ExternalDataSourceType;
import com.sforce.soap.metadata.ExternalPrincipalType;

/**
 * @author cristian.ambrozie
 * 
 */
public class ExternalDataSourceRequestProcessor {

	private static Map<String, Object> insertRequest;

	public static Map<String, Object> processInsertRequest(
			Map<String, Object> request) {
		insertRequest = request;

		if (insertRequest.containsKey("type")) {
			setExternalDataSourceType();
		}

		if (insertRequest.containsKey("principalType")) {
			setExternalDataSourcePrincipalType();
		}

		if (insertRequest.containsKey("protocol")) {
			setExternalDataSourceProtocol();
		}

		return insertRequest;
	}

	private static void setExternalDataSourceType() {

		if (insertRequest.get("type").toString().equalsIgnoreCase("OData")) {
			insertRequest.put("type", ExternalDataSourceType.OData);
		}
	}

	private static void setExternalDataSourcePrincipalType() {
		if (insertRequest.get("principalType").toString().equalsIgnoreCase("Anonymous")) {
			insertRequest.put("principalType", ExternalPrincipalType.Anonymous);
		} 
		else if (insertRequest.get("principalType").toString().equalsIgnoreCase("PerUser")) {
			insertRequest.put("principalType", ExternalPrincipalType.PerUser);
		} 
		else if (insertRequest.get("principalType").toString().equalsIgnoreCase("NamedUser")) {
			insertRequest.put("principalType", ExternalPrincipalType.NamedUser);
		}
	}

	private static void setExternalDataSourceProtocol() {
		if (insertRequest.get("protocol").toString().equalsIgnoreCase("NoAuthentication")) {
			insertRequest.put("protocol",
					AuthenticationProtocol.NoAuthentication);
		} 
		else if (insertRequest.get("protocol").toString().equalsIgnoreCase("Oauth")) {
			insertRequest.put("protocol",
					AuthenticationProtocol.Oauth);
		}
		else if (insertRequest.get("protocol").toString().equalsIgnoreCase("Password")) {
			insertRequest.put("protocol",
					AuthenticationProtocol.Password);
		}
	}
}
