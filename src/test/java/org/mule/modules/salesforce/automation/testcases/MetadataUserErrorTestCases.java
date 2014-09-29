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

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mule.common.metadata.MetaDataKey;
import org.mule.modules.salesforce.SalesforceConnector;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;

import com.sforce.soap.partner.DescribeSObjectResult;



public class MetadataUserErrorTestCases extends SalesforceTestParent {
	
	SalesforceConnector connector;
	
	@Before
	public void setUp() throws Exception {

		connector = new SalesforceConnector();
		connector.connect("nicolas.mondada@33demo.com", "newKey123", "KawFDaMBPdIWp5zfjnQlRxhv", "https://login.salesforce.com/services/Soap/u/31.0", null, 0, null, null, null, null);
	}
	
	@Test
	public void testMetadata() {

		try {
			
			List<MetaDataKey> metadataKeys = connector.getMetaDataKeys();
			MetaDataKey userKey = null;
			for (MetaDataKey key : metadataKeys){
				if (key.getDisplayName().equalsIgnoreCase("User")){
					userKey = key;
				}
			}
//			connector.getMetaData(userKey);
			DescribeSObjectResult[] describeSObject = connector.getSalesforceSoapAdapter().describeSObjects(new String[] {userKey.getId()});
			System.out.println(describeSObject);

		} catch (Exception e) {
			fail(ConnectorTestUtils.getStackTrace(e));
			System.out.println(e.getStackTrace());
		}
		
	}

}