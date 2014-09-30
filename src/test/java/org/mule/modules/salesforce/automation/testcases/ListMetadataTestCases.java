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

import com.sforce.soap.metadata.FileProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;
import org.junit.After;
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

public class ListMetadataTestCases extends SalesforceTestParent {

    // Used to delete inserted objects based on their full name.
    private List<String> fullNames = new ArrayList<String>();

    @Before
    public void setUp() throws Exception {
        initializeTestRunMessage("listMetadataTestData");
        runFlowAndGetPayload("create-metadata");
    }

    @Category({ RegressionTests.class })
    @Test
    public void testListMetadata() {
        try {
            List<Map<String, Object>> objects = getTestRunMessageValue("objects");
            List<FileProperties> metadata = runFlowAndGetPayload("list-metadata");

            assertTrue(metadata.size() >= objects.size());
            for (Map<String, Object> object : objects) {

                String fullName = (String) object.get("fullName");

                boolean exists = false;
                for (FileProperties props : metadata) {
                    if (fullName.equals(props.getFullName())) {
                        exists = true;
                        fullNames.add(props.getNamespacePrefix() + "__" + props.getFullName());
                        break;
                    }
                }
                assertTrue(exists);
            }
        }
        catch (Exception e) {
            fail(ConnectorTestUtils.getStackTrace(e));
        }
    }

    @After
    public void tearDown() throws Exception {
        upsertOnTestRunMessage("fullNames", fullNames);
        runFlowAndGetPayload("delete-metadata");
    }

}
