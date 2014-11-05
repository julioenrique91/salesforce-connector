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

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.RedirectListener;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.mule.RequestContext;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.lifecycle.Start;
import org.mule.api.annotations.oauth.OAuth2;
import org.mule.api.annotations.oauth.OAuthAccessToken;
import org.mule.api.annotations.oauth.OAuthAuthorizationParameter;
import org.mule.api.annotations.oauth.OAuthCallbackParameter;
import org.mule.api.annotations.oauth.OAuthConsumerKey;
import org.mule.api.annotations.oauth.OAuthConsumerSecret;
import org.mule.api.annotations.oauth.OAuthPostAuthorization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sforce.async.AsyncApiException;
import com.sforce.async.BulkConnection;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.MessageHandler;

/**
 * The Salesforce Connector will allow to connect to the Salesforce application using OAuth as the authentication
 * mechanism. Almost every operation that can be done via the Salesforce's API can be done thru this connector.
 * This connector will also work if your Salesforce objects are customized with additional fields or even you are
 * working with custom objects.
 * <p/>
 * Integrating with Salesforce consists of web service calls utilizing XML request/response setup
 * over an HTTPS connection. The technical details of this connection such as request headers,
 * error handling, HTTPS connection, etc. are all abstracted from the user to make implementation
 * quick and easy.
 * <p/>
 * This version of the connector allows you to use OAuth for authentication instead of the
 * username/password/securityToken combination.
 * <p/>
 * {@sample.config ../../../doc/mule-module-sfdc.xml.sample sfdc:config-with-oauth}
 *
 * @author MuleSoft, Inc.
 */
@org.mule.api.annotations.Connector(name = "sfdc",
        schemaVersion = "5.0",
        friendlyName = "Salesforce (OAuth)",
        minMuleVersion = "3.5",
        configElementName = "config-with-oauth")
@OAuth2(authorizationUrl = "https://login.salesforce.com/services/oauth2/authorize",
        accessTokenUrl = "https://login.salesforce.com/services/oauth2/token",
        authorizationParameters = {
                @OAuthAuthorizationParameter(name = "display", type = SalesforceOAuthDisplay.class,
                        description = "Tailors the login page to the user's device type."),
                @OAuthAuthorizationParameter(name = "immediate", type = SalesforceOAuthImmediate.class,
                        optional = true, defaultValue = "FALSE", description = "Avoid interacting with the user."),
                @OAuthAuthorizationParameter(name = "prompt", type = SalesforceOAuthPrompt.class,
                        optional = true, description = "Specifies how the authorization server prompts the user for reauthentication and reapproval.")
        })
public class SalesforceOAuthConnector extends BaseSalesforceConnector {
    private static final Logger LOGGER = Logger.getLogger(SalesforceOAuthConnector.class);

    /**
     * Connection to the SOAP API
     */
    private PartnerConnection partnerConnection;

    /**
     * REST connection to the bulk API
     */
    private BulkConnection bulkConnection;
    
    /**
     * Connection to the Metadata API
     */
    private MetadataConnection metadataConnection;

    /**
     * Your application's client identifier (consumer key in Remote Access Detail).
     */
    @Configurable
    @OAuthConsumerKey
    private String consumerKey;

    /**
     * Your application's client secret (consumer secret in Remote Access Detail).
     */
    @Configurable
    @OAuthConsumerSecret
    private String consumerSecret;

    @OAuthAccessToken
    private String accessToken;

    @OAuthCallbackParameter(expression = "#[json:instance_url]")
    private String instanceId;

    //REMOVE THIS: this hack needs to be removed once the connector returns the remote user id
    // by itself in a right way after authorize
    @OAuthCallbackParameter(expression = "#[json:id]")
    private String userId;

    @Override
    protected boolean isReadyToSubscribe() {
        return this.accessToken != null;
    }

    @Start
    public void init() {
        this.registerTransformers();
    }

    @OAuthPostAuthorization
    public void postAuthorize() throws ConnectionException, MalformedURLException, AsyncApiException, Exception {
        ConnectorConfig config = new ConnectorConfig();
        if (LOGGER.isDebugEnabled()) {
            config.addMessageHandler(new MessageHandler() {
                @Override
                public void handleRequest(URL endpoint, byte[] request) {
                    LOGGER.debug("Sending request to " + endpoint.toString());
                    LOGGER.debug(new String(request));
                }

                @Override
                public void handleResponse(URL endpoint, byte[] response) {
                    LOGGER.debug("Receiving response from " + endpoint.toString());
                    LOGGER.debug(new String(response));
                }
            });
        }
        config.setSessionId(accessToken);
        config.setManualLogin(true);

        config.setCompression(false);

        String serviceEndpoint = "https://" + (new URL(instanceId)).getHost() + "/services/Soap/u/31.0";
        config.setServiceEndpoint(serviceEndpoint);

        this.partnerConnection = Connector.newConnection(config);
        setConnectionOptions(this.partnerConnection);

        String restEndpoint = "https://" + (new URL(instanceId)).getHost() + "/services/async/31.0";
        config.setRestEndpoint(restEndpoint);

		this.bulkConnection = new BulkConnection(config);
		
		String metadataendpoint = getMetadataServiceEndpoint();
		ConnectorConfig metadataConfig = new ConnectorConfig();
		metadataConfig.setServiceEndpoint(metadataendpoint);
		metadataConfig.setManualLogin(true);
		metadataConfig.setCompression(false);
		metadataConfig.setSessionId(accessToken);
		this.metadataConnection = new MetadataConnection(metadataConfig);
		

		this.processSubscriptions();

        RequestContext.getEvent().setFlowVariable("remoteUserId", userId);
    }
    
    private String getMetadataServiceEndpoint() throws Exception {
        HttpClient client = new HttpClient();
        client.setIdleTimeout(5000);
        client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
        client.registerListener(RedirectListener.class.getName());
        client.start();
        ContentExchange exchange = new ContentExchange();
        exchange.addRequestHeader("Authorization", "Bearer " + accessToken);
        exchange.addRequestHeader("Accept", "application/json");
        exchange.setMethod(HttpMethods.POST);
        exchange.setURL(userId + "?version=32.0");
        client.send(exchange);
        exchange.waitForDone();
        int status = exchange.getResponseStatus();
        if (status == HttpStatus.OK_200) {
         String result = exchange.getResponseContent();
         client.stop();
         
         JsonElement jElement = new JsonParser().parse(result);
         JsonObject jObject = jElement.getAsJsonObject();
         JsonObject urls = jObject.getAsJsonObject("urls");
         String metadataEndpoint = urls.getAsJsonPrimitive("metadata").getAsString();
         
         return metadataEndpoint;
        }
        return "";
       }
    
    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    protected PartnerConnection getConnection() {
        return this.partnerConnection;
    }

    @Override
    protected BulkConnection getBulkConnection() {
        return this.bulkConnection;
    }
    
    @Override
    protected MetadataConnection getMetadataConnection() {
        return this.metadataConnection;
    }

    @Override
    protected String getSessionId() {
        return this.accessToken;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
