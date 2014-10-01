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

    private String orgNamespace;
    private List<String> toDelete = new ArrayList<String>();

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
            List objects = new ArrayList();
            objects.add(getTestRunMessageValue("before"));
            upsertOnTestRunMessage("objects", objects);

            List<UpsertResult> resultsForCreate = runFlowAndGetPayload("upsert-metadata");
            for (UpsertResult result : resultsForCreate) {
                assertTrue(result.getSuccess());
                assertTrue(result.isCreated());
            }

            objects = new ArrayList();
            Map<String, Object> after = getTestRunMessageValue("after");
            String fullName = orgNamespace + "__" + after.get("fullName");
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
