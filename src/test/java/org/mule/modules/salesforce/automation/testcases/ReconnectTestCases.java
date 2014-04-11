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

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.salesforce.automation.RegressionTests;
import org.mule.modules.salesforce.automation.SalesforceTestParent;

import java.util.ArrayList;
import java.util.List;

public class ReconnectTestCases extends SalesforceTestParent {
	
	@Before
	public void setUp() throws Exception {

	}

    @Category({RegressionTests.class})
    @Test
    public void getUserInfoLoadTest() throws Exception {
        List<Thread> ths = new ArrayList<Thread>();
        for (int i = 0; i < 100; i++) {
            Thread th = new Thread( new Runnable() {
                @Override
                public void run() {
                    try {
                        runFlowAndGetPayload("get-user-info");
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }

                }
            });
            th.start();
            ths.add(th);
        }

        for (Thread th: ths){
            th.join();
        }
    }

}