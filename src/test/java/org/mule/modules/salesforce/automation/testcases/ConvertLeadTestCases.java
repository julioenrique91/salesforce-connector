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
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.partner.LeadConvertResult;
import com.sforce.soap.partner.SaveResult;



public class ConvertLeadTestCases extends SalesforceTestParent {

	@Before
	public void setUp() throws Exception {
    	
    	List<String> sObjectsIds = new ArrayList<String>();
    	SaveResult saveResult;
    	
		loadTestRunMessage("convertLeadTestData");
		
        saveResult = runFlowAndGetPayload("create-single-from-message", "convertLeadLead");
        sObjectsIds.add(saveResult.getId());
        upsertOnTestRunMessage("leadId", saveResult.getId());

		upsertOnTestRunMessage("idsToDeleteFromMessage", sObjectsIds);
  
     
	}
	
	@After
	public void tearDown() throws Exception {
		
		runFlowAndGetPayload("delete-from-message");

	}
	
	@Category({RegressionTests.class})
	@Test
	public void testConvertLeadDefaultValues() {
			
		try {

			LeadConvertResult leadConvertResult =  runFlowAndGetPayload("convert-lead-default-values");

			assertTrue(leadConvertResult.isSuccess());
		
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
		
	}

}