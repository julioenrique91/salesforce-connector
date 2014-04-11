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

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.partner.DeletedRecord;
import com.sforce.soap.partner.GetDeletedResult;
import com.sforce.soap.partner.SaveResult;



public class GetDeletedTestCases extends SalesforceTestParent {

	@Before
	public void setUp() throws Exception {
    	
    	List<String> sObjectsIds = new ArrayList<String>();
    	
		loadTestRunMessage("getDeletedTestData");
        
        List<SaveResult> saveResultsList =  runFlowAndGetPayload("create-from-message");
        
        Iterator<SaveResult> saveResultsIter = saveResultsList.iterator();  

		while (saveResultsIter.hasNext()) {
			
			SaveResult saveResult = saveResultsIter.next();
			sObjectsIds.add(saveResult.getId());
			
		}

		upsertOnTestRunMessage("idsToDeleteFromMessage", sObjectsIds);
		
		runFlowAndGetPayload("delete-from-message");
		
		// because of the rounding applied to the seconds 
		Thread.sleep(AFTER_DELETE_DELAY);

	}
	
	@Category({RegressionTests.class})
	@Test
	public void testGetDeleted() {
		
		List<String> createdRecordsIds = getTestRunMessageValue("idsToDeleteFromMessage");
		List<String> deletedRecordsIds = new ArrayList<String>();
		
		try {
			
			GetDeletedResult deletedResult =  runFlowAndGetPayload("get-deleted");
			
			DeletedRecord[] deletedRecords = deletedResult.getDeletedRecords();
			
			assertTrue(deletedRecords != null && deletedRecords.length > 0);
			
			for (int i = 0; i < deletedRecords.length; i++) {
				deletedRecordsIds.add(((DeletedRecord) deletedRecords[i]).getId());
			}
			
			assertTrue(deletedRecordsIds.containsAll(createdRecordsIds));
		
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
		
	}

}