
package org.mule.modules.salesforce.oauth;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Generated;
import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.api.expression.ExpressionManager;
import org.mule.api.store.ObjectStore;
import org.mule.modules.salesforce.SalesforceOAuthConnector;
import org.mule.modules.salesforce.adapters.SalesforceOAuthConnectorOAuth2Adapter;
import org.mule.security.oauth.BaseOAuth2Manager;
import org.mule.security.oauth.OAuth2Adapter;
import org.mule.security.oauth.OAuth2Manager;
import org.mule.security.oauth.OnNoTokenPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A {@code SalesforceOAuthConnectorOAuthManager} is a wrapper around {@link SalesforceOAuthConnector } that adds access token management capabilities to the pojo.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-SNAPSHOT", date = "2014-02-14T12:48:49-06:00", comments = "Build UNKNOWN_BUILDNUMBER")
public class SalesforceOAuthConnectorOAuthManager
    extends BaseOAuth2Manager<OAuth2Adapter>
{

    private static Logger logger = LoggerFactory.getLogger(SalesforceOAuthConnectorOAuthManager.class);
    private final static String MODULE_NAME = "Salesforce (OAuth)";
    private final static String MODULE_VERSION = "5.4.5-SNAPSHOT";
    private final static String DEVKIT_VERSION = "3.5.0-SNAPSHOT";
    private final static String DEVKIT_BUILD = "UNKNOWN_BUILDNUMBER";

    @Override
    protected Logger getLogger() {
        return logger;
    }

    /**
     * Sets consumerKey
     * 
     * @param key to set
     */
    public void setConsumerKey(String value) {
        super.setConsumerKey(value);
    }

    /**
     * Sets consumerSecret
     * 
     * @param secret to set
     */
    public void setConsumerSecret(String value) {
        super.setConsumerSecret(value);
    }

    /**
     * Sets timeObjectStore
     * 
     * @param scope to set
     */
    public void setTimeObjectStore(ObjectStore<? extends Serializable> value) {
        SalesforceOAuthConnectorOAuth2Adapter connector = ((SalesforceOAuthConnectorOAuth2Adapter) this.getDefaultUnauthorizedConnector());
        connector.setTimeObjectStore(value);
    }

    /**
     * Retrieves timeObjectStore
     * 
     */
    public ObjectStore<? extends Serializable> getTimeObjectStore() {
        SalesforceOAuthConnectorOAuth2Adapter connector = ((SalesforceOAuthConnectorOAuth2Adapter) this.getDefaultUnauthorizedConnector());
        return connector.getTimeObjectStore();
    }

    /**
     * Sets clientId
     * 
     * @param scope to set
     */
    public void setClientId(String value) {
        SalesforceOAuthConnectorOAuth2Adapter connector = ((SalesforceOAuthConnectorOAuth2Adapter) this.getDefaultUnauthorizedConnector());
        connector.setClientId(value);
    }

    /**
     * Retrieves clientId
     * 
     */
    public String getClientId() {
        SalesforceOAuthConnectorOAuth2Adapter connector = ((SalesforceOAuthConnectorOAuth2Adapter) this.getDefaultUnauthorizedConnector());
        return connector.getClientId();
    }

    /**
     * Sets assignmentRuleId
     * 
     * @param scope to set
     */
    public void setAssignmentRuleId(String value) {
        SalesforceOAuthConnectorOAuth2Adapter connector = ((SalesforceOAuthConnectorOAuth2Adapter) this.getDefaultUnauthorizedConnector());
        connector.setAssignmentRuleId(value);
    }

    /**
     * Retrieves assignmentRuleId
     * 
     */
    public String getAssignmentRuleId() {
        SalesforceOAuthConnectorOAuth2Adapter connector = ((SalesforceOAuthConnectorOAuth2Adapter) this.getDefaultUnauthorizedConnector());
        return connector.getAssignmentRuleId();
    }

    /**
     * Sets useDefaultRule
     * 
     * @param scope to set
     */
    public void setUseDefaultRule(Boolean value) {
        SalesforceOAuthConnectorOAuth2Adapter connector = ((SalesforceOAuthConnectorOAuth2Adapter) this.getDefaultUnauthorizedConnector());
        connector.setUseDefaultRule(value);
    }

    /**
     * Retrieves useDefaultRule
     * 
     */
    public Boolean getUseDefaultRule() {
        SalesforceOAuthConnectorOAuth2Adapter connector = ((SalesforceOAuthConnectorOAuth2Adapter) this.getDefaultUnauthorizedConnector());
        return connector.getUseDefaultRule();
    }

    /**
     * Sets batchSobjectMaxDepth
     * 
     * @param scope to set
     */
    public void setBatchSobjectMaxDepth(Integer value) {
        SalesforceOAuthConnectorOAuth2Adapter connector = ((SalesforceOAuthConnectorOAuth2Adapter) this.getDefaultUnauthorizedConnector());
        connector.setBatchSobjectMaxDepth(value);
    }

    /**
     * Retrieves batchSobjectMaxDepth
     * 
     */
    public Integer getBatchSobjectMaxDepth() {
        SalesforceOAuthConnectorOAuth2Adapter connector = ((SalesforceOAuthConnectorOAuth2Adapter) this.getDefaultUnauthorizedConnector());
        return connector.getBatchSobjectMaxDepth();
    }

    /**
     * Sets allowFieldTruncationSupport
     * 
     * @param scope to set
     */
    public void setAllowFieldTruncationSupport(Boolean value) {
        SalesforceOAuthConnectorOAuth2Adapter connector = ((SalesforceOAuthConnectorOAuth2Adapter) this.getDefaultUnauthorizedConnector());
        connector.setAllowFieldTruncationSupport(value);
    }

    /**
     * Retrieves allowFieldTruncationSupport
     * 
     */
    public Boolean getAllowFieldTruncationSupport() {
        SalesforceOAuthConnectorOAuth2Adapter connector = ((SalesforceOAuthConnectorOAuth2Adapter) this.getDefaultUnauthorizedConnector());
        return connector.getAllowFieldTruncationSupport();
    }

    public String getModuleName() {
        return MODULE_NAME;
    }

    public String getModuleVersion() {
        return MODULE_VERSION;
    }

    public String getDevkitVersion() {
        return DEVKIT_VERSION;
    }

    public String getDevkitBuild() {
        return DEVKIT_BUILD;
    }

    @Override
    protected OAuth2Adapter instantiateAdapter() {
        return new SalesforceOAuthConnectorOAuth2Adapter(this);
    }

    @Override
    protected KeyedPoolableObjectFactory createPoolFactory(OAuth2Manager<OAuth2Adapter> oauthManager, ObjectStore<Serializable> objectStore) {
        return new SalesforceOAuthConnectorOAuthClientFactory(oauthManager, objectStore);
    }

    @Override
    protected void setCustomProperties(OAuth2Adapter adapter) {
        SalesforceOAuthConnectorOAuth2Adapter connector = ((SalesforceOAuthConnectorOAuth2Adapter) adapter);
        connector.setConsumerKey(getConsumerKey());
        connector.setConsumerSecret(getConsumerSecret());
        connector.setTimeObjectStore(getTimeObjectStore());
        connector.setClientId(getClientId());
        connector.setAssignmentRuleId(getAssignmentRuleId());
        connector.setUseDefaultRule(getUseDefaultRule());
        connector.setBatchSobjectMaxDepth(getBatchSobjectMaxDepth());
        connector.setAllowFieldTruncationSupport(getAllowFieldTruncationSupport());
    }

    protected void fetchCallbackParameters(OAuth2Adapter adapter, String response) {
        SalesforceOAuthConnectorOAuth2Adapter connector = ((SalesforceOAuthConnectorOAuth2Adapter) adapter);
        ExpressionManager expressionManager = (muleContext.getExpressionManager());
        MuleMessage muleMessage = new DefaultMuleMessage(response, (muleContext));
        connector.setInstanceId(((String) expressionManager.evaluate("#[json:instance_url]", muleMessage)));
        connector.setUserId(((String) expressionManager.evaluate("#[json:id]", muleMessage)));
    }

    public void setOnNoToken(OnNoTokenPolicy policy) {
        this.getDefaultUnauthorizedConnector().setOnNoTokenPolicy(policy);
    }

    @Override
    protected Set<Class<? extends Exception>> refreshAccessTokenOn() {
        Set<Class<? extends Exception>> types = new HashSet<Class<? extends Exception>>();
        types.add((org.mule.modules.salesforce.exception.SalesforceSessionExpiredException.class));
        return types;
    }

}
