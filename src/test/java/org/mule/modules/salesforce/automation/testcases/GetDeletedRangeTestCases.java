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
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.partner.DeletedRecord;
import com.sforce.soap.partner.GetDeletedResult;
import com.sforce.soap.partner.SaveResult;



public class GetDeletedRangeTestCases extends SalesforceTestParent {

	@Before
	public void setUp() throws Exception {
    	
    	List<String> sObjectsIds = new ArrayList<String>();
		
		loadTestRunMessage("getDeletedRangeTestData");

        List<SaveResult> saveResults = runFlowAndGetPayload("create-from-message");
        Iterator<SaveResult> saveResultsIter = saveResults.iterator();  

		while (saveResultsIter.hasNext()) {
			
			SaveResult saveResult = saveResultsIter.next();
			sObjectsIds.add(saveResult.getId());
			
		}

		upsertOnTestRunMessage("idsToDeleteFromMessage", sObjectsIds);
		
		runFlowAndGetPayload("delete-from-message");
		
		GetDeletedResult deletedResult =  runFlowAndGetPayload("get-deleted");
		DeletedRecord[] deletedRecords = deletedResult.getDeletedRecords();
		
		GregorianCalendar endTime = (GregorianCalendar) ((DeletedRecord) deletedRecords[0]).getDeletedDate();
		endTime.add(GregorianCalendar.MINUTE, 1);
		
		GregorianCalendar startTime = (GregorianCalendar) endTime.clone(); 
		startTime.add(GregorianCalendar.MINUTE, -(Integer.parseInt((String) getTestRunMessageValue("duration"))));
		
		upsertOnTestRunMessage("endTime", endTime);
		upsertOnTestRunMessage("startTime", startTime);
     
	}
	
	@Category({SmokeTests.class, RegressionTests.class})
	@Test
	public void testGetDeletedRange() {
		
		List<String> createdRecordsIds = getTestRunMessageValue("idsToDeleteFromMessage");
		
		try {
			
			GetDeletedResult deletedResult =  (GetDeletedResult) runFlowAndGetPayload("get-deleted-range");
			DeletedRecord[] deletedRecords = deletedResult.getDeletedRecords();
			
			assertTrue(deletedRecords != null && deletedRecords.length > 0);

			for (int i = 0; i < deletedRecords.length; i++) {
				assertTrue(createdRecordsIds.contains(((DeletedRecord) deletedRecords[i]).getId())); 
		     }
		
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
		
	}

}