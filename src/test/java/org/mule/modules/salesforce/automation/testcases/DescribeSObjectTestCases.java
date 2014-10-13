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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.soap.partner.DescribeGlobalSObjectResult;
import com.sforce.soap.partner.DescribeSObjectResult;

public class DescribeSObjectTestCases extends SalesforceTestParent {

	Set<String> salesforceTypes;

	@Before
	public void setUp() throws Exception {

		salesforceTypes = new HashSet<String>();

		try {

			initializeTestRunMessage("describeGlobalTestData");

			DescribeGlobalResult describeGlobalResult = runFlowAndGetPayload("describe-global");
			DescribeGlobalSObjectResult[] sObjects = describeGlobalResult
					.getSobjects();

			for (int index = 0; index < sObjects.length; index++) {
				salesforceTypes.add(sObjects[index].getName());
			}

		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}

	}

	@Category({ RegressionTests.class })
	@Test
	public void testDescribeSObjectForAllSalesforceTypes() {
		try {
			Map<String, Object> testData = new HashMap<String, Object>();
			for (String salesforceType : salesforceTypes) {
				testData.put("type", salesforceType);
				initializeTestRunMessage(testData);
				DescribeSObjectResult describeSObjectResult = runFlowAndGetPayload("describe-sobject");
				String sObjectName = describeSObjectResult.getName();

				assertTrue(sObjectName.equals(getTestRunMessageValue("type")));
			}
			
		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
		}

	}

}