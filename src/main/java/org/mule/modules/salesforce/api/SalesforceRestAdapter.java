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

import com.sforce.async.BulkConnection;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Adapter for Salesforce Rest Partner Connection
 *
 * @author Mulesoft, Inc
 */
public class SalesforceRestAdapter {
    private static Logger logger = Logger.getLogger(SalesforceRestAdapter.class);

    public static BulkConnection adapt(final BulkConnection facade) {

        return (BulkConnection) Enhancer.create(
                BulkConnection.class,
            new InvocationHandler() {
                public Object invoke(Object proxy, Method method,
                                     Object[] args) throws Throwable {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format(
                                "Invoked method %s with arguments %s",
                                method.getName(), Arrays.toString(args)));
                    }
                    try {
                        Object ret = method.invoke(facade, args);
                        if (logger.isDebugEnabled()) {
                            logger.debug(String.format(
                                    "Returned method %s with value %s",
                                    ret, Arrays.toString(args)));
                        }

                        return ret;
                    } catch (Exception e) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Method " + method.getName() + " threw " + e.getClass());
                        }

                        throw SalesforceExceptionHandlerAdapter.analyzeRestException(e);
                    }

                }

            });
    }
}
