
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
@Generated(value = "Mule DevKit Version 3.5.0-cascade", date = "2014-01-13T03:42:10-06:00", comments = "Build UNNAMED.1791.ad9d188")
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
