
package org.mule.modules.salesforce.process;

import java.util.List;
import javax.annotation.Generated;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.devkit.ProcessInterceptor;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.routing.filter.Filter;
import org.mule.devkit.processor.ExpressionEvaluatorSupport;
import org.mule.modules.salesforce.adapters.SalesforceConnectorConnectionIdentifierAdapter;
import org.mule.modules.salesforce.connection.ConnectionManager;
import org.mule.modules.salesforce.connection.UnableToAcquireConnectionException;
import org.mule.modules.salesforce.connection.UnableToReleaseConnectionException;
import org.mule.modules.salesforce.connectivity.SalesforceConnectorConnectionKey;
import org.mule.modules.salesforce.processors.AbstractConnectedProcessor;
import org.mule.security.oauth.callback.ProcessCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Generated(value = "Mule DevKit Version 3.5.0-cascade", date = "2014-01-13T09:19:38-06:00", comments = "Build UNNAMED.1791.ad9d188")
public class ManagedConnectionProcessInterceptor<T >
    extends ExpressionEvaluatorSupport
    implements ProcessInterceptor<T, SalesforceConnectorConnectionIdentifierAdapter>
{

    private static Logger logger = LoggerFactory.getLogger(ManagedConnectionProcessInterceptor.class);
    private final ConnectionManager<SalesforceConnectorConnectionKey, SalesforceConnectorConnectionIdentifierAdapter> connectionManager;
    private final MuleContext muleContext;
    private final ProcessInterceptor<T, SalesforceConnectorConnectionIdentifierAdapter> next;

    public ManagedConnectionProcessInterceptor(ProcessInterceptor<T, SalesforceConnectorConnectionIdentifierAdapter> next, ConnectionManager<SalesforceConnectorConnectionKey, SalesforceConnectorConnectionIdentifierAdapter> connectionManager, MuleContext muleContext) {
        this.next = next;
        this.connectionManager = connectionManager;
        this.muleContext = muleContext;
    }

    public T execute(ProcessCallback<T, SalesforceConnectorConnectionIdentifierAdapter> processCallback, SalesforceConnectorConnectionIdentifierAdapter object, MessageProcessor messageProcessor, MuleEvent event)
        throws Exception
    {
        SalesforceConnectorConnectionIdentifierAdapter connection = null;
        SalesforceConnectorConnectionKey key = null;
        if ((messageProcessor!= null)&&((messageProcessor instanceof AbstractConnectedProcessor)&&(((AbstractConnectedProcessor) messageProcessor).getUsername()!= null))) {
            final String _transformedUsername = ((String) evaluateAndTransform(muleContext, event, AbstractConnectedProcessor.class.getDeclaredField("_usernameType").getGenericType(), null, ((AbstractConnectedProcessor) messageProcessor).getUsername()));
            if (_transformedUsername == null) {
                throw new UnableToAcquireConnectionException("Parameter username in method connect can't be null because is not @Optional");
            }
            final String _transformedPassword = ((String) evaluateAndTransform(muleContext, event, AbstractConnectedProcessor.class.getDeclaredField("_passwordType").getGenericType(), null, ((AbstractConnectedProcessor) messageProcessor).getPassword()));
            if (_transformedPassword == null) {
                throw new UnableToAcquireConnectionException("Parameter password in method connect can't be null because is not @Optional");
            }
            final String _transformedSecurityToken = ((String) evaluateAndTransform(muleContext, event, AbstractConnectedProcessor.class.getDeclaredField("_securityTokenType").getGenericType(), null, ((AbstractConnectedProcessor) messageProcessor).getSecurityToken()));
            if (_transformedSecurityToken == null) {
                throw new UnableToAcquireConnectionException("Parameter securityToken in method connect can't be null because is not @Optional");
            }
            final String _transformedUrl = ((String) evaluateAndTransform(muleContext, event, AbstractConnectedProcessor.class.getDeclaredField("_urlType").getGenericType(), null, ((AbstractConnectedProcessor) messageProcessor).getUrl()));
            final String _transformedProxyHost = ((String) evaluateAndTransform(muleContext, event, AbstractConnectedProcessor.class.getDeclaredField("_proxyHostType").getGenericType(), null, ((AbstractConnectedProcessor) messageProcessor).getProxyHost()));
            final Integer _transformedProxyPort = ((Integer) evaluateAndTransform(muleContext, event, AbstractConnectedProcessor.class.getDeclaredField("_proxyPortType").getGenericType(), null, ((AbstractConnectedProcessor) messageProcessor).getProxyPort()));
            final String _transformedProxyUsername = ((String) evaluateAndTransform(muleContext, event, AbstractConnectedProcessor.class.getDeclaredField("_proxyUsernameType").getGenericType(), null, ((AbstractConnectedProcessor) messageProcessor).getProxyUsername()));
            final String _transformedProxyPassword = ((String) evaluateAndTransform(muleContext, event, AbstractConnectedProcessor.class.getDeclaredField("_proxyPasswordType").getGenericType(), null, ((AbstractConnectedProcessor) messageProcessor).getProxyPassword()));
            final String _transformedSessionId = ((String) evaluateAndTransform(muleContext, event, AbstractConnectedProcessor.class.getDeclaredField("_sessionIdType").getGenericType(), null, ((AbstractConnectedProcessor) messageProcessor).getSessionId()));
            final String _transformedServiceEndpoint = ((String) evaluateAndTransform(muleContext, event, AbstractConnectedProcessor.class.getDeclaredField("_serviceEndpointType").getGenericType(), null, ((AbstractConnectedProcessor) messageProcessor).getServiceEndpoint()));
            key = new SalesforceConnectorConnectionKey(_transformedUsername, _transformedPassword, _transformedSecurityToken, _transformedUrl, _transformedProxyHost, _transformedProxyPort, _transformedProxyUsername, _transformedProxyPassword, _transformedSessionId, _transformedServiceEndpoint);
        } else {
            key = connectionManager.getDefaultConnectionKey();
        }
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(("Attempting to acquire connection using "+ key.toString()));
            }
            connection = connectionManager.acquireConnection(key);
            if (connection == null) {
                throw new UnableToAcquireConnectionException();
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug((("Connection has been acquired with [id="+ connection.getConnectionIdentifier())+"]"));
                }
            }
            return next.execute(processCallback, connection, messageProcessor, event);
        } catch (Exception e) {
            if (processCallback.getManagedExceptions()!= null) {
                for (Class exceptionClass: ((List<Class<? extends Exception>> ) processCallback.getManagedExceptions())) {
                    if (exceptionClass.isInstance(e)) {
                        if (logger.isDebugEnabled()) {
                            logger.debug((((("An exception ( "+ exceptionClass.getName())+") has been thrown. Destroying the connection with [id=")+ connection.getConnectionIdentifier())+"]"));
                        }
                        try {
                            if (connection!= null) {
                                connectionManager.destroyConnection(key, connection);
                                connection = null;
                            }
                        } catch (Exception innerException) {
                            logger.error(innerException.getMessage(), innerException);
                        }
                    }
                }
            }
            throw e;
        } finally {
            try {
                if (connection!= null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug((("Releasing the connection back into the pool [id="+ connection.getConnectionIdentifier())+"]"));
                    }
                    connectionManager.releaseConnection(key, connection);
                }
            } catch (Exception e) {
                throw new UnableToReleaseConnectionException(e);
            }
        }
    }

    public T execute(ProcessCallback<T, SalesforceConnectorConnectionIdentifierAdapter> processCallback, SalesforceConnectorConnectionIdentifierAdapter object, Filter filter, MuleMessage message)
        throws Exception
    {
        throw new UnsupportedOperationException();
    }

}
