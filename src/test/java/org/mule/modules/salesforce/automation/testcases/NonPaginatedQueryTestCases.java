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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;
import org.mule.streaming.ConsumerIterator;

import com.sforce.soap.partner.SaveResult;

public class NonPaginatedQueryTestCases extends SalesforceTestParent {

	@Before
	public void setUp() throws Exception {
    	
    	List<String> sObjectsIds = new ArrayList<String>();

		loadTestRunMessage("queryTestData");
        
        List<SaveResult> saveResultsList =  runFlowAndGetPayload("create-from-message");
        Iterator<SaveResult> saveResultsIter = saveResultsList.iterator();  

        List<Map<String,Object>> sObjects = getTestRunMessageValue("sObjectFieldMappingsFromMessage");
		Iterator<Map<String,Object>> sObjectsIterator = sObjects.iterator();
        
		while (saveResultsIter.hasNext()) {
			
			SaveResult saveResult = saveResultsIter.next();
			Map<String,Object> sObject = (Map<String, Object>) sObjectsIterator.next();
			sObjectsIds.add(saveResult.getId());
	        sObject.put("Id", saveResult.getId());
			
		}

		upsertOnTestRunMessage("idsToDeleteFromMessage", sObjectsIds);
     
	}
	
	@After
	public void tearDown() throws Exception {
			
		runFlowAndGetPayload("delete-from-message");

	}
	
	@Category({RegressionTests.class})
	@Test
	public void testNonPaginatedQuery() {
		
		List<String> queriedRecordIds = getTestRunMessageValue("idsToDeleteFromMessage");
		List<String> returnedSObjectsIds = new ArrayList<String>();
		
		try {
			
			ConsumerIterator<Map<String, Object>> iter = runFlowAndGetPayload("non-paginated-query");
			
			int count = 0;
			while (iter.hasNext()) {
				Map<String, Object> sObject = iter.next();
				returnedSObjectsIds.add(sObject.get("Id").toString());
				System.out.println(sObject.get("Id").toString());
				count++;
			}
			
			assertTrue(returnedSObjectsIds.size() > 0);
			assertEquals(count, iter.size());

			for (int index = 0; index < queriedRecordIds.size(); index++) {
				assertTrue(returnedSObjectsIds.contains(queriedRecordIds.get(index).toString())); 
		     }
		
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
		
	}
}
