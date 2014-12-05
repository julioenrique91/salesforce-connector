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
import java.util.Iterator;
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

import com.sforce.soap.partner.SaveResult;



public class SearchTestCases extends SalesforceTestParent {

	@Before
	public void setUp() throws Exception {
    	
    	List<String> sObjectsIds = new ArrayList<String>();
			
		loadTestRunMessage("searchTestData");
        
        List<SaveResult> saveResultsList =  runFlowAndGetPayload("create-from-message");
        Iterator<SaveResult> saveResultsIter = saveResultsList.iterator();  
        
		while (saveResultsIter.hasNext()) {
			
			SaveResult saveResult = saveResultsIter.next();
			sObjectsIds.add(saveResult.getId());
			
		}

		upsertOnTestRunMessage("idsToDeleteFromMessage", sObjectsIds);
		
		Thread.sleep(BEFORE_SEARCH_DELAY);
 
	}
	
	@After
	public void tearDown() throws Exception {
    	
		runFlowAndGetPayload("delete-from-message");
     
	}
	
	@Category({RegressionTests.class})
	@Test
	@Ignore(value = "Will be fixed in next release")
	public void testSearch() {
		
		List<String> createdRecordIds = getTestRunMessageValue("idsToDeleteFromMessage");
		List<String> returnedSObjectsIds = new ArrayList<String>();
		
		try {

			List<Map<String, Object>> returnedRecordIds = runFlowAndGetPayload("search");
	        
			assertTrue(returnedRecordIds.size() > 0);
			
	        Iterator<Map<String, Object>> iter = returnedRecordIds.iterator();   

			while (iter.hasNext()) {
				
				Map<String, Object> sObject = iter.next();
				returnedSObjectsIds.add(sObject.get("Id").toString());
				
			}
			
			for (int index = 0; index < createdRecordIds.size(); index++) {
				assertTrue(returnedSObjectsIds.contains(createdRecordIds.get(index).toString())); 
		     }
		
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
		
	}

}