package org.mule.modules.salesforce.automation.testcases;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.partner.SaveResult;

public class CreateEmptyTestCases extends SalesforceTestParent {
	
    @Category({RegressionTests.class})
	@Test
	@Ignore("saveResults object (createSingle() method, SalesforceConnector) will always have length >= 1")
	public void testCreateSingleChildElementsFromMessage() {
		try {
			initializeTestRunMessage("createEmptyRecord");
	        SaveResult saveResult = runFlowAndGetPayload("create-empty-from-message");
	        assertNull(saveResult);
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
	}
}
