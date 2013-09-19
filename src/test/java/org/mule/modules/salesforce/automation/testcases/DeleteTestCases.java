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
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.partner.DeleteResult;
import com.sforce.soap.partner.SaveResult;



public class DeleteTestCases extends SalesforceTestParent {

	@Before
	public void setUp() throws Exception {
    	
    	List<String> sObjectsIds = new ArrayList<String>();
			
		loadTestRunMessage("createRecord");

        List<SaveResult> saveResults =  runFlowAndGetPayload("create-from-message");
        Iterator<SaveResult> iter = saveResults.iterator();  

		while (iter.hasNext()) {
			
			SaveResult saveResult = iter.next();
			sObjectsIds.add(saveResult.getId());
			
		}

		upsertOnTestRunMessage("idsToDeleteFromMessage", sObjectsIds);

	}
	
	@Category({SmokeTests.class, RegressionTests.class})
	@Test
	public void testDeleteChildElementsFromMessage() {
		
		try {

			List<DeleteResult> deleteResults = runFlowAndGetPayload("delete-from-message");
			Iterator<DeleteResult> iter = deleteResults.iterator();  
	
			while (iter.hasNext()) {
				
				DeleteResult deleteResult = iter.next();
				assertTrue(deleteResult.getSuccess());
				
			}

		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
		
	}

}