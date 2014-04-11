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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.async.BatchInfo;
import com.sforce.async.BatchResult;
import com.sforce.async.JobInfo;



public class CreateBatchForQueryTestCases extends SalesforceTestParent {
	
	@Before
	public void setUp() throws Exception {


		
		loadTestRunMessage("createBatchForQueryTestData");
	
		JobInfo jobInfo = runFlowAndGetPayload("create-job");
		
		upsertOnTestRunMessage("jobId", jobInfo.getId());
		upsertOnTestRunMessage("jobInfoRef", jobInfo);

	}
	
	@After
	public void tearDown() throws Exception {

		runFlowAndGetPayload("delete-from-message");
		runFlowAndGetPayload("close-job");
		
	}
	
    @Category({RegressionTests.class})
	@Test
	public void testCreateBatchForQuery() {
    	
    	BatchInfo batchInfo;
    	
		try {
  
			batchInfo = runFlowAndGetPayload("create-batch-for-query");
			
			do {
				
				Thread.sleep(BATCH_PROCESSING_DELAY);
				upsertOnTestRunMessage("batchInfoRef", batchInfo);
				batchInfo = runFlowAndGetPayload("batch-info");

			} while (batchInfo.getState().equals(com.sforce.async.BatchStateEnum.InProgress) || batchInfo.getState().equals(com.sforce.async.BatchStateEnum.Queued));
	
			assertTrue(batchInfo.getState().equals(com.sforce.async.BatchStateEnum.Completed));
			
			assertBatchSucessAndGetSObjectIds((BatchResult) runFlowAndGetPayload("batch-result")); 
	        
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
    	     
	}

}