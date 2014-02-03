
package org.mule.modules.salesforce.oauth;

import java.io.Serializable;
import javax.annotation.Generated;
import org.mule.api.store.ObjectStore;
import org.mule.common.security.oauth.OAuthState;
import org.mule.modules.salesforce.adapters.SalesforceOAuthConnectorOAuth2Adapter;
import org.mule.security.oauth.BaseOAuthClientFactory;
import org.mule.security.oauth.OAuth2Adapter;
import org.mule.security.oauth.OAuth2Manager;

@Generated(value = "Mule DevKit Version 3.5.0-cascade", date = "2014-02-03T11:24:15-06:00", comments = "Build UNNAMED.1791.ad9d188")
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
    }

}
