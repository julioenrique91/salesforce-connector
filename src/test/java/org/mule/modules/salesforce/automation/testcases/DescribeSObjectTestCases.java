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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.partner.DescribeSObjectResult;


public class DescribeSObjectTestCases extends SalesforceTestParent {
	
    @Category({RegressionTests.class})
	@Test
	public void testDescribeSObjectForAccount() {
    	
		try {
			
			loadTestRunMessage("describeSObjectAccountTestData");
			
			DescribeSObjectResult describeSObjectResult = runFlowAndGetPayload("describe-sobject");
			String sObjectName = describeSObjectResult.getName();

	        assertTrue(sObjectName.equals(getTestRunMessageValue("type")));

		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
     
	}
    
    @Category({RegressionTests.class})
   	@Test
   	public void testDescribeSObjectForUser() {
       	
   		try {
   			
   			loadTestRunMessage("describeSObjectUserTestData");
   			
   			DescribeSObjectResult describeSObjectResult = runFlowAndGetPayload("describe-sobject");
   			String sObjectName = describeSObjectResult.getName();

   	        assertTrue(sObjectName.equals(getTestRunMessageValue("type")));

   		} catch (Exception e) {
   			fail(ConnectorTestUtils.getStackTrace(e));
   		}
        
   	}

}