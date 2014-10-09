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

import com.sforce.soap.metadata.DescribeMetadataResult;
import com.sforce.soap.metadata.UpsertResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UpsertMetadataTestCases extends SalesforceTestParent {

	// Used to delete inserted objects based on their full name.
	private List<String> toDelete = new ArrayList<String>();
	private String orgNamespace;

    @Before
    public void setUp() throws Exception {
        initializeTestRunMessage("upsertMetadataTestData");

        DescribeMetadataResult result = runFlowAndGetPayload("describe-metadata");
        orgNamespace = result.getOrganizationNamespace();
    }

    @Category({ RegressionTests.class })
    @Test
    public void testUpsertMetadata() {
        try {
			// creating a new object
            List<Map<String, Object>> objects = new ArrayList<Map<String, Object>>();
			Map<String, Object> before = getTestRunMessageValue("before");
			String fullName = orgNamespace + "__" + before.get("fullName");
			before.put("fullName", fullName);
            objects.add(before);
            upsertOnTestRunMessage("objects", objects);

            List<UpsertResult> resultsForCreate = runFlowAndGetPayload("upsert-metadata");
            for (UpsertResult result : resultsForCreate) {
                assertTrue(result.getSuccess());
                assertTrue(result.isCreated());
            }

			// upserting the above object
            objects = new ArrayList<Map<String, Object>>();
            Map<String, Object> after = getTestRunMessageValue("after");
            fullName = orgNamespace + "__" + after.get("fullName");
            after.put("fullName", fullName);
            objects.add(after);
            upsertOnTestRunMessage("objects", objects);

            List<UpsertResult> resultsForUpdate = runFlowAndGetPayload("upsert-metadata");
            for (UpsertResult result : resultsForUpdate) {
                assertTrue(result.getSuccess());
                assertFalse(result.isCreated());
                toDelete.add(result.getFullName());
            }
        }
        catch (Exception e) {
            fail(ConnectorTestUtils.getStackTrace(e));
        }
    }

    @After
    public void tearDown() throws Exception {
        upsertOnTestRunMessage("fullNames", toDelete);
        runFlowAndGetPayload("delete-metadata");
    }
}