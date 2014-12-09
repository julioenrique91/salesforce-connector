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

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.Connect;
import org.mule.api.annotations.ConnectionIdentifier;
import org.mule.api.annotations.Disconnect;
import org.mule.api.annotations.MetaDataScope;
import org.mule.api.annotations.ValidateConnection;
import org.mule.api.annotations.display.Password;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.lifecycle.Start;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.modules.salesforce.connection.CustomMetadataConnection;
import org.mule.modules.salesforce.connection.CustomPartnerConnection;

import com.sforce.async.AsyncApiException;
import com.sforce.async.BulkConnection;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.fault.ApiFault;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.MessageHandler;
import com.sforce.ws.SessionRenewer;


/**
 * The Salesforce Connector will allow to connect to the Salesforce application using regular username and password via
 * the SOAP API. Almost every operation that can be done via the Salesforce's API can be done thru this connector. This
 * connector will also work if your Salesforce objects are customized with additional fields or even you are working
 * with custom objects.
 * <p/>
 * Integrating with Salesforce consists of web service calls utilizing XML request/response setup
 * over an HTTPS connection. The technical details of this connection such as request headers,
 * error handling, HTTPS connection, etc. are all abstracted from the user to make implementation
 * quick and easy.
 * <p/>
 * {@sample.config ../../../doc/mule-module-sfdc.xml.sample sfdc:config}
 *
 * @author MuleSoft, Inc.
 */

@org.mule.api.annotations.Connector(name = "sfdc", schemaVersion = "5.0", friendlyName = "Salesforce", minMuleVersion = "3.5")
@MetaDataScope(SalesforceMetadataManager.class)
public class SalesforceConnector extends BaseSalesforceConnector {
    private static final Logger LOGGER = Logger.getLogger(SalesforceConnector.class);

    /**
     * Partner connection
     */
    private CustomPartnerConnection connection;

    /**
     * REST connection to the bulk API
     */
    private BulkConnection bulkConnection;
    
    /**
     * MetadataConnection connection
     */
    private CustomMetadataConnection metadataConnection;

    /**
     * Login result
     */
    private LoginResult loginResult;

    protected void setConnection(CustomPartnerConnection connection) {
        this.connection = connection;
    }

    protected void setBulkConnection(BulkConnection bulkConnection) {
        this.bulkConnection = bulkConnection;
    }
    
    protected void setCustomMetadataConnection(CustomMetadataConnection metadataConnection) {
        this.metadataConnection = metadataConnection;
    }

    protected void setLoginResult(LoginResult loginResult) {
        this.loginResult = loginResult;
    }

    protected LoginResult getLoginResult() {
        return loginResult;
    }

    @ValidateConnection
    public boolean isConnected() {
        return metadataConnection != null
        		&& bulkConnection != null
                && connection != null
                && loginResult != null
                && loginResult.getSessionId() != null;
    }

    /**
     * Returns the session id for the current connection
     *
     * @return the session id for the current connection
     */
    @ConnectionIdentifier
    public String getSessionId() {
        if (connection != null && loginResult != null && metadataConnection != null) {
            return loginResult.getSessionId();
        } else {
            return null;
        }
    }


    /**
     * End the current session
     *
     * @throws Exception
     */
    @Disconnect
    public synchronized void destroySession() {
        if (isInitializedBayeuxClient() && getBayeuxClient().isConnected()) {
            getBayeuxClient().disconnect();
        }

        if (connection != null && loginResult != null) {
            try {
                connection.logout();
            } catch (ConnectionException ce) {
                LOGGER.error(ce);
            } finally {
                loginResult = null;
                connection.setConnection(null);
                connection = null;
                setBayeuxClient(null);
            }
        }
    }

    @Override
    protected boolean isReadyToSubscribe() {
        return this.isConnected();
    }

    @Start
    public void init() {
        this.registerTransformers();
    }

