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

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;
import org.mule.util.IOUtils;

import com.sforce.async.BatchInfo;
import com.sforce.async.JobInfo;



public class QueryResultStreamTestCases extends SalesforceTestParent {
	
	
	@Before
	public void setUp() throws Exception {
		
    	
		loadTestRunMessage("createBatchForQueryTestData");
		
		try {

			JobInfo jobInfo = runFlowAndGetPayload("create-job");
			
			upsertOnTestRunMessage("jobId", jobInfo.getId());
			upsertOnTestRunMessage("jobInfoRef", jobInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
	}
	
	@After
	public void tearDown() throws Exception {

		runFlowAndGetPayload("close-job");
		
	}
	
    @Category({RegressionTests.class})
	@Test
	public void testQueryResultStream() {
    	
    	BatchInfo batchInfo;
    	
		boolean isSuccess = true;

		try {
  
			batchInfo = runFlowAndGetPayload("create-batch-for-query");
			
			do {
				
				Thread.sleep(BATCH_PROCESSING_DELAY);
				upsertOnTestRunMessage("batchInfoRef", batchInfo);
				batchInfo = runFlowAndGetPayload("batch-info");

			} while (batchInfo.getState().equals(com.sforce.async.BatchStateEnum.InProgress) || batchInfo.getState().equals(com.sforce.async.BatchStateEnum.Queued));
	
			assertTrue(batchInfo.getState().equals(com.sforce.async.BatchStateEnum.Completed));
			
			String operationResponse = IOUtils.toString((InputStream) runFlowAndGetPayload("query-result-stream"));
			
			isSuccess = StringUtils.contains(operationResponse, "<queryResult");
			
			assertTrue(isSuccess);
			
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
    	     
	}

}