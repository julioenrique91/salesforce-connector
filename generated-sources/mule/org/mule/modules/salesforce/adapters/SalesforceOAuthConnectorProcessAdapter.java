
package org.mule.modules.salesforce.adapters;

import javax.annotation.Generated;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.devkit.ProcessAdapter;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.routing.filter.Filter;
import org.mule.modules.salesforce.SalesforceOAuthConnector;
import org.mule.security.oauth.callback.ProcessCallback;


/**
 * A <code>SalesforceOAuthConnectorProcessAdapter</code> is a wrapper around {@link SalesforceOAuthConnector } that enables custom processing strategies.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-M4", date = "2014-03-07T01:13:44-06:00", comments = "Build M4.1875.17b58a3")
public class SalesforceOAuthConnectorProcessAdapter
    extends SalesforceOAuthConnectorLifecycleAdapter
    implements ProcessAdapter<SalesforceOAuthConnectorCapabilitiesAdapter>
{


    public<P >ProcessTemplate<P, SalesforceOAuthConnectorCapabilitiesAdapter> getProcessTemplate() {
        final SalesforceOAuthConnectorCapabilitiesAdapter object = this;
        return new ProcessTemplate<P,SalesforceOAuthConnectorCapabilitiesAdapter>() {


            @Override
            public P execute(ProcessCallback<P, SalesforceOAuthConnectorCapabilitiesAdapter> processCallback, MessageProcessor messageProcessor, MuleEvent event)
                throws Exception
            {
                return processCallback.process(object);
            }

            @Override
            public P execute(ProcessCallback<P, SalesforceOAuthConnectorCapabilitiesAdapter> processCallback, Filter filter, MuleMessage message)
                throws Exception
            {
                return processCallback.process(object);
            }

        }
        ;
    }

}
