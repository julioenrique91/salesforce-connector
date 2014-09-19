/**
 * Mule Salesforce Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.salesforce.metadata.type;

import org.apache.commons.lang.StringUtils;

/**
 * @author Mulesoft, Inc
 */
public enum MetadataType {
    CUSTOM_OBJECT("com.sforce.soap.metadata", com.sforce.soap.metadata.CustomObject.class, "Custom Object"),
    CUSTOM_FIELD("com.sforce.soap.metadata", com.sforce.soap.metadata.CustomField.class, "Custom Field"),
    EXTERNAL_DATA_SOURCE("com.sforce.soap.metadata", com.sforce.soap.metadata.ExternalDataSource.class, "External Data Source");

    String entityPackage;
    Class metadataEntityClass;
    String displayName;

    MetadataType(String entityPackage, Class metadataEntityClass, String displayName) {
        this.entityPackage = entityPackage;
        this.metadataEntityClass = metadataEntityClass;
        this.displayName = displayName;

    }

    public String getEntityPackage() {
        return this.entityPackage;
    }

    public Class getMetadataEntityClass() {
        return this.metadataEntityClass;
    }

    public String getServiceNowEndpoint() {
        return String.format("/%s.do?SOAP", StringUtils.lowerCase(this.toString()));
    }

    public String getDisplayName() {
        return  displayName;
    }
}
