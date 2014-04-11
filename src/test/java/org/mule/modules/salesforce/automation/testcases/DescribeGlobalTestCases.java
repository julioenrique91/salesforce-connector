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
import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.soap.partner.DescribeGlobalSObjectResult;



public class DescribeGlobalTestCases extends SalesforceTestParent {
	
    @Category({RegressionTests.class})
	@Test
	public void testDescribeGlobal() {
    	
    	List<String> retrievedSObjectNames = new ArrayList<String>();
    	
		try {
			
			loadTestRunMessage("describeGlobalTestData");

			DescribeGlobalResult describeGlobalResult = runFlowAndGetPayload("describe-global");
			DescribeGlobalSObjectResult[] sObjects = describeGlobalResult.getSobjects();
			
			for (int index = 0; index<sObjects.length; index++) {
				retrievedSObjectNames.add(sObjects[index].getName());	
			}
			
	        assertTrue(retrievedSObjectNames.containsAll((List<String>) getTestRunMessageValue("expectedSObjectNames")));

		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}
     
	}

}