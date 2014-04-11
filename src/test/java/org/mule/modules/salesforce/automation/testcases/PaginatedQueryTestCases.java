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
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.QueryResultObject;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.partner.SaveResult;



public class PaginatedQueryTestCases extends SalesforceTestParent {

	@Before
	public void setUp() throws Exception {
    	
    	List<String> sObjectsIds = new ArrayList<String>();

		loadTestRunMessage("paginatedQueryTestData");

        SaveResult saveResult = runFlowAndGetPayload("create-single-from-message");
        
        sObjectsIds.add(saveResult.getId());
        
		upsertOnTestRunMessage("idsToDeleteFromMessage", sObjectsIds);
     
	}
	
	@After
	public void tearDown() throws Exception {

		runFlowAndGetPayload("delete-from-message");

	}
	
	@Category({RegressionTests.class})
	@Test
	public void testPaginatedQuery() {
		
		List<String> queriedRecordIds = getTestRunMessageValue("idsToDeleteFromMessage");
		String queriedRecordId = queriedRecordIds.get(0).toString();
		
		List<String> returnedSObjectsIds;
		QueryResultObject queryResult;
		List<Map<String, Object>> records;
		
		try {
			
			do {
				
				returnedSObjectsIds = new ArrayList<String>();
				
				queryResult =  (QueryResultObject) runFlowAndGetPayload("paginated-query");
				
				records =  (List<Map<String, Object>>) queryResult.getData();
		        
		        Iterator<Map<String, Object>> iter = records.iterator();  

				while (iter.hasNext()) {
					
					Map<String, Object> sObject = iter.next();
					returnedSObjectsIds.add(sObject.get("Id").toString());
					
				}
				
				assertTrue(returnedSObjectsIds.size() > 0);
				
				upsertOnTestRunMessage("queryResultObjectRef", queryResult);
				
				runFlowAndGetPayload("paginated-query-by-queryResultObject-ref");
				
				
			} while (!returnedSObjectsIds.contains(queriedRecordId) && queryResult.hasMore());
			
			assertTrue(returnedSObjectsIds.contains(queriedRecordId));
			
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
		
	}
	
	@Category({RegressionTests.class})
	@Test
	public void testPaginatedQueryWithDeletedRecords() {
		
		List<String> queriedRecordIds = getTestRunMessageValue("idsToDeleteFromMessage");
		String queriedRecordId = queriedRecordIds.get(0).toString();
		
		List<String> returnedSObjectsIds;
		QueryResultObject queryResult;
		List<Map<String, Object>> records;
		
		try {
			
			runFlowAndGetPayload("delete-from-message");

			Thread.sleep(60000);
			
			do {
				
				returnedSObjectsIds = new ArrayList<String>();
				
				queryResult =  (QueryResultObject) runFlowAndGetPayload("paginated-query-with-deleted-records");
				
				records =  (List<Map<String, Object>>) queryResult.getData();
		        
		        Iterator<Map<String, Object>> iter = records.iterator();  

				while (iter.hasNext()) {
					
					Map<String, Object> sObject = iter.next();
					returnedSObjectsIds.add(sObject.get("Id").toString());
					
				}
				
				assertTrue(returnedSObjectsIds.size() > 0);
				
				upsertOnTestRunMessage("queryResultObjectRef", queryResult);
				
				runFlowAndGetPayload("paginated-query-by-queryResultObject-ref");
				
			} while (!returnedSObjectsIds.contains(queriedRecordId) && queryResult.hasMore());
			
			assertTrue(returnedSObjectsIds.contains(queriedRecordId));

		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
		
	}

}