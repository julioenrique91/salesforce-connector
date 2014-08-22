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

import com.sforce.async.AsyncApiException;
import com.sforce.async.AsyncExceptionCode;
import com.sforce.soap.partner.fault.UnexpectedErrorFault;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mule.modules.salesforce.exception.SalesforceSessionExpiredException;

/**
 * Centralized handler for Salesforce exceptions, to check if a reconnection exception has to be thrown
 *
 * @author Mulesoft, Inc
 */
public class SalesforceExceptionHandlerAdapter {

    private static Logger logger = Logger.getLogger(SalesforceExceptionHandlerAdapter.class);


    /**
     * Returns a concrete exception to be thrown by analyzing the parametrized one
     *
     * @param e Exception to be analyzed
     * @return {@link org.mule.modules.salesforce.exception.SalesforceSessionExpiredException} if the parametrized
     * exception {@code e} indicates that the session has been invalidated, otherwise will return the same exception it
     * received without any change.
     */
    public static Exception analyzeRestException(Exception e) {
        if (logger.isDebugEnabled()) {
            logger.debug("Analyzing exception " + e.getClass());
        }
        if (e.getCause() instanceof AsyncApiException &&
                (((AsyncApiException) e.getCause()).getExceptionCode() == AsyncExceptionCode.InvalidSessionId)) {
            return new SalesforceSessionExpiredException(e.getCause().getMessage(), e.getCause());
        } else {
            return e;
        }
    }

    /**
     * Returns a concrete exception to be thrown by analyzing the parametrized one
     *
     * @param e Exception to be analyzed
     * @return {@link org.mule.modules.salesforce.exception.SalesforceSessionExpiredException} if the parametrized
     * exception {@code e} indicates that the session has been invalidated, otherwise will return a
     * {@link RuntimeException} with the parametrized exception as cause.
     */
    public static Exception analyzeSoapException(Exception e) {
        if (e.getCause() instanceof UnexpectedErrorFault
                && (!StringUtils.isEmpty(e.getCause().getMessage()) && e.getCause().getMessage().contains("INVALID_SESSION_ID"))
                || (e.getCause().toString() != null && e.getCause().toString().contains("INVALID_SESSION_ID"))) {
            return new SalesforceSessionExpiredException(e.getCause());
        } else {
            //TODO shouldn't this return the same exception it got parametrized? Why would we want to be wrapped in a RuntimeException?
            return new RuntimeException(e.getCause());
        }
    }
}
