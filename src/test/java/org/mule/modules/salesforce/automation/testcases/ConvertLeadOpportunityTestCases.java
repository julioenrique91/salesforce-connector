package org.mule.modules.salesforce.automation.testcases;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.partner.LeadConvertResult;
import com.sforce.soap.partner.SaveResult;

public class ConvertLeadOpportunityTestCases extends SalesforceTestParent {

	private static final String OPPORTUNITY_NAME = "opportunity";
	
	@Before
	public void setUp() throws Exception {
    	
    	List<String> sObjectsIds = new ArrayList<String>();
    	SaveResult saveResult;
    	
		initializeTestRunMessage("convertLeadTestData");
		
        saveResult = runFlowAndGetPayload("create-single-from-message", "convertLeadLead");
        sObjectsIds.add(saveResult.getId());
        upsertOnTestRunMessage("leadId", saveResult.getId());
		upsertOnTestRunMessage("idsToDeleteFromMessage", sObjectsIds);
        upsertOnTestRunMessage("opportunityName", OPPORTUNITY_NAME);
	}
	
	@After
	public void tearDown() throws Exception {
		runFlowAndGetPayload("delete-from-message");
	}
	
	@Category({RegressionTests.class})
	@Test
	public void testConvertLeadDefaultValues() {
		try {
			LeadConvertResult leadConvertResult =  runFlowAndGetPayload("convert-lead-with-opportunity");
			assertTrue(leadConvertResult.isSuccess());
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
		
	}

}
