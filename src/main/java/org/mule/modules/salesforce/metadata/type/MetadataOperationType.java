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
public enum MetadataOperationType {
    CREATE("Create"),
    DELETE("Delete");

    String name;

    MetadataOperationType(String name) {
        this.name = name;
    }

    public String getOperationName() {
        return this.name;
    }

    public String getOperationNameLowerCase() {
        return StringUtils.uncapitalize(this.name);
    }
}