    /**
     * Creates a new Salesforce session
     *
     * @param username        Username used to initialize the session
     * @param password        Password used to authenticate the user
     * @param securityToken   User's security token. It can be omitted if your IP has been whitelisted on Salesforce
     * @param url             Salesforce endpoint URL
     * @param proxyHost       Hostname of the proxy
     * @param proxyPort       Port of the proxy
     * @param proxyUsername   Username used to authenticate against the proxy
     * @param proxyPassword   Password used to authenticate against the proxy
     * @param sessionId       This value could be used for specifying an active Salesforce session.
     *                        Please take into account you must specify all the connection parameters anyway since they will be used
     *                        in case of needing a reconnection.
     * @param serviceEndpoint Specifies the service endpoint. This value will only be used in case of using sessionId configuration property.
     *                        Otherwise the service endpoint will be retrieved from the login results.
     * @throws ConnectionException if a problem occurred while trying to create the session
     */
    @Connect
    public synchronized void connect(@ConnectionKey String username,
                                     @Password String password,
                                     @Optional String securityToken,
                                     @Optional @Default("https://login.salesforce.com/services/Soap/u/32.0") String url,
                                     @Optional @Placement(group = "Proxy Settings") String proxyHost,
                                     @Optional @Placement(group = "Proxy Settings") @Default("80") int proxyPort,
                                     @Optional @Placement(group = "Proxy Settings") String proxyUsername,
                                     @Optional @Placement(group = "Proxy Settings") @Password String proxyPassword,
                                     @Optional @Placement(group = "Session") String sessionId,
                                     @Optional @Placement(group = "Session") String serviceEndpoint,
                                     @Optional @Default("0") int readTimeout,
                                     @Optional @Default("0") int connectionTimeout) throws org.mule.api.ConnectionException {

        ConnectorConfig connectorConfig = createConnectorConfig(url, username, password + StringUtils.defaultString(securityToken), proxyHost, proxyPort, proxyUsername, proxyPassword, readTimeout, connectionTimeout);
        if (LOGGER.isDebugEnabled()) {
            connectorConfig.addMessageHandler(new MessageHandler() {
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

        try {
            PartnerConnection _connection = Connector.newConnection(connectorConfig);
            this.connection = new CustomPartnerConnection();
            this.connection.setConnection(_connection);
            setConnectionOptions(connection);
        } catch (ConnectionException e) {
            throw new org.mule.api.ConnectionException(ConnectionExceptionCode.UNKNOWN, null, e.getMessage(), e);
        }

        if (sessionId != null && serviceEndpoint != null) {
            connection.getSessionHeader().setSessionId(sessionId);
            connection.getConfig().setSessionId(sessionId);
            connection.getConfig().setServiceEndpoint(serviceEndpoint);
        } else {
            reconnect();
        }

        try {
            String restEndpoint = "https://" + (new URL(connectorConfig.getServiceEndpoint())).getHost() + "/services/async/" + getApiVersion();
            connectorConfig.setRestEndpoint(restEndpoint);
            bulkConnection = new BulkConnection(connectorConfig);
        } catch (AsyncApiException e) {
            throw new org.mule.api.ConnectionException(ConnectionExceptionCode.UNKNOWN, e.getExceptionCode().toString(), e.getMessage(), e);
        } catch (MalformedURLException e) {
            throw new org.mule.api.ConnectionException(ConnectionExceptionCode.UNKNOWN_HOST, null, e.getMessage(), e);
        }
        
        try {
			String metadataServiceEndpoint = "https://" + (new URL(connectorConfig.getServiceEndpoint())).getHost() + "/services/Soap/c/" + getApiVersion();
			ConnectorConfig metadataConfig = new ConnectorConfig();
			metadataConfig.setUsername(username);
			metadataConfig.setPassword(password);
			metadataConfig.setAuthEndpoint(metadataServiceEndpoint);
			metadataConfig.setServiceEndpoint(loginResult.getMetadataServerUrl());
			metadataConfig.setSessionId(loginResult.getSessionId());
			metadataConfig.setManualLogin(true);
			metadataConfig.setCompression(false);
			MetadataConnection _metadataConnection = new MetadataConnection(metadataConfig);
	        this.metadataConnection = new CustomMetadataConnection();
	        this.metadataConnection.setConnection(_metadataConnection);
		} catch (MalformedURLException e) {
			 throw new org.mule.api.ConnectionException(ConnectionExceptionCode.UNKNOWN_HOST, null, e.getMessage(), e);
		} catch (ConnectionException e) {
			 throw new org.mule.api.ConnectionException(ConnectionExceptionCode.UNKNOWN, null, e.getMessage(), e);
		}

        this.processSubscriptions();
    }

    public void reconnect() throws org.mule.api.ConnectionException {
        LOGGER.debug("Creating a Salesforce session using " + connection.getConfig().getUsername());
        try {
            loginResult = connection.login(connection.getConfig().getUsername(), connection.getConfig().getPassword());
            if (loginResult.isPasswordExpired()) {
                connection.logout();
                String username = connection.getConfig().getUsername();
                connection = null;
                throw new org.mule.api.ConnectionException(ConnectionExceptionCode.CREDENTIALS_EXPIRED, null, "The password for the user " + username + " has expired");
            }
            LOGGER.debug("Session established successfully with ID " + loginResult.getSessionId() + " at instance " + loginResult.getServerUrl());
            connection.getSessionHeader().setSessionId(loginResult.getSessionId());
            connection.getConfig().setServiceEndpoint(loginResult.getServerUrl());
            connection.getConfig().setSessionId(loginResult.getSessionId());
        } catch (ApiFault e) {
            throw new org.mule.api.ConnectionException(ConnectionExceptionCode.UNKNOWN, ((ApiFault) e).getExceptionCode().name(), ((ApiFault) e).getExceptionMessage(), e);
        } catch (ConnectionException e) {
            throw new org.mule.api.ConnectionException(ConnectionExceptionCode.UNKNOWN, null, e.getMessage(), e);
        }
    }

    /**
     * Create connector config
     *
     * @param endpoint      Salesforce endpoint
     * @param username      Username to use for authentication
     * @param password      Password to use for authentication
     * @param proxyHost
     * @param proxyPort
     * @param proxyUsername
     * @param proxyPassword
     * @return
     */
    protected ConnectorConfig createConnectorConfig(String endpoint, String username, String password, String proxyHost, int proxyPort, String proxyUsername, String proxyPassword, int readTimeout, int connectionTimeout) {
        ConnectorConfig config = new ConnectorConfig();
        config.setUsername(username);
        config.setPassword(password);
        
        config.setReadTimeout(readTimeout);
        config.setConnectionTimeout(connectionTimeout);

        config.setAuthEndpoint(endpoint);
        config.setServiceEndpoint(endpoint);

        config.setManualLogin(true);

        config.setCompression(false);

        if (proxyHost != null && proxyUsername != null && proxyPassword != null) {

            //Updated to use Proxy instead of proxyUserName since it does not work on ceratain ms proxies.
            Authenticator.setDefault(new ProxyAuthenticator(proxyUsername, proxyPassword));
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));

            config.setProxy(proxy);
        }

        SessionRenewer sessionRenewer = new SessionRenewer() {
            public SessionRenewalHeader renewSession(ConnectorConfig config) throws ConnectionException {

                try {
                    reconnect();
                } catch (org.mule.api.ConnectionException e) {
                    throw new ConnectionException(e.getMessage(), e);
                }

                SessionRenewer.SessionRenewalHeader sessionRenewalHeader = new SessionRenewer.SessionRenewalHeader();
                sessionRenewalHeader.name = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "SessionHeader");
                sessionRenewalHeader.headerElement = connection.getSessionHeader();

                return sessionRenewalHeader;
            }
        };

        config.setSessionRenewer(sessionRenewer);

        return config;
    }

    @Override
    protected CustomPartnerConnection getCustomPartnerConnection() {
        return connection;
    }

    @Override
    protected BulkConnection getBulkConnection() {
        return bulkConnection;
    }
    
    @Override
    protected CustomMetadataConnection getCustomMetadataConnection() {
        return metadataConnection;
    }
}
