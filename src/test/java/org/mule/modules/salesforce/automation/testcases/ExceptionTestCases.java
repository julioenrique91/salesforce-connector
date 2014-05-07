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

import com.sforce.ws.SoapFaultException;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.SmokeTestSuite;
import org.mule.modules.salesforce.exception.SalesforceSessionExpiredException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

@SuppressWarnings("unchecked")
public class ExceptionTestCases extends SalesforceTestParent {

	@Category({SmokeTestSuite.class, RegressionTests.class})
    @Test
	public void testExceptionWrongSessionId() throws Exception {
        try {
            testObjects = (HashMap<String,Object>) context.getBean("queryTestData");
            testObjects.put("query", "SELECT Id, Name FROM Account");
            flow = lookupFlowConstruct("query-wrong-config");
            flow.process(getTestEvent(testObjects));
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof SalesforceSessionExpiredException);
        }

	}

    //TODO: add tests
    @Category({SmokeTestSuite.class, RegressionTests.class})
    @Test
    public void testExceptionWrongSessionIdRestAPI() throws Exception {
        try {
            testObjects = (HashMap<String,Object>) context.getBean("queryTestData");
            testObjects.put("query", "SELECT Id, Name FROM Account");
            flow = lookupFlowConstruct("query-wrong-config");
            flow.process(getTestEvent(testObjects));
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof SalesforceSessionExpiredException);
        }

    }

    @Category({SmokeTestSuite.class, RegressionTests.class})
    @Test
    public void testExceptionBadObjectCreate() throws Exception {

        try {
            testObjects = (HashMap<String,Object>) context.getBean("createSingleRecord");
            ((Map) testObjects.get("sObjectFieldMappingsFromMessage")).put("Wrong Field", "Value");
            flow = lookupFlowConstruct("create-single-from-message");
            response = flow.process(getTestEvent(testObjects));
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof RuntimeException);
            assertTrue(e.getCause().getCause() instanceof SoapFaultException);
        }
    }
}