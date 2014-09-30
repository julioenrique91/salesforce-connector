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
import com.sforce.soap.metadata.SaveResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class RenameMetadataTestCases extends SalesforceTestParent {

    private String orgNamespace;

    @Before
    public void setUp() throws Exception {
        initializeTestRunMessage("renameMetadataTestData");
        runFlowAndGetPayload("create-metadata");

        DescribeMetadataResult result = runFlowAndGetPayload("describe-metadata");
        orgNamespace = result.getOrganizationNamespace();
    }

    @Category({RegressionTests.class})
    @Test
    public void testRenameMetadata() throws Exception {
        String oldFullName = getTestRunMessageValue("oldFullName");
        String newFullName = getTestRunMessageValue("newFullName");

        upsertOnTestRunMessage("oldFullName", orgNamespace + "__" + oldFullName);
        upsertOnTestRunMessage("newFullName", orgNamespace + "__" + newFullName);

        SaveResult result = runFlowAndGetPayload("rename-metadata");
        assertTrue(result.isSuccess());
    }

    @After
    public void tearDown() throws Exception {
        final String fullName = getTestRunMessageValue("newFullName");

        upsertOnTestRunMessage("fullNames", Arrays.asList(fullName));
        runFlowAndGetPayload("delete-metadata");
    }

}
