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
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.async.BatchInfo;
import com.sforce.async.BatchResult;
import com.sforce.async.Result;
import com.sforce.soap.partner.SaveResult;



public class UpsertBulkTestCases extends SalesforceTestParent {

	private void assertBatchSucessAndUpdatedSObjectId(BatchResult batchResult) {
		
		List<String> sObjectsIds = new ArrayList<String>();
		
		boolean isSuccess = true;
		Result[] results = batchResult.getResult();
		
		for (int index=0; index<results.length; index++) {
			
			if (results[index].isSuccess()) {
				sObjectsIds.add(results[index].getId());
			} else {
				isSuccess = false;	
			}
	
		}
		
		upsertOnTestRunMessage("idsToDeleteFromMessage", sObjectsIds);
		
		assertTrue(sObjectsIds.contains(((HashMap<String,Object>) getBeanFromContext("upsertBulkSObjectToBeUpdated")).get("Id")));
		assertTrue(isSuccess);

	}
	
	@Before
	public void setUp() throws Exception {
 
		loadTestRunMessage("upsertBulkTestData");
		
        SaveResult saveResult = runFlowAndGetPayload("create-single-from-message");
		((HashMap<String,Object>) getBeanFromContext("upsertBulkSObjectToBeUpdated")).put("Id", saveResult.getId());
   
	}

	@After
	public void tearDown() throws Exception {
		
		runFlowAndGetPayload("delete-from-message");
		
	}
	
	@Category({RegressionTests.class})
	@Test
	public void testUpsertBulk() {
		
    	BatchInfo batchInfo;
		
		try {
			
			batchInfo = runFlowAndGetPayload("upsert-bulk");
			
			do {
				
				Thread.sleep(BATCH_PROCESSING_DELAY);
				upsertOnTestRunMessage("batchInfoRef", batchInfo);
				batchInfo = runFlowAndGetPayload("batch-info");

			} while (batchInfo.getState().equals(com.sforce.async.BatchStateEnum.InProgress) || batchInfo.getState().equals(com.sforce.async.BatchStateEnum.Queued));
	
			assertTrue(batchInfo.getState().equals(com.sforce.async.BatchStateEnum.Completed));
			
			assertBatchSucessAndUpdatedSObjectId((BatchResult) runFlowAndGetPayload("batch-result")); 
		
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
		
	}

}