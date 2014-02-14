
package org.mule.modules.salesforce.oauth;

import java.io.Serializable;
import javax.annotation.Generated;
import org.mule.api.store.ObjectStore;
import org.mule.common.security.oauth.OAuthState;
import org.mule.modules.salesforce.adapters.SalesforceOAuthConnectorOAuth2Adapter;
import org.mule.security.oauth.BaseOAuthClientFactory;
import org.mule.security.oauth.OAuth2Adapter;
import org.mule.security.oauth.OAuth2Manager;

@Generated(value = "Mule DevKit Version 3.5.0-SNAPSHOT", date = "2014-02-14T01:39:50-06:00", comments = "Build UNKNOWN_BUILDNUMBER")
public class SalesforceOAuthConnectorOAuthClientFactory
    extends BaseOAuthClientFactory
{

    private SalesforceOAuthConnectorOAuthManager oauthManager;

    public SalesforceOAuthConnectorOAuthClientFactory(OAuth2Manager<OAuth2Adapter> oauthManager, ObjectStore<Serializable> objectStore) {
        super(oauthManager, objectStore);
        this.oauthManager = (SalesforceOAuthConnectorOAuthManager) oauthManager;
    }

    @Override
    protected Class<? extends OAuth2Adapter> getAdapterClass() {
        return SalesforceOAuthConnectorOAuth2Adapter.class;
    }

    @Override
    protected void setCustomAdapterProperties(OAuth2Adapter adapter, OAuthState state) {
        SalesforceOAuthConnectorOAuth2Adapter connector = ((SalesforceOAuthConnectorOAuth2Adapter) adapter);
        connector.setInstanceId(state.getCustomProperty("instanceId"));
        connector.setUserId(state.getCustomProperty("userId"));
        connector.setTimeObjectStore(oauthManager.getTimeObjectStore());
        connector.setClientId(oauthManager.getClientId());
        connector.setAssignmentRuleId(oauthManager.getAssignmentRuleId());
        connector.setUseDefaultRule(oauthManager.getUseDefaultRule());
        connector.setBatchSobjectMaxDepth(oauthManager.getBatchSobjectMaxDepth());
        connector.setAllowFieldTruncationSupport(oauthManager.getAllowFieldTruncationSupport());
    }

    @Override
    protected void setCustomStateProperties(OAuth2Adapter adapter, OAuthState state) {
        SalesforceOAuthConnectorOAuth2Adapter connector = ((SalesforceOAuthConnectorOAuth2Adapter) adapter);
        state.setCustomProperty("instanceId", connector.getInstanceId());
        state.setCustomProperty("userId", connector.getUserId());
    }

}
