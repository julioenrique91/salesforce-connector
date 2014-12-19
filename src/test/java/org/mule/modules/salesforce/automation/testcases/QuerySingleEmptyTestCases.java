package org.mule.modules.salesforce.automation.testcases;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
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
import org.mule.transport.NullPayload;

import com.sforce.soap.partner.SaveResult;

public class QuerySingleEmptyTestCases extends SalesforceTestParent {
	
	@Before
	public void setUp() throws Exception {
    	
    	List<String> sObjectsIds = new ArrayList<String>();
			
		initializeTestRunMessage("querySingleEmptyTestData");
		
	    List<SaveResult> saveResultsList =  runFlowAndGetPayload("create-from-message");
	    Iterator<SaveResult> saveResultsIter = saveResultsList.iterator();  
	
	    List<Map<String,Object>> sObjects = getTestRunMessageValue("sObjectFieldMappingsFromMessage");
		Iterator<Map<String,Object>> sObjectsIterator = sObjects.iterator();
	    
		while (saveResultsIter.hasNext()) {
			SaveResult saveResult = saveResultsIter.next();
			Map<String,Object> sObject = (Map<String, Object>) sObjectsIterator.next();
			sObjectsIds.add(saveResult.getId());
	        sObject.put("Id", saveResult.getId());
		}
	
		upsertOnTestRunMessage("idsToDeleteFromMessage", sObjectsIds);
	}
	
	@After
	public void tearDown() throws Exception {
		runFlowAndGetPayload("delete-from-message");
	}
	
	@Category({RegressionTests.class})
	@Test
	public void testQuerySingle() {
		try {
			Object firstRecord =  runFlowAndGetPayload("query-single");
			assertTrue(firstRecord instanceof NullPayload); 
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
		
	}

	
}
