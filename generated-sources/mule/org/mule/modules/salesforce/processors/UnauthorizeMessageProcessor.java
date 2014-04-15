
package org.mule.modules.salesforce.processors;

import javax.annotation.Generated;
import org.mule.api.construct.FlowConstructAware;
import org.mule.api.processor.MessageProcessor;
import org.mule.modules.salesforce.oauth.SalesforceOAuthConnectorOAuthManager;
import org.mule.security.oauth.processor.BaseOAuth2UnauthorizeMessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Generated(value = "Mule DevKit Version 3.5.0-SNAPSHOT", date = "2014-04-15T02:41:26-05:00", comments = "Build master.1915.dd1962d")
public class UnauthorizeMessageProcessor
    extends BaseOAuth2UnauthorizeMessageProcessor
    implements FlowConstructAware, MessageProcessor
{

    private static Logger logger = LoggerFactory.getLogger(UnauthorizeMessageProcessor.class);

    @Override
    protected Class<SalesforceOAuthConnectorOAuthManager> getOAuthManagerClass() {
        return SalesforceOAuthConnectorOAuthManager.class;
    }

}
