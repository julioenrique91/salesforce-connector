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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

public class CreateBatchStreamTestCases extends SalesforceTestParent {

	

	@Before
	public void setUp() throws Exception {


		loadTestRunMessage("createStreamBatchTestData");

		JobInfo jobInfo = (JobInfo) runFlowAndGetPayload("create-job");

		upsertOnTestRunMessage("jobId", jobInfo.getId());
		upsertOnTestRunMessage("jobInfoRef", jobInfo);

	}

	@After
	public void tearDown() throws Exception {

		runFlowAndGetPayload("delete-from-message");
		runFlowAndGetPayload("close-job");

	}

	@Category({ RegressionTests.class })
	@Test
	public void testCreateBatch() {
		BatchInfo batchInfo = null;
		FileInputStream fileInput = null;
		
		boolean isSuccess = true;

		List<String> sObjectsIds = new ArrayList<String>();
		
		try {
			
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("batchStreamInputData.csv").getFile());
			fileInput = new FileInputStream(file);
			upsertOnTestRunMessage("stream", fileInput);

			
			batchInfo = runFlowAndGetPayload("create-batch-stream");

			do {

				Thread.sleep(BATCH_PROCESSING_DELAY);
				upsertOnTestRunMessage("batchInfoRef", batchInfo);
				batchInfo = runFlowAndGetPayload("batch-info");

			} while (batchInfo.getState().equals(
					com.sforce.async.BatchStateEnum.InProgress)
					|| batchInfo.getState().equals(
							com.sforce.async.BatchStateEnum.Queued));

			assertTrue(batchInfo.getState().equals(
					com.sforce.async.BatchStateEnum.Completed));

			String operationResponse = IOUtils
					.toString((InputStream) runFlowAndGetPayload("batch-result-stream"));

			String lines[] = operationResponse.split("\n");
			for(int index=1;index<lines.length;index++){
				String[] auxLine=lines[index].split(",");
				sObjectsIds.add(auxLine[0]);
				assertEquals(auxLine[3],"\"\"");
				assertEquals(auxLine[1],"\"true\"");
			}

			upsertOnTestRunMessage("idsToDeleteFromMessage", sObjectsIds);

			assertTrue(isSuccess);

		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		} finally {
			try {
				fileInput.close();
			} catch (IOException e) {
				System.out.println("File closing error");
			}
		}

	}

}