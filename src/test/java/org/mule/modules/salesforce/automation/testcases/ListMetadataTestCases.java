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
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.metadata.DescribeMetadataResult;
import com.sforce.soap.metadata.FileProperties;

public class ListMetadataTestCases extends SalesforceTestParent {

	// Used to delete inserted objects based on their full name.
	private List<String> toDelete = new ArrayList<String>();
	private String orgNamespace;

	@Before
	public void setUp() throws Exception {
		initializeTestRunMessage("createRemoteSiteSettingMetadataTestData");
		runFlowAndGetPayload("create-metadata");

		DescribeMetadataResult result = runFlowAndGetPayload("describe-metadata");
		orgNamespace = result.getOrganizationNamespace();
	}

	@Category({ RegressionTests.class })
	@Test
	public void testListMetadata() {
		try {
			List<Map<String, Object>> objects = getTestRunMessageValue("objects");
			List<FileProperties> metadata = runFlowAndGetPayload("list-metadata");

			assertTrue(metadata.size() >= objects.size());

			for (Map<String, Object> object : objects) {
				String objectFullName = null;
				if (orgNamespace != null && !orgNamespace.isEmpty()) {
					objectFullName = orgNamespace + "__" + (String) object.get("fullName");
				} 
				else {
					objectFullName = (String) object.get("fullName");
				}
				String propsFullName;
				boolean exists = false;

				for (FileProperties props : metadata) {
					if (props.getNamespacePrefix() != null && !props.getNamespacePrefix().isEmpty()) {
						propsFullName = props.getNamespacePrefix() + "__" + props.getFullName();
					}
					else {
						propsFullName = props.getFullName();
					}
					if (objectFullName.equals(propsFullName)) {
						exists = true;
						toDelete.add(propsFullName);
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
		upsertOnTestRunMessage("fullNames", toDelete);
		runFlowAndGetPayload("delete-metadata");
	}
}