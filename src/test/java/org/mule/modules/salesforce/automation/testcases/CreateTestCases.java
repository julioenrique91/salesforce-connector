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
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.salesforce.automation.SmokeTests;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.partner.SaveResult;



public class CreateTestCases extends SalesforceTestParent {
	
    @Category({SmokeTests.class, RegressionTests.class})
	@Test
	public void testCreateChildElementsFromMessage() {
    	List<String> sObjectsIds = new ArrayList<String>(); 	
		try {	
			initializeTestRunMessage("createRecord");
	        List<SaveResult> saveResults =  runFlowAndGetPayload("create-from-message");
	        Iterator<SaveResult> iter = saveResults.iterator();  
			while (iter.hasNext()) {
				SaveResult saveResult = iter.next();
				assertTrue(saveResult.getSuccess());
				sObjectsIds.add(saveResult.getId());	
			}
			upsertOnTestRunMessage("idsToDeleteFromMessage", sObjectsIds);
			runFlowAndGetPayload("delete-from-message");
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
	}
    
    @Category({RegressionTests.class})
	@Test
	public void testCreatePassingHeaders() {
    	initializeTestRunMessage("createPassingHeadersTestData");
		try {
			List<SaveResult> saveResults = runFlowAndGetPayload("create-passing-headers"); 
			Iterator<SaveResult> iter = saveResults.iterator(); 
			while (iter.hasNext()) {
				SaveResult saveResult = iter.next();
				assertTrue(!saveResult.getSuccess());
				String errorStatusCode = saveResult.getErrors()[0].getStatusCode().name().toString();
				assertTrue(errorStatusCode.equals("ALL_OR_NONE_OPERATION_ROLLED_BACK")|errorStatusCode.equals("REQUIRED_FIELD_MISSING"));
			}
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
	}
    
    
    

}