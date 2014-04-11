
package org.mule.modules.salesforce.config;

import javax.annotation.Generated;
import org.mule.api.processor.MessageProcessor;
import org.mule.config.spring.factories.MessageProcessorChainFactoryBean;
import org.mule.security.oauth.callback.DefaultSaveAccessTokenCallback;

@Generated(value = "Mule DevKit Version 3.5.0-SNAPSHOT", date = "2014-04-11T04:46:13-05:00", comments = "Build master.1913.fb52000")
public class SaveAccessTokenCallbackFactoryBean
    extends MessageProcessorChainFactoryBean
{


    public Class getObjectType() {
        return DefaultSaveAccessTokenCallback.class;
    }

    public Object getObject()
        throws Exception
    {
        DefaultSaveAccessTokenCallback callback = new DefaultSaveAccessTokenCallback();
        callback.setMessageProcessor(((MessageProcessor) super.getObject()));
        return callback;
    }

}
