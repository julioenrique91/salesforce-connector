/**
 * Mule Salesforce Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.salesforce.api;

/**
 * @author Mulesoft, Inc
 */
public enum SalesforceHeader {

    ALL_OR_NONE("AllOrNoneHeader", com.sforce.soap.partner.AllOrNoneHeader_element.class),
    ALLOW_FIELD_TRUNCATION("AllowFieldTruncationHeader", com.sforce.soap.partner.AllowFieldTruncationHeader_element.class),
    ASSIGNMENT_RULE("AssignmentRuleHeader", com.sforce.soap.partner.AssignmentRuleHeader_element.class),
    CALL_OPTIONS("CallOptions", com.sforce.soap.partner.CallOptions_element.class),
    DISABLE_FEED_TRACKING("DisableFeedTrackingHeader", com.sforce.soap.partner.DisableFeedTrackingHeader_element.class),
    EMAIL("EmailHeader", com.sforce.soap.partner.EmailHeader_element.class),
    LOCALE_OPTIONS("LocaleOptions", com.sforce.soap.partner.LocaleOptions_element.class),
    MRU("MruHeader", com.sforce.soap.partner.MruHeader_element.class),
    OWNER_CHANGE_OPTIONS("OwnerChangeOptions", com.sforce.soap.partner.OwnerChangeOptions_element.class),
    QUERY_OPTIONS("QueryOptions", com.sforce.soap.partner.QueryOptions_element.class),
    USER_TERRITORY_DELETE("UserTerritoryDeleteHeader", com.sforce.soap.partner.UserTerritoryDeleteHeader_element.class);

    String headerName;
    Class headerClass;

    SalesforceHeader(String headerName, Class clazz) {
        this.headerName = headerName;
        this.headerClass = clazz;
    }

    public String getHeaderName() {
        return this.headerName;
    }

    public Class getHeaderClass() {
        return this.headerClass;
    }
}
