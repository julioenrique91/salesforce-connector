package org.mule.modules.salesforce.automation.testcases;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.metadata.SaveResult;

public class ReadMetadataTestCases extends SalesforceTestParent{
	private List<String> fullNames = new ArrayList<String>();
	private List<SaveResult> metadatas;
	
	@Before
	public void setUp() throws Exception {
		initializeTestRunMessage("createRemoteSiteSettingMetadataTestData");
		metadatas = runFlowAndGetPayload("create-metadata");
	}
	
	@Category({ RegressionTests.class })
	@Test
	public void testListMetadata() {
		try {
			for (SaveResult metadata: metadatas) {
				fullNames.add(metadata.getFullName());
			}
			upsertOnTestRunMessage("fullNames", fullNames);
			
			List<Map<String, Object>> metadatas = runFlowAndGetPayload("read-metadata");
			assertTrue(metadatas.size() > 0);
			assertTrue(metadatas.size() == fullNames.size());
		}
		catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
	}
	
	@After
	public void tearDown() throws Exception {
		runFlowAndGetPayload("delete-metadata");
	}
}
