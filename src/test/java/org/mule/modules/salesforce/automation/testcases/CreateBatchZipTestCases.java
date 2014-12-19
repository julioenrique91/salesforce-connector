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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;
import org.mule.util.IOUtils;

import com.sforce.async.BatchInfo;
import com.sforce.async.JobInfo;

public class CreateBatchZipTestCases extends SalesforceTestParent {

	
	@Before
	public void setUp() throws Exception {
		initializeTestRunMessage("createZipBatchTestData");
		
		JobInfo jobInfo = (JobInfo) runFlowAndGetPayload("create-job");

		upsertOnTestRunMessage("jobId", jobInfo.getId());
		upsertOnTestRunMessage("jobInfoRef", jobInfo);
	}
	
	@After
	public void tearDown() throws Exception {
		//runFlowAndGetPayload("delete-from-message");
		//runFlowAndGetPayload("close-job");
	}
	
	@Category({ RegressionTests.class })
	@Test
	@Ignore("The zip creation procedure is not straightforward. Some request.txt file has to be created. See full documentation for this at http://www.salesforce.com/us/developer/docs/api_asynch/api_asynch.pdf")
	public void testCreateBatch() {
		BatchInfo batchInfo = null;
		InputStream fileInput = null;
		
		boolean isSuccess = true;

		List<String> sObjectsIds = new ArrayList<String>();
		
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("batchStreamInputData.zip").getFile());
			fileInput = new FileInputStream (file);
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
