
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
import org.mule.modules.salesforce.processors.ConnectivityProcessor;
import org.mule.security.oauth.callback.ProcessCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Generated(value = "Mule DevKit Version 3.5.0-SNAPSHOT", date = "2014-04-11T04:37:26-05:00", comments = "Build master.1913.fb52000")
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

    @Override
    public T execute(ProcessCallback<T, SalesforceConnectorConnectionIdentifierAdapter> processCallback, SalesforceConnectorConnectionIdentifierAdapter object, MessageProcessor messageProcessor, MuleEvent event)
        throws Exception
    {
        SalesforceConnectorConnectionIdentifierAdapter connection = null;
        SalesforceConnectorConnectionKey key = null;
        if (hasConnectionKeysOverride(messageProcessor)) {
            ConnectivityProcessor connectivityProcessor = ((ConnectivityProcessor) messageProcessor);
            final String _transformedUsername = ((String) evaluateAndTransform(muleContext, event, connectivityProcessor.typeFor("_usernameType"), null, connectivityProcessor.getUsername()));
            if (_transformedUsername == null) {
                throw new UnableToAcquireConnectionException("Parameter username in method connect can't be null because is not @Optional");
            }
            final String _transformedPassword = ((String) evaluateAndTransform(muleContext, event, connectivityProcessor.typeFor("_passwordType"), null, connectivityProcessor.getPassword()));
            if (_transformedPassword == null) {
                throw new UnableToAcquireConnectionException("Parameter password in method connect can't be null because is not @Optional");
            }
            final String _transformedSecurityToken = ((String) evaluateAndTransform(muleContext, event, connectivityProcessor.typeFor("_securityTokenType"), null, connectivityProcessor.getSecurityToken()));
            if (_transformedSecurityToken == null) {
                throw new UnableToAcquireConnectionException("Parameter securityToken in method connect can't be null because is not @Optional");
            }
            final String _transformedUrl = ((String) evaluateAndTransform(muleContext, event, connectivityProcessor.typeFor("_urlType"), null, connectivityProcessor.getUrl()));
            final String _transformedProxyHost = ((String) evaluateAndTransform(muleContext, event, connectivityProcessor.typeFor("_proxyHostType"), null, connectivityProcessor.getProxyHost()));
            final Integer _transformedProxyPort = ((Integer) evaluateAndTransform(muleContext, event, connectivityProcessor.typeFor("_proxyPortType"), null, connectivityProcessor.getProxyPort()));
            final String _transformedProxyUsername = ((String) evaluateAndTransform(muleContext, event, connectivityProcessor.typeFor("_proxyUsernameType"), null, connectivityProcessor.getProxyUsername()));
            final String _transformedProxyPassword = ((String) evaluateAndTransform(muleContext, event, connectivityProcessor.typeFor("_proxyPasswordType"), null, connectivityProcessor.getProxyPassword()));
            final String _transformedSessionId = ((String) evaluateAndTransform(muleContext, event, connectivityProcessor.typeFor("_sessionIdType"), null, connectivityProcessor.getSessionId()));
            final String _transformedServiceEndpoint = ((String) evaluateAndTransform(muleContext, event, connectivityProcessor.typeFor("_serviceEndpointType"), null, connectivityProcessor.getServiceEndpoint()));
            key = new SalesforceConnectorConnectionKey(_transformedUsername, _transformedPassword, _transformedSecurityToken, _transformedUrl, _transformedProxyHost, _transformedProxyPort, _transformedProxyUsername, _transformedProxyPassword, _transformedSessionId, _transformedServiceEndpoint);
        } else {
            key = connectionManager.getEvaluatedConnectionKey(event);
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

    /**
     * Validates that the current message processor has changed any of its connection parameters at processor level. If so, a new SalesforceConnectorConnectionKey must be generated
     * 
     * @param messageProcessor
     *     the message processor to test against the keys
     * @return
     *     true if any of the parameters in @Connect method annotated with @ConnectionKey was override in the XML, false otherwise  
     */
    private Boolean hasConnectionKeysOverride(MessageProcessor messageProcessor) {
        if ((messageProcessor == null)||(!(messageProcessor instanceof ConnectivityProcessor))) {
            return false;
        }
        ConnectivityProcessor connectivityProcessor = ((ConnectivityProcessor) messageProcessor);
        if (connectivityProcessor.getUsername()!= null) {
            return true;
        }
        return false;
    }

    public T execute(ProcessCallback<T, SalesforceConnectorConnectionIdentifierAdapter> processCallback, SalesforceConnectorConnectionIdentifierAdapter object, Filter filter, MuleMessage message)
        throws Exception
    {
        throw new UnsupportedOperationException();
    }

}
