
package org.mule.modules.salesforce.connectivity;

import javax.annotation.Generated;


/**
 * A tuple of connection parameters
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-cascade", date = "2014-01-13T03:30:10-06:00", comments = "Build UNNAMED.1791.ad9d188")
public class SalesforceConnectorConnectionKey {

    /**
     * 
     */
    private String username;
    /**
     * 
     */
    private String password;
    /**
     * 
     */
    private String securityToken;
    /**
     * 
     */
    private String url;
    /**
     * 
     */
    private String proxyHost;
    /**
     * 
     */
    private int proxyPort;
    /**
     * 
     */
    private String proxyUsername;
    /**
     * 
     */
    private String proxyPassword;
    /**
     * 
     */
    private String sessionId;
    /**
     * 
     */
    private String serviceEndpoint;

    public SalesforceConnectorConnectionKey(String username, String password, String securityToken, String url, String proxyHost, int proxyPort, String proxyUsername, String proxyPassword, String sessionId, String serviceEndpoint) {
        this.username = username;
        this.password = password;
        this.securityToken = securityToken;
        this.url = url;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.sessionId = sessionId;
        this.serviceEndpoint = serviceEndpoint;
    }

    /**
     * Sets proxyUsername
     * 
     * @param value Value to set
     */
    public void setProxyUsername(String value) {
        this.proxyUsername = value;
    }

    /**
     * Retrieves proxyUsername
     * 
     */
    public String getProxyUsername() {
        return this.proxyUsername;
    }

    /**
     * Sets username
     * 
     * @param value Value to set
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Retrieves username
     * 
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets sessionId
     * 
     * @param value Value to set
     */
    public void setSessionId(String value) {
        this.sessionId = value;
    }

    /**
     * Retrieves sessionId
     * 
     */
    public String getSessionId() {
        return this.sessionId;
    }

    /**
     * Sets proxyHost
     * 
     * @param value Value to set
     */
    public void setProxyHost(String value) {
        this.proxyHost = value;
    }

    /**
     * Retrieves proxyHost
     * 
     */
    public String getProxyHost() {
        return this.proxyHost;
    }

    /**
     * Sets securityToken
     * 
     * @param value Value to set
     */
    public void setSecurityToken(String value) {
        this.securityToken = value;
    }

    /**
     * Retrieves securityToken
     * 
     */
    public String getSecurityToken() {
        return this.securityToken;
    }

    /**
     * Sets serviceEndpoint
     * 
     * @param value Value to set
     */
    public void setServiceEndpoint(String value) {
        this.serviceEndpoint = value;
    }

    /**
     * Retrieves serviceEndpoint
     * 
     */
    public String getServiceEndpoint() {
        return this.serviceEndpoint;
    }

    /**
     * Sets proxyPort
     * 
     * @param value Value to set
     */
    public void setProxyPort(int value) {
        this.proxyPort = value;
    }

    /**
     * Retrieves proxyPort
     * 
     */
    public int getProxyPort() {
        return this.proxyPort;
    }

    /**
     * Sets password
     * 
     * @param value Value to set
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Retrieves password
     * 
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets proxyPassword
     * 
     * @param value Value to set
     */
    public void setProxyPassword(String value) {
        this.proxyPassword = value;
    }

    /**
     * Retrieves proxyPassword
     * 
     */
    public String getProxyPassword() {
        return this.proxyPassword;
    }

    /**
     * Sets url
     * 
     * @param value Value to set
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Retrieves url
     * 
     */
    public String getUrl() {
        return this.url;
    }

    public int hashCode() {
        int hash = 1;
        hash = (hash* 31);
        if (this.username!= null) {
            hash += this.username.hashCode();
        }
        return hash;
    }

    public boolean equals(Object obj) {
        return (((obj instanceof SalesforceConnectorConnectionKey)&&(this.username!= null))&&this.username.equals(((SalesforceConnectorConnectionKey) obj).username));
    }

}