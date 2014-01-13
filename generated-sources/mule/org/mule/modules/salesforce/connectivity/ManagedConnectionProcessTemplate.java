
package org.mule.modules.salesforce.connectivity;

import javax.annotation.Generated;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.devkit.ProcessInterceptor;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.routing.filter.Filter;
import org.mule.modules.salesforce.adapters.SalesforceConnectorConnectionIdentifierAdapter;
import org.mule.modules.salesforce.connection.ConnectionManager;
import org.mule.modules.salesforce.process.ManagedConnectionProcessInterceptor;
import org.mule.security.oauth.callback.ProcessCallback;
import org.mule.security.oauth.process.ProcessCallbackProcessInterceptor;
import org.mule.security.oauth.process.RetryProcessInterceptor;

@Generated(value = "Mule DevKit Version 3.5.0-cascade", date = "2014-01-13T03:30:10-06:00", comments = "Build UNNAMED.1791.ad9d188")
public class ManagedConnectionProcessTemplate<P >implements ProcessTemplate<P, SalesforceConnectorConnectionIdentifierAdapter>
{

    private final ProcessInterceptor<P, SalesforceConnectorConnectionIdentifierAdapter> processInterceptor;

    public ManagedConnectionProcessTemplate(ConnectionManager<SalesforceConnectorConnectionKey, SalesforceConnectorConnectionIdentifierAdapter> connectionManager, MuleContext muleContext) {
        ProcessInterceptor<P, SalesforceConnectorConnectionIdentifierAdapter> processCallbackProcessInterceptor = new ProcessCallbackProcessInterceptor<P, SalesforceConnectorConnectionIdentifierAdapter>();
        ProcessInterceptor<P, SalesforceConnectorConnectionIdentifierAdapter> managedConnectionProcessInterceptor = new ManagedConnectionProcessInterceptor<P>(processCallbackProcessInterceptor, connectionManager, muleContext);
        ProcessInterceptor<P, SalesforceConnectorConnectionIdentifierAdapter> retryProcessInterceptor = new RetryProcessInterceptor<P, SalesforceConnectorConnectionIdentifierAdapter>(managedConnectionProcessInterceptor, muleContext, connectionManager.getRetryPolicyTemplate());
        processInterceptor = retryProcessInterceptor;
    }

    public P execute(ProcessCallback<P, SalesforceConnectorConnectionIdentifierAdapter> processCallback, MessageProcessor messageProcessor, MuleEvent event)
        throws Exception
    {
        return processInterceptor.execute(processCallback, null, messageProcessor, event);
    }

    public P execute(ProcessCallback<P, SalesforceConnectorConnectionIdentifierAdapter> processCallback, Filter filter, MuleMessage message)
        throws Exception
    {
        return processInterceptor.execute(processCallback, null, filter, message);
    }

}