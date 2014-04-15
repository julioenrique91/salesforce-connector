
package org.mule.modules.salesforce.adapters;

import javax.annotation.Generated;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.devkit.ProcessAdapter;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.routing.filter.Filter;
import org.mule.modules.salesforce.SalesforceConnector;
import org.mule.security.oauth.callback.ProcessCallback;


/**
 * A <code>SalesforceConnectorProcessAdapter</code> is a wrapper around {@link SalesforceConnector } that enables custom processing strategies.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-SNAPSHOT", date = "2014-04-15T03:57:33-05:00", comments = "Build master.1915.dd1962d")
public class SalesforceConnectorProcessAdapter
    extends SalesforceConnectorLifecycleAdapter
    implements ProcessAdapter<SalesforceConnectorCapabilitiesAdapter>
{


    public<P >ProcessTemplate<P, SalesforceConnectorCapabilitiesAdapter> getProcessTemplate() {
        final SalesforceConnectorCapabilitiesAdapter object = this;
        return new ProcessTemplate<P,SalesforceConnectorCapabilitiesAdapter>() {


            @Override
            public P execute(ProcessCallback<P, SalesforceConnectorCapabilitiesAdapter> processCallback, MessageProcessor messageProcessor, MuleEvent event)
                throws Exception
            {
                return processCallback.process(object);
            }

            @Override
            public P execute(ProcessCallback<P, SalesforceConnectorCapabilitiesAdapter> processCallback, Filter filter, MuleMessage message)
                throws Exception
            {
                return processCallback.process(object);
            }

        }
        ;
    }

}
