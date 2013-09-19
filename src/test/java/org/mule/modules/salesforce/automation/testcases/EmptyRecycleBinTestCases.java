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
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.partner.EmptyRecycleBinResult;
import com.sforce.soap.partner.SaveResult;



public class EmptyRecycleBinTestCases extends SalesforceTestParent {
	
    @Category({RegressionTests.class})
    @Test
	public void testEmptyRecycleBin() {
    	
    	List<String> sObjectsIds = new ArrayList<String>();
    	
		try {
			
			loadTestRunMessage("emptyRecycleBinTestData");

	        List<SaveResult> saveResults =  runFlowAndGetPayload("create-from-message");
	        Iterator<SaveResult> saveResultsIter = saveResults.iterator();  

			while (saveResultsIter.hasNext()) {
				
				SaveResult saveResult = saveResultsIter.next();
				sObjectsIds.add(saveResult.getId());
				
			}

			upsertOnTestRunMessage("idsToDeleteFromMessage", sObjectsIds);
			upsertOnTestRunMessage("idsRef", sObjectsIds);
			
			runFlowAndGetPayload("delete-from-message");
			
	        List<EmptyRecycleBinResult> emptyRecycleBinResults =  runFlowAndGetPayload("empty-recycle-bin");
	        Iterator<EmptyRecycleBinResult> emptyRecycleBinResultsIter = emptyRecycleBinResults.iterator(); 
	        
			while (emptyRecycleBinResultsIter.hasNext()) {
				
				EmptyRecycleBinResult emptyRecycleBinResult = emptyRecycleBinResultsIter.next();
				assertTrue(emptyRecycleBinResult.getSuccess());
				assertTrue(sObjectsIds.contains(emptyRecycleBinResult.getId()));
				
			}

		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
     
	}

}