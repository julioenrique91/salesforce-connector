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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.metadata.DescribeMetadataResult;
import com.sforce.soap.metadata.SaveResult;

public class UpdateMetadataTestCases extends SalesforceTestParent {

    // Used to delete inserted objects based on their full name.
    private List<String> toDelete = new ArrayList<String>();
    private String orgNamespace;

    @Before
    public void setUp() throws Exception {
        initializeTestRunMessage("updateMetadataTestData");
		runFlowAndGetPayload("create-metadata");

		DescribeMetadataResult result = runFlowAndGetPayload("describe-metadata");
		orgNamespace = result.getOrganizationNamespace();
    }

    @Category({ RegressionTests.class })
    @Test
    public void testUpdateMetadata() throws Exception {
        try {
            List<Map<String, Object>> objects = new ArrayList<Map<String, Object>>();
            Map<String, Object> updatedObject = (Map<String, Object>) getTestRunMessageValue("updatedObject");
            objects.add(updatedObject);
            upsertOnTestRunMessage("objects", objects);

            List<SaveResult> results = runFlowAndGetPayload("update-metadata");

            for (SaveResult result : results) {
                assertTrue(result.isSuccess());
				toDelete.add(orgNamespace + "__" + result.getFullName());
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