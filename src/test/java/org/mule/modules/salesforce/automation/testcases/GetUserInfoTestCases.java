/**
 * Mule Salesforce Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.salesforce.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.partner.GetUserInfoResult;



public class GetUserInfoTestCases extends SalesforceTestParent {
     
    @Category({SmokeTests.class, RegressionTests.class})
	@Test
	public void testGetUserInfo() {
    	
		try {
			
			loadTestRunMessage("getUserInfoResult");
	        
	        GetUserInfoResult userInfoResult = runFlowAndGetPayload("get-user-info");

	        assertEquals(getTestRunMessageValue("userName").toString(), userInfoResult.getUserName()); 
	        
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
     
	}

}