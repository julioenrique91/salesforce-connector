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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.mule.modules.salesforce.connection.CustomPartnerConnection;

/**
 * Adapter for Salesforce Partner Connection
 *
 * @author Mulesoft, Inc
 */
public class SalesforceSoapAdapter {
    private static final Logger logger = Logger.getLogger(SalesforceSoapAdapter.class);

    private SalesforceSoapAdapter() {

    }

    public static CustomPartnerConnection adapt(
            final CustomPartnerConnection facade, final Map<SalesforceHeader, Object> headers) {

        return (CustomPartnerConnection) Enhancer.create(
        		CustomPartnerConnection.class,
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method,
                                         Object[] args) throws Exception {
                        if (logger.isDebugEnabled()) {
                        	logger.debug(String.format(
                                    "Invoked method %s with arguments %s",
                                    method.getName(), Arrays.toString(args)));
                        }
                        try {
                        	CustomPartnerConnection connection = addHeaders(facade, headers);
                            Object ret = method.invoke(connection, args);
                            if (logger.isDebugEnabled()) {
                            	logger.debug(String.format(
                                        "Returned method %s with value %s",
                                        ret, Arrays.toString(args)));
                            }

                            return ret;
                        } catch (Exception e) {
                            if (logger.isDebugEnabled()) {
                            	logger.debug("Method " + method.getName() + " thew " + e.getClass());
                            }

                            throw SalesforceExceptionHandlerAdapter.analyzeSoapException(e);
                        }

                    }

                }
        );
    }

    private static CustomPartnerConnection addHeaders(CustomPartnerConnection partnerConnection, Map<SalesforceHeader, Object> headers) {
        clearHeaders(partnerConnection);
        if (headers != null) {
            for (Map.Entry<SalesforceHeader, Object> entry : headers.entrySet()) {
                if (!Map.class.isAssignableFrom(entry.getValue().getClass())) {
                	logger.error(String.format("The header %s should be a Map", entry.getKey().getHeaderName()));
                    continue;
                }
                try {
                    Object headerObject = entry.getKey().getHeaderClass().newInstance();
                    BeanUtils.populate(headerObject, (Map) entry.getValue());
                    partnerConnection.getClass().getMethod("__set" + entry.getKey().getHeaderName(), entry.getKey().getHeaderClass()).
                            invoke(partnerConnection, headerObject);
                } catch (Exception e) { //NOSONAR ReflectiveOperationException is not present in JDK 6
                	logger.error(String.format("Header %s is incorrect, couldn't be added to the request", entry.getKey().toString()), e);
                }
            }
        }
        return partnerConnection;
    }

    private static void clearHeaders(CustomPartnerConnection partnerConnection) {
    	if (partnerConnection.getConnection() != null) {
	        partnerConnection.getConnection().clearAllOrNoneHeader();
	        partnerConnection.getConnection().clearAllowFieldTruncationHeader();
	        partnerConnection.getConnection().clearAssignmentRuleHeader();
	        partnerConnection.getConnection().clearCallOptions();
	        partnerConnection.getConnection().clearDisableFeedTrackingHeader();
	        partnerConnection.getConnection().clearEmailHeader();
	        partnerConnection.getConnection().clearLocaleOptions();
	        partnerConnection.getConnection().clearMruHeader();
	        partnerConnection.getConnection().clearOwnerChangeOptions();
	        partnerConnection.getConnection().clearQueryOptions();
	        partnerConnection.getConnection().clearUserTerritoryDeleteHeader();
    	}
    }
}
