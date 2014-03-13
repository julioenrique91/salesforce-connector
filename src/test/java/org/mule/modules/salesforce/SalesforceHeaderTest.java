/**
 * Mule Salesforce Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.salesforce;

import com.sforce.soap.partner.PartnerConnection;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.salesforce.api.SalesforceHeader;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SalesforceHeaderTest {

    private SalesforceConnector salesforceConnector = new SalesforceConnector();

    @Before
    public void setUpTests() {
        salesforceConnector.setConnection(new PartnerConnection());
    }

    @Test
    public void testAddAllOrNoneHeader() {
        Map<SalesforceHeader, Object> headers = new HashMap<SalesforceHeader, Object>();
        Map<String, Object> allOrNoneHeader = new HashMap<String, Object>();
        allOrNoneHeader.put("allOrNone", true);

        headers.put(SalesforceHeader.ALL_OR_NONE, allOrNoneHeader);

        PartnerConnection partnerConnection = salesforceConnector.getSalesforceSoapAdapter(headers);
        assertTrue(partnerConnection.getAllOrNoneHeader().isAllOrNone());
    }

    @Test
    public void testAddEmailHeader() {
        Map<SalesforceHeader, Object> headers = new HashMap<SalesforceHeader, Object>();
        Map<String, Object> emailHeader = new HashMap<String, Object>();
        emailHeader.put("triggerAutoResponseEmail", true);
        emailHeader.put("triggerOtherEmail", true);
        emailHeader.put("triggerUserEmail", false);

        headers.put(SalesforceHeader.EMAIL, emailHeader);

        PartnerConnection partnerConnection = salesforceConnector.getSalesforceSoapAdapter(headers);
        assertTrue(partnerConnection.getEmailHeader().isTriggerAutoResponseEmail());
        assertTrue(partnerConnection.getEmailHeader().isTriggerOtherEmail());
        assertFalse(partnerConnection.getEmailHeader().isTriggerUserEmail());
    }

    @Test
    public void testNoHeaders() {
        PartnerConnection partnerConnection = salesforceConnector.getSalesforceSoapAdapter(
                new HashMap<SalesforceHeader, Object>());
        assertNull(partnerConnection.getAllOrNoneHeader());
    }

    @Test
    public void testAddWrongHeader() {
        Map<SalesforceHeader, Object> headers = new HashMap<SalesforceHeader, Object>();
        Map<String, Object> allOrNoneHeader = new HashMap<String, Object>();
        allOrNoneHeader.put("allOrNoneWrongField", true);

        headers.put(SalesforceHeader.ALL_OR_NONE, allOrNoneHeader);

        PartnerConnection partnerConnection = salesforceConnector.getSalesforceSoapAdapter(headers);
        assertFalse(partnerConnection.getAllOrNoneHeader().isAllOrNone());
    }

    @Test
    public void testAddNoMapHeader() {
        Map<SalesforceHeader, Object> headers = new HashMap<SalesforceHeader, Object>();

        headers.put(SalesforceHeader.ALL_OR_NONE, "allOrNone");

        PartnerConnection partnerConnection = salesforceConnector.getSalesforceSoapAdapter(headers);
        assertNull(partnerConnection.getAllOrNoneHeader());
    }

    @Test
    public void testAddNullHeaders() {
        PartnerConnection partnerConnection = salesforceConnector.getSalesforceSoapAdapter(null);
        assertNull(partnerConnection.getAllOrNoneHeader());
    }

    @Test
    public void testAddEmptyMapHeaders() {
        PartnerConnection partnerConnection = salesforceConnector.getSalesforceSoapAdapter();
        assertNull(partnerConnection.getAllOrNoneHeader());
    }

    @Test
    public void testCleanHeaders() {
        Map<SalesforceHeader, Object> headers = new HashMap<SalesforceHeader, Object>();
        Map<String, Object> allOrNoneHeader = new HashMap<String, Object>();
        allOrNoneHeader.put("allOrNone", true);

        headers.put(SalesforceHeader.ALL_OR_NONE, allOrNoneHeader);

        PartnerConnection partnerConnection = salesforceConnector.getSalesforceSoapAdapter(headers);
        assertTrue(partnerConnection.getAllOrNoneHeader().isAllOrNone());

        headers = new HashMap<SalesforceHeader, Object>();
        Map<String, Object> localeOptions = new HashMap<String, Object>();
        localeOptions.put("language", "en_GB");

        headers.put(SalesforceHeader.LOCALE_OPTIONS, localeOptions);

        partnerConnection = salesforceConnector.getSalesforceSoapAdapter(headers);
        assertEquals("en_GB", partnerConnection.getLocaleOptions().getLanguage());
        assertNull(partnerConnection.getAllOrNoneHeader());
    }
}