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
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.salesforce.automation.SmokeTests;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.metadata.DescribeMetadataResult;
import com.sforce.soap.metadata.SaveResult;

public class CreateMetadataTestCases extends SalesforceTestParent {

	// Used to delete inserted objects based on their full name.
	private List<String> toDelete = new ArrayList<String>();
	private String orgNamespace;

    @Category({SmokeTests.class, RegressionTests.class})
    @Test
    public void testCreateRemoteSiteSettingMetadata() {
        try {
            initializeTestRunMessage("createRemoteSiteSettingMetadataTestData");

            List<SaveResult> results = runFlowAndGetPayload("create-metadata");
            for (SaveResult result : results) {
                assertTrue(result.isSuccess());
            }
        }
        catch (Exception e) {
            fail(ConnectorTestUtils.getStackTrace(e));
        }
    }
    
    @Category({SmokeTests.class, RegressionTests.class})
    @Test
    public void testCreateCustomObjectsMetadata() {
        try {
            initializeTestRunMessage("createCustomObjectMetadataTestData");
            
            List<SaveResult> results = runFlowAndGetPayload("create-metadata");
            for (SaveResult result : results) {
                assertTrue(result.isSuccess());
            }
        }
        catch (Exception e) {
            fail(ConnectorTestUtils.getStackTrace(e));
        }
    }
    
    @After
    public void tearDown() throws Exception {
    	
        DescribeMetadataResult result = runFlowAndGetPayload("describe-metadata");
        orgNamespace = result.getOrganizationNamespace();

        // Get fullnames that were used to create metadata objects
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

        upsertOnTestRunMessage("fullNames", toDelete);
        runFlowAndGetPayload("delete-metadata");
    }
}