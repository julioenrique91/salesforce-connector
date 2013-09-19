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
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.partner.SaveResult;



public class RetrieveTestCases extends SalesforceTestParent {

	@Before
	public void setUp() throws Exception {
    	
    	List<String> sObjectsIds = new ArrayList<String>();

		loadTestRunMessage("retrieveTestData");

        List<SaveResult> saveResultsList =  runFlowAndGetPayload("create-from-message");
        Iterator<SaveResult> saveResultsIter = saveResultsList.iterator();  
        
		while (saveResultsIter.hasNext()) {
			
			SaveResult saveResult = saveResultsIter.next();
			sObjectsIds.add(saveResult.getId());
			
		}

		upsertOnTestRunMessage("idsToRetrieveFromMessage", sObjectsIds);
		upsertOnTestRunMessage("idsToDeleteFromMessage", sObjectsIds);

	}
	
	@After
	public void tearDown() throws Exception {
    	
		runFlowAndGetPayload("delete-from-message");
     
	}
	
	@Category({RegressionTests.class})
	@Test
	public void testRetrieveChildElementsFromMessage() {
		
		List<String> createdRecordIds = getTestRunMessageValue("idsToRetrieveFromMessage");
		List<String> fieldsToRetrieve = getTestRunMessageValue("fieldsToRetrieveFromMessage");
		
		try {
			
			List<Map<String, Object>> records =  runFlowAndGetPayload("retrieve-from-message");
	
	        Iterator<Map<String, Object>> recordsIter = records.iterator();  

			while (recordsIter.hasNext()) {
				
				Map<String, Object> sObject = recordsIter.next();
				assertTrue(createdRecordIds.contains(sObject.get("Id").toString())); 
				
				for (int index = 0; index < fieldsToRetrieve.size(); index++) {
					assertTrue(sObject.containsKey(fieldsToRetrieve.get(index).toString())); 
			    }

			}
		
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
		
	}

}