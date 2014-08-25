/**
 * Mule Salesforce Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.salesforce.exception;

import com.sforce.soap.partner.Error;

import java.util.Arrays;
import java.util.List;

public class SalesforceBulkException extends Exception {

    private static final long serialVersionUID = -7180832542515849354L;

    private final List<Error> errors;

    public SalesforceBulkException(Error error) {
        this.errors = Arrays.asList(error);
    }

    public SalesforceBulkException(Error[] error) {
        this.errors = Arrays.asList(error);
    }

    public List<Error> getErrors() {
        return errors;
    }

}
