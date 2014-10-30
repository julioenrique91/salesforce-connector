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

import com.sforce.soap.metadata.DeleteResult;
import com.sforce.soap.metadata.DescribeMetadataResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.salesforce.automation.SmokeTests;
import org.mule.modules.tests.ConnectorTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DeleteMetadataTestCases extends SalesforceTestParent {

    private List<String> toDelete = new ArrayList<String>();
	private String orgNamespace;

    @Before
    public void setUp() throws Exception {
        initializeTestRunMessage("createRemoteSiteSettingMetadataTestData");
        runFlowAndGetPayload("create-metadata");

		DescribeMetadataResult result = runFlowAndGetPayload("describe-metadata");
		orgNamespace = result.getOrganizationNamespace();

		// Get fullnames that were used to create test objects
		List<Map<String, Object>> metadataObjects = getTestRunMessageValue("objects");
		for (Map<String, Object> metadataObject : metadataObjects) {
			String fullName = (String) metadataObject.get("fullName");
			 if (orgNamespace != null && !orgNamespace.isEmpty()) {
				 toDelete.add(orgNamespace + "__" + fullName);
			 }
			 else {
				 toDelete.add(fullName);
			 }
		}
    }

    @Category({SmokeTests.class, RegressionTests.class})
    @Test
    public void testDeleteMetadata() {
        try {
            upsertOnTestRunMessage("fullNames", toDelete);
            List<DeleteResult> deleteResults = runFlowAndGetPayload("delete-metadata");
            for (DeleteResult deleteResult : deleteResults) {
                assertTrue(deleteResult.isSuccess());
            }
        }
        catch (Exception e) {
            fail(ConnectorTestUtils.getStackTrace(e));
        }
    }
}