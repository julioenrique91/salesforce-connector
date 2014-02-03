
package org.mule.modules.salesforce.config;

import javax.annotation.Generated;
import org.mule.api.processor.MessageProcessor;
import org.mule.config.spring.factories.MessageProcessorChainFactoryBean;
import org.mule.security.oauth.callback.DefaultRestoreAccessTokenCallback;

@Generated(value = "Mule DevKit Version 3.5.0-cascade", date = "2014-02-03T11:16:26-06:00", comments = "Build UNNAMED.1791.ad9d188")
public class RestoreAccessTokenCallbackFactoryBean
    extends MessageProcessorChainFactoryBean
{


    public Class getObjectType() {
        return DefaultRestoreAccessTokenCallback.class;
    }

    public Object getObject()
        throws Exception
    {
        DefaultRestoreAccessTokenCallback callback = new DefaultRestoreAccessTokenCallback();
        callback.setMessageProcessor(((MessageProcessor) super.getObject()));
        return callback;
    }

}
