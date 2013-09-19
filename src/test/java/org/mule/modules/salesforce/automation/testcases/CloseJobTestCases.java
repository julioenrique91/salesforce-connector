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

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.async.JobInfo;



public class CloseJobTestCases extends SalesforceTestParent {

	@Before
	public void setUp() throws Exception {

		loadTestRunMessage("closeJobTestData");

		JobInfo jobInfo = runFlowAndGetPayload("create-job");

		upsertOnTestRunMessage("jobId", jobInfo.getId());
			
	}
	
    @Category({RegressionTests.class})
	@Test
	public void testCloseJob() {

		try {

			JobInfo jobInfo = runFlowAndGetPayload("close-job");

			assertEquals(com.sforce.async.JobStateEnum.Closed, jobInfo.getState());
			assertEquals(getTestRunMessageValue("jobId").toString(), jobInfo.getId());
			assertEquals(getTestRunMessageValue("concurrencyMode").toString(), jobInfo.getConcurrencyMode().toString());
			assertEquals(getTestRunMessageValue("operation").toString(), jobInfo.getOperation().toString());
			assertEquals(getTestRunMessageValue("contentType").toString(), jobInfo.getContentType().toString());

		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
		
	}

}