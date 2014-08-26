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

import com.sforce.soap.partner.PartnerConnection;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * Adapter for Salesforce Partner Connection
 *
 * @author Mulesoft, Inc
 */
public class SalesforceSoapAdapter {
    private static final Logger LOGGER = Logger.getLogger(SalesforceSoapAdapter.class);

    private SalesforceSoapAdapter() {

    }

    public static PartnerConnection adapt(
            final PartnerConnection facade, final Map<SalesforceHeader, Object> headers) {

        return (PartnerConnection) Enhancer.create(
                PartnerConnection.class,
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method,
                                         Object[] args) throws Exception {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug(String.format(
                                    "Invoked method %s with arguments %s",
                                    method.getName(), Arrays.toString(args)));
                        }
                        try {
                            PartnerConnection connection = addHeaders(facade, headers);
                            Object ret = method.invoke(connection, args);
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug(String.format(
                                        "Returned method %s with value %s",
                                        ret, Arrays.toString(args)));
                            }

                            return ret;
                        } catch (Exception e) {
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("Method " + method.getName() + " thew " + e.getClass());
                            }

                            throw SalesforceExceptionHandlerAdapter.analyzeSoapException(e);
                        }

                    }

                }
        );
    }

    private static PartnerConnection addHeaders(PartnerConnection partnerConnection, Map<SalesforceHeader, Object> headers) {
        clearHeaders(partnerConnection);
        if (headers != null) {
            for (Map.Entry<SalesforceHeader, Object> entry : headers.entrySet()) {
                if (!Map.class.isAssignableFrom(entry.getValue().getClass())) {
                    LOGGER.error(String.format("The header %s should be a Map", entry.getKey().getHeaderName()));
                    continue;
                }
                try {
                    Object headerObject = entry.getKey().getHeaderClass().newInstance();
                    BeanUtils.populate(headerObject, (Map) entry.getValue());
                    partnerConnection.getClass().getMethod("__set" + entry.getKey().getHeaderName(), entry.getKey().getHeaderClass()).
                            invoke(partnerConnection, headerObject);
                } catch (Exception e) {
                    LOGGER.error(String.format("Header %s is incorrect, couldn't be added to the request", entry.getKey().toString()));
                }
            }
        }
        return partnerConnection;
    }

    private static void clearHeaders(PartnerConnection partnerConnection) {
        partnerConnection.clearAllOrNoneHeader();
        partnerConnection.clearAllowFieldTruncationHeader();
        partnerConnection.clearAssignmentRuleHeader();
        partnerConnection.clearCallOptions();
        partnerConnection.clearDisableFeedTrackingHeader();
        partnerConnection.clearEmailHeader();
        partnerConnection.clearLocaleOptions();
        partnerConnection.clearMruHeader();
        partnerConnection.clearOwnerChangeOptions();
        partnerConnection.clearQueryOptions();
        partnerConnection.clearUserTerritoryDeleteHeader();
    }
}
