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
import java.util.Map;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;
import org.mule.modules.tests.ConnectorTestUtils;
import org.mule.streaming.ConsumerIterator;



public class PublishTopicTestCases extends SalesforceTestParent {

    @Category({RegressionTests.class})
	@Test
	public void testPublishTopicWithDescription() {
    	initializeTestRunMessage("publishTopic");
    	boolean isTrue=true;
    	try {
			runFlowAndGetPayload("publish-topic");
		} catch (Exception e) {
			isTrue=false;
			fail(ConnectorTestUtils.getStackTrace(e));
		}
    	finally{
    		assertTrue(isTrue);
    	}
	}
    
    @Category({RegressionTests.class})
	@Test
	public void testPublishTopicWithNoDescription() {
    	initializeTestRunMessage("publishTopicNoDescription");
    	boolean isTrue=true;
    	try {
			runFlowAndGetPayload("publish-topic");
		} catch (Exception e) {
			isTrue=false;
			fail(ConnectorTestUtils.getStackTrace(e));
		}
    	finally{
    		assertTrue(isTrue);
    	}
	}
    
    @Category({RegressionTests.class})
	@Test
	@Ignore
	public void testPublishExistingTopic() {
    	initializeTestRunMessage("publishExistingTopic");
    	boolean isTrue=true;
    	try {
			runFlowAndGetPayload("publish-topic");
		} catch (Exception e) {
			isTrue=false;
			fail(ConnectorTestUtils.getStackTrace(e));
		}
    	finally{
    		assertTrue(isTrue);
    	}
	}
    
    @Category({RegressionTests.class})
	@Test
	@Ignore
	public void testPublishExistingTopicNoDescription() {
    	initializeTestRunMessage("publishExistingTopicNoDescription");
    	boolean isTrue=true;
    	try {
			runFlowAndGetPayload("publish-topic");
		} catch (Exception e) {
			isTrue=false;
			fail(ConnectorTestUtils.getStackTrace(e));
		}
    	finally{
    		assertTrue(isTrue);
    	}
	}
    
    @Category({RegressionTests.class})
	@Test
	public void testCannotCreateTopic() {
    	initializeTestRunMessage("cannotCreateTopic");
    	boolean isTrue=false;
    	try {
			runFlowAndGetPayload("publish-topic");
		} catch (Exception e) {
			isTrue=true;
		} 
    	finally{
    		assertTrue(isTrue);
    	}
	}
    
    @After
	public void tearDown() throws Exception {
    	String query="SELECT Id FROM PushTopic WHERE Name ="+"'"+(String)getTestRunMessageValue("topicName")+"'";
    	upsertOnTestRunMessage("query", query);
    	ConsumerIterator<Map<String, Object>> iter = runFlowAndGetPayload("query");
    	ArrayList<String >sObjectsIds=new ArrayList<String>();
		while (iter.hasNext()) {
			Map<String, Object> sObject = iter.next();
			sObjectsIds.add(sObject.get("Id").toString());
		}
    	sObjectsIds.add((String)getTestRunMessageValue("topicName"));
    	upsertOnTestRunMessage("idsToDeleteFromMessage", sObjectsIds);
		runFlowAndGetPayload("delete-from-message");
	}

}