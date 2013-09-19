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

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MuleEvent;
import org.mule.api.processor.MessageProcessor;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.async.JobInfo;



public class CreateJobTestCases extends SalesforceTestParent {
	
	@After
	public void tearDown() throws Exception {

		runFlowAndGetPayload("close-job");
		
	}
	
    @Category({RegressionTests.class})
	@Test
	public void testCreateJob() {
    	
		loadTestRunMessage("createJobTestData");
    	
		try {
			
			JobInfo jobInfo = runFlowAndGetPayload("create-job");
			
			assertEquals(com.sforce.async.JobStateEnum.Open, jobInfo.getState());
			assertEquals(getTestRunMessageValue("concurrencyMode").toString(), jobInfo.getConcurrencyMode().toString());
			assertEquals(getTestRunMessageValue("operation").toString(), jobInfo.getOperation().toString());
			assertEquals(getTestRunMessageValue("contentType").toString(), jobInfo.getContentType().toString());

			upsertOnTestRunMessage("jobId", jobInfo.getId());
	        
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
     
	}

}