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
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.UpsertResult;



public class UpsertTestCases extends SalesforceTestParent {

	@Before
	public void setUp() throws Exception {
			
		loadTestRunMessage("upsertTestData");
		
        SaveResult saveResult = (SaveResult) runFlowAndGetPayload("create-single-from-message");
        
        Map<String,Object> sObjectToBeUpdated = getTestRunMessageValue("sObjectFieldMappingsFromMessage");
        sObjectToBeUpdated.put("Id", saveResult.getId());
        upsertOnTestRunMessage("sObjectToBeUpdatedId", saveResult.getId());
        
        List<Map<String,Object>> sObjectsList = getTestRunMessageValue("salesforceSObjectsListFromMessage");
        sObjectsList.add(sObjectToBeUpdated);
     
	}
	
	@After
	public void tearDown() throws Exception {
    	
		runFlowAndGetPayload("delete-from-message");
     
	}
	
	@Category({RegressionTests.class})
	@Test
	public void testUpsertChildElementsFromMessage() {
		
    	List<String> sObjectsIds = new ArrayList<String>();
    	String sObjectToBeUpdatedId = getTestRunMessageValue("sObjectToBeUpdatedId");
			
		try {
			
			List<UpsertResult> upsertResults = runFlowAndGetPayload("upsert-from-message");
	        
	        Iterator<UpsertResult> upsertResultsIter = upsertResults.iterator();  
	        
			while (upsertResultsIter.hasNext()) {
				
				UpsertResult upsertResult = upsertResultsIter.next();
				sObjectsIds.add(upsertResult.getId());
				
				assertTrue(upsertResult.getSuccess());
				
				if (!sObjectToBeUpdatedId.equals(upsertResult.getId())) {
					assertTrue(upsertResult.isCreated());
				}
					 
			}

			upsertOnTestRunMessage("idsToDeleteFromMessage", sObjectsIds);	        
		
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
		
	}

}