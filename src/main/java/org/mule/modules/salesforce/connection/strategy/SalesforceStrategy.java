/**
 * Mule Salesforce Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.salesforce.connection.strategy;


import com.sforce.async.BulkConnection;
import com.sforce.soap.partner.AssignmentRuleHeader_element;
import com.sforce.soap.partner.CallOptions_element;
import org.apache.log4j.Logger;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.api.callback.SourceCallback;
import org.mule.modules.salesforce.SalesforceBayeuxClient;
import org.mule.modules.salesforce.SalesforceBayeuxMessageListener;
import org.mule.modules.salesforce.connection.CustomMetadataConnection;
import org.mule.modules.salesforce.connection.CustomPartnerConnection;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public abstract class SalesforceStrategy {
    private static final Logger logger = Logger.getLogger(SalesforceStrategy.class);
    /**
     * Bayeux client
     */
    private SalesforceBayeuxClient bc;

    private final List<Subscription> subscriptions = new ArrayList<Subscription>();

    /**
     *
     *
     *
     * CONFIGURABLES
     *
     *
     *
     */


    /**
     * Client ID for partners
     */
    @Configurable
    @Optional
    private String clientId;

    /**
     * The ID of a specific assignment rule to run for the Case or Lead. The assignment rule can be active or inactive. The ID can be retrieved by querying the AssignmentRule object. If specified, do not specify useDefaultRule. This element is ignored for accounts, because all territory assignment rules are applied.
     * <p/>
     * If the value is not in correct ID format (15-character or 18-character Salesforce ID), the call fails and a MALFORMED_ID exception is returned.
     */
    @Configurable
    @Optional
    private String assignmentRuleId;

    /**
     * If true for a Case or Lead, uses the default (active) assignment rule for a Case or Lead. If specified, do not specify an assignmentRuleId. If true for an Account, all territory assignment rules are applied, and if false, no territory assignment rules are applied.
     */
    @Configurable
    @Optional
    private Boolean useDefaultRule;

    /**
     * If true, truncate field values that are too long, which is the behavior in API versions 14.0 and earlier.
     * <p/>
     * Default is false: no change in behavior. If a string or textarea value is too large, the operation fails and the fault code STRING_TOO_LONG is returned.
     */
    @Configurable
    @Optional
    private Boolean allowFieldTruncationSupport;

    /**
     * Salesforce API version
     */
    @Configurable
    @Default("32.0")
    private Double apiVersion;


    /**
     *
     *
     *
     *
     * DEFINE BEHAVIOUR FOR SUBCLASSES STRATEGIES
     *
     *
     *
     *
     *
     */


    public abstract CustomPartnerConnection getCustomPartnerConnection();

    public abstract BulkConnection getBulkConnection();

    public abstract CustomMetadataConnection getCustomMetadataConnection();

    public abstract String getSessionId();

    public abstract boolean isReadyToSubscribe();


    /**
     *
     *
     *
     *
     * HELPER METHODS
     *
     *
     *
     *
     *
     */


    public SalesforceBayeuxClient getBayeuxClient() {
        try {
            if (bc == null
                    && getCustomPartnerConnection() != null
                    && getCustomPartnerConnection().getConnection() != null
                    && getCustomPartnerConnection().getConnection().getConfig() != null) {
                bc = new SalesforceBayeuxClient(this);

                if (!bc.isHandshook()) {
                    bc.handshake();
                }
            }
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        }

        return bc;
    }

    protected void setConnectionOptions(CustomPartnerConnection connection) {
        //call options
        String clientId = getClientId();
        if (clientId != null) {
            CallOptions_element callOptions = new CallOptions_element();
            callOptions.setClient(clientId);
            connection.__setCallOptions(callOptions);
        }

        //assignment rule
        String assignmentRuleId = getAssignmentRuleId();
        Boolean useDefaultRule = getUseDefaultRule();
        if (assignmentRuleId != null || useDefaultRule != null) {
            AssignmentRuleHeader_element assignmentRule = new AssignmentRuleHeader_element();
            if (assignmentRuleId != null) {
                assignmentRule.setAssignmentRuleId(assignmentRuleId);
            }
            if (useDefaultRule != null) {
                assignmentRule.setUseDefaultRule(useDefaultRule);
            }
            connection.__setAssignmentRuleHeader(assignmentRule);
        }

        //allow field truncation
        Boolean allowFieldTruncationSupport = getAllowFieldTruncationSupport();
        if (allowFieldTruncationSupport != null) {
            connection.setAllowFieldTruncationHeader(allowFieldTruncationSupport);
        }
    }

    protected void processSubscriptions() {
        boolean resubscribe = false;

        if (this.bc == null) {
            resubscribe = true;
        }

        for (Subscription p : subscriptions) {
            if (resubscribe || !p.isSubscribed()) {
                this.subscribe(p.getTopic(), p.getCallback());
            }
        }
    }

    public void subscribe(String topicName, SourceCallback callback) {
        this.getBayeuxClient().subscribe(topicName, new SalesforceBayeuxMessageListener(callback));
    }


    /**
     *
     *
     *
     *
     * GETTERS AND SETTERS
     *
     *
     *
     */

    protected boolean isInitializedBayeuxClient() {
        return this.bc != null;
    }

    public void setBayeuxClient(SalesforceBayeuxClient bc) {
        this.bc = bc;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAssignmentRuleId() {
        return assignmentRuleId;
    }

    public void setAssignmentRuleId(String assignmentRuleId) {
        this.assignmentRuleId = assignmentRuleId;
    }

    public Boolean getUseDefaultRule() {
        return useDefaultRule;
    }

    public void setUseDefaultRule(Boolean useDefaultRule) {
        this.useDefaultRule = useDefaultRule;
    }

    public Boolean getAllowFieldTruncationSupport() {
        return allowFieldTruncationSupport;
    }

    public void setAllowFieldTruncationSupport(Boolean allowFieldTruncationSupport) {
        this.allowFieldTruncationSupport = allowFieldTruncationSupport;
    }

    /**
     * @return the apiVersion
     */
    public Double getApiVersion() {
        return apiVersion;
    }

    /**
     * @param apiVersion the apiVersion to set
     */
    public void setApiVersion(Double apiVersion) {
        this.apiVersion = apiVersion;
    }


    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }
}
