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
import java.util.HashMap;
import java.util.Iterator;
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

import com.sforce.soap.partner.GetUpdatedResult;
import com.sforce.soap.partner.SaveResult;



public class GetUpdatedTestCases extends SalesforceTestParent {

	@Before
	public void setUp() throws Exception {
    	
    	List<String> sObjectsIds = new ArrayList<String>();

		loadTestRunMessage("getUpdatedTestData");
        
        List<SaveResult> saveResultsList =  runFlowAndGetPayload("create-from-message");
        Iterator<SaveResult> saveResultsIter = saveResultsList.iterator();  

        List<Map<String,Object>> sObjects = getTestRunMessageValue("salesforceSObjectsListFromMessage");
		Iterator<Map<String,Object>> sObjectsIterator = sObjects.iterator();
        
		while (saveResultsIter.hasNext()) {
			
			SaveResult saveResult = saveResultsIter.next();
			Map<String,Object> sObject = (Map<String, Object>) sObjectsIterator.next();
			sObjectsIds.add(saveResult.getId());
	        sObject.put("Id", saveResult.getId());
			
		}

		upsertOnTestRunMessage("idsToDeleteFromMessage", sObjectsIds);
		
		runFlowAndGetPayload("update-from-message");

		// because of the rounding applied to the seconds 
		Thread.sleep(60000);
		
	}
	
	@After
	public void tearDown() throws Exception {
    	
		runFlowAndGetPayload("delete-from-message");

	}
	
	@Category({SmokeTests.class, RegressionTests.class})
	@Test
	public void testGetUpdated() {
		
		List<String> createdRecordsIds = getTestRunMessageValue("idsToDeleteFromMessage");
		
		try {
			
			GetUpdatedResult updatedResult = runFlowAndGetPayload("get-updated");

			String[] ids = updatedResult.getIds();
			
			assertTrue(ids != null && ids.length > 0);

			for (int i = 0; i < ids.length; i++) {
				assertTrue(createdRecordsIds.contains(ids[i].toString())); 
		     }
		
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
		
	}

}