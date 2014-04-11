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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.salesforce.automation.SmokeTests;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.partner.SaveResult;



public class UpdateSingleTestCases extends SalesforceTestParent {

	@Before
	public void setUp() throws Exception {
    	
    	List<String> sObjectsIds = new ArrayList<String>();
    	
    	loadTestRunMessage("updateSingleTestData");
		
        SaveResult saveResult = runFlowAndGetPayload("create-single-from-message");
        Map<String,Object> sObject = getTestRunMessageValue("salesforceObjectFromMessage");
        
        sObjectsIds.add(saveResult.getId());
        sObject.put("Id", saveResult.getId());

		upsertOnTestRunMessage("idsToDeleteFromMessage", sObjectsIds);
     
	}
	
	@After
	public void tearDown() throws Exception {
 
		runFlowAndGetPayload("delete-from-message");

	}
	
	@Category({SmokeTests.class, RegressionTests.class})
	@Test
	public void testUpdateSingleChildElementsFromMessage() {
			
		try {			

			SaveResult saveResult =  runFlowAndGetPayload("update-single-from-message");

			assertTrue(saveResult.getSuccess());
		
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
		
	}

}