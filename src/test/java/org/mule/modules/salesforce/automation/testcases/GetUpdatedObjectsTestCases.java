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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.partner.SaveResult;

public class GetUpdatedObjectsTestCases extends SalesforceTestParent {
		
	@Before
	public void setUp() throws Exception {
    	
		List<String> sObjectsIds = new ArrayList<String>();
		
		loadTestRunMessage("getUpdatedObjectsTestData");
		Map<String,Object> sObject = getTestRunMessageValue("salesforceObjectFromMessage");

        SaveResult saveResult = (SaveResult) runFlowAndGetPayload("create-single-from-message");
        sObjectsIds.add(saveResult.getId());
        sObject.put("Id", saveResult.getId());
		upsertOnTestRunMessage("idsToDeleteFromMessage", sObjectsIds);

		runFlowAndGetPayload("get-updated-objects");
		
		runFlowAndGetPayload("update-single-from-message");
		Thread.sleep(AFTER_UPDATE_DELAY);

	}
	
	@After
	public void tearDown() throws Exception {
    	
		runFlowAndGetPayload("delete-from-message");
		
	}
	
	@Category({RegressionTests.class})
	@Test
	public void testGetUpdatedObjects() {
		
		List<String> updatedRecordsList = getTestRunMessageValue("idsToDeleteFromMessage");
		List<String> returnedSObjectsIds;
		
		String anUpdatedRecordId = updatedRecordsList.get(0).toString();

		try {

	        returnedSObjectsIds = getReturnedSObjectsIds((List<Map<String, Object>>) runFlowAndGetPayload("get-updated-objects"));
			
			assertTrue(updatedRecordsList.size() > 0);
			assertTrue(returnedSObjectsIds.contains(anUpdatedRecordId)); 
			
			runFlowAndGetPayload("reset-updated-objects-timestamp");
			
	        returnedSObjectsIds = getReturnedSObjectsIds((List<Map<String, Object>>) runFlowAndGetPayload("get-updated-objects"));
			
			assertTrue(!returnedSObjectsIds.contains(anUpdatedRecordId)); 
			
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
		
	}

	private List<String> getReturnedSObjectsIds(List<Map<String, Object>> records) {

		List<String> sObjectsIds = new ArrayList<String>();
		Iterator<Map<String, Object>> iter;
        iter = records.iterator();  

		while (iter.hasNext()) {
			Map<String, Object> sObject = iter.next();
			sObjectsIds.add(sObject.get("Id").toString());	
		}
		
		return sObjectsIds;
		
	}

}