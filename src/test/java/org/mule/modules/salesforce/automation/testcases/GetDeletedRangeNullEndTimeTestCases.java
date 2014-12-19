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
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.partner.DeletedRecord;
import com.sforce.soap.partner.GetDeletedResult;
import com.sforce.soap.partner.SaveResult;

public class GetDeletedRangeNullEndTimeTestCases extends SalesforceTestParent {

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
		
		List<String> deletedRecordsIds = new ArrayList<String>();
		for (DeletedRecord deletedRecord : deletedRecords) {
			deletedRecordsIds.add(deletedRecord.getId());
		}
		
		upsertOnTestRunMessage("deletedRecordsIds", deletedRecordsIds);
		
		GregorianCalendar endTime = (GregorianCalendar) ((DeletedRecord) deletedRecords[0]).getDeletedDate();
		endTime.add(GregorianCalendar.MINUTE, 1);
		
		GregorianCalendar startTime = (GregorianCalendar) endTime.clone(); 
		startTime.add(GregorianCalendar.MINUTE, -(Integer.parseInt((String) getTestRunMessageValue("duration"))));
		
		endTime = null;
		upsertOnTestRunMessage("endTime", endTime);
		upsertOnTestRunMessage("startTime", startTime);
     
	}
	
	@Category({RegressionTests.class})
	@Test
	public void testGetDeletedRange() {
		
		List<String> deletedRecordsIds = getTestRunMessageValue("deletedRecordsIds");
		
		try {
			
			GetDeletedResult deletedResult =  (GetDeletedResult) runFlowAndGetPayload("get-deleted-range");
			DeletedRecord[] deletedRecords = deletedResult.getDeletedRecords();
			
			assertTrue(deletedRecords != null && deletedRecords.length > 0);

			for (int i = 0; i < deletedRecords.length; i++) {
				assertTrue(deletedRecordsIds.contains(((DeletedRecord) deletedRecords[i]).getId())); 
		     }
		
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
		
	}

}
