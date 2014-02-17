
package org.mule.modules.salesforce.processors;

import javax.annotation.Generated;
import org.mule.streaming.processor.AbstractDevkitBasedPageableMessageProcessor;

@Generated(value = "Mule DevKit Version 3.5.0-SNAPSHOT", date = "2014-02-17T03:16:10-06:00", comments = "Build UNKNOWN_BUILDNUMBER")
public abstract class AbstractPagedConnectedProcessor
    extends AbstractDevkitBasedPageableMessageProcessor
{

    protected Object username;
    protected String _usernameType;
    protected Object password;
    protected String _passwordType;
    protected Object securityToken;
    protected String _securityTokenType;
    protected Object url;
    protected String _urlType;
    protected Object proxyHost;
    protected String _proxyHostType;
    protected Object proxyPort;
    protected int _proxyPortType;
    protected Object proxyUsername;
    protected String _proxyUsernameType;
    protected Object proxyPassword;
    protected String _proxyPasswordType;
    protected Object sessionId;
    protected String _sessionIdType;
    protected Object serviceEndpoint;
    protected String _serviceEndpointType;

    public AbstractPagedConnectedProcessor(String operationName) {
        super(operationName);
    }

    /**
     * Sets proxyUsername
     * 
     * @param value Value to set
     */
    public void setProxyUsername(Object value) {
        this.proxyUsername = value;
    }

    /**
     * Retrieves proxyUsername
     * 
     */
    public Object getProxyUsername() {
        return this.proxyUsername;
    }

    /**
     * Sets username
     * 
     * @param value Value to set
     */
    public void setUsername(Object value) {
        this.username = value;
    }

    /**
     * Retrieves username
     * 
     */
    public Object getUsername() {
        return this.username;
    }

    /**
     * Sets sessionId
     * 
     * @param value Value to set
     */
    public void setSessionId(Object value) {
        this.sessionId = value;
    }

    /**
     * Retrieves sessionId
     * 
     */
    public Object getSessionId() {
        return this.sessionId;
    }

    /**
     * Sets proxyHost
     * 
     * @param value Value to set
     */
    public void setProxyHost(Object value) {
        this.proxyHost = value;
    }

    /**
     * Retrieves proxyHost
     * 
     */
    public Object getProxyHost() {
        return this.proxyHost;
    }

    /**
     * Sets securityToken
     * 
     * @param value Value to set
     */
    public void setSecurityToken(Object value) {
        this.securityToken = value;
    }

    /**
     * Retrieves securityToken
     * 
     */
    public Object getSecurityToken() {
        return this.securityToken;
    }

    /**
     * Sets serviceEndpoint
     * 
     * @param value Value to set
     */
    public void setServiceEndpoint(Object value) {
        this.serviceEndpoint = value;
    }

    /**
     * Retrieves serviceEndpoint
     * 
     */
    public Object getServiceEndpoint() {
        return this.serviceEndpoint;
    }

    /**
     * Sets proxyPort
     * 
     * @param value Value to set
     */
    public void setProxyPort(Object value) {
        this.proxyPort = value;
    }

    /**
     * Retrieves proxyPort
     * 
     */
    public Object getProxyPort() {
        return this.proxyPort;
    }

    /**
     * Sets password
     * 
     * @param value Value to set
     */
    public void setPassword(Object value) {
        this.password = value;
    }

    /**
     * Retrieves password
     * 
     */
    public Object getPassword() {
        return this.password;
    }

    /**
     * Sets proxyPassword
     * 
     * @param value Value to set
     */
    public void setProxyPassword(Object value) {
        this.proxyPassword = value;
    }

    /**
     * Retrieves proxyPassword
     * 
     */
    public Object getProxyPassword() {
        return this.proxyPassword;
    }

    /**
     * Sets url
     * 
     * @param value Value to set
     */
    public void setUrl(Object value) {
        this.url = value;
    }

    /**
     * Retrieves url
     * 
     */
    public Object getUrl() {
        return this.url;
    }

}
