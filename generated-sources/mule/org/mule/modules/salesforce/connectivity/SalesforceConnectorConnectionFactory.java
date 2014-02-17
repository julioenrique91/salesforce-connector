
package org.mule.modules.salesforce.connectivity;

import javax.annotation.Generated;
import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.mule.api.context.MuleContextAware;
import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.Startable;
import org.mule.api.lifecycle.Stoppable;
import org.mule.modules.salesforce.adapters.SalesforceConnectorConnectionIdentifierAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Generated(value = "Mule DevKit Version 3.5.0-SNAPSHOT", date = "2014-02-17T01:15:14-06:00", comments = "Build UNKNOWN_BUILDNUMBER")
public class SalesforceConnectorConnectionFactory implements KeyedPoolableObjectFactory
{

    private static Logger logger = LoggerFactory.getLogger(SalesforceConnectorConnectionFactory.class);
    private SalesforceConnectorConnectionManager connectionManager;

    public SalesforceConnectorConnectionFactory(SalesforceConnectorConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public Object makeObject(Object key)
        throws Exception
    {
        if (!(key instanceof SalesforceConnectorConnectionKey)) {
            if (key == null) {
                logger.warn("Connection key is null");
            } else {
                logger.warn("Cannot cast key of type ".concat(key.getClass().getName().concat(" to ").concat("org.mule.modules.salesforce.connectivity.SalesforceConnectorConnectionKey")));
            }
            throw new RuntimeException("Invalid key type ".concat(key.getClass().getName()));
        }
        SalesforceConnectorConnectionIdentifierAdapter connector = new SalesforceConnectorConnectionIdentifierAdapter();
        connector.setTimeObjectStore(connectionManager.getTimeObjectStore());
        connector.setClientId(connectionManager.getClientId());
        connector.setAssignmentRuleId(connectionManager.getAssignmentRuleId());
        connector.setUseDefaultRule(connectionManager.getUseDefaultRule());
        connector.setBatchSobjectMaxDepth(connectionManager.getBatchSobjectMaxDepth());
        connector.setAllowFieldTruncationSupport(connectionManager.getAllowFieldTruncationSupport());
        if (connector instanceof MuleContextAware) {
            ((MuleContextAware) connector).setMuleContext(connectionManager.getMuleContext());
        }
        if (connector instanceof Initialisable) {
            ((Initialisable) connector).initialise();
        }
        if (connector instanceof Startable) {
            ((Startable) connector).start();
        }
        if (!connector.isConnected()) {
            connector.connect(((SalesforceConnectorConnectionKey) key).getUsername(), ((SalesforceConnectorConnectionKey) key).getPassword(), ((SalesforceConnectorConnectionKey) key).getSecurityToken(), ((SalesforceConnectorConnectionKey) key).getUrl(), ((SalesforceConnectorConnectionKey) key).getProxyHost(), ((SalesforceConnectorConnectionKey) key).getProxyPort(), ((SalesforceConnectorConnectionKey) key).getProxyUsername(), ((SalesforceConnectorConnectionKey) key).getProxyPassword(), ((SalesforceConnectorConnectionKey) key).getSessionId(), ((SalesforceConnectorConnectionKey) key).getServiceEndpoint());
        }
        return connector;
    }

    public void destroyObject(Object key, Object obj)
        throws Exception
    {
        if (!(key instanceof SalesforceConnectorConnectionKey)) {
            if (key == null) {
                logger.warn("Connection key is null");
            } else {
                logger.warn("Cannot cast key of type ".concat(key.getClass().getName().concat(" to ").concat("org.mule.modules.salesforce.connectivity.SalesforceConnectorConnectionKey")));
            }
            throw new RuntimeException("Invalid key type ".concat(key.getClass().getName()));
        }
        if (!(obj instanceof SalesforceConnectorConnectionIdentifierAdapter)) {
            if (obj == null) {
                logger.warn("Connector is null");
            } else {
                logger.warn("Cannot cast connector of type ".concat(obj.getClass().getName().concat(" to ").concat("org.mule.modules.salesforce.adapters.SalesforceConnectorConnectionIdentifierAdapter")));
            }
            throw new RuntimeException("Invalid connector type ".concat(obj.getClass().getName()));
        }
        try {
            ((SalesforceConnectorConnectionIdentifierAdapter) obj).destroySession();
        } catch (Exception e) {
            throw e;
        } finally {
            if (((SalesforceConnectorConnectionIdentifierAdapter) obj) instanceof Stoppable) {
                ((Stoppable) obj).stop();
            }
            if (((SalesforceConnectorConnectionIdentifierAdapter) obj) instanceof Disposable) {
                ((Disposable) obj).dispose();
            }
        }
    }

    public boolean validateObject(Object key, Object obj) {
        if (!(obj instanceof SalesforceConnectorConnectionIdentifierAdapter)) {
            if (obj == null) {
                logger.warn("Connector is null");
            } else {
                logger.warn("Cannot cast connector of type ".concat(obj.getClass().getName().concat(" to ").concat("org.mule.modules.salesforce.adapters.SalesforceConnectorConnectionIdentifierAdapter")));
            }
            throw new RuntimeException("Invalid connector type ".concat(obj.getClass().getName()));
        }
        try {
            return ((SalesforceConnectorConnectionIdentifierAdapter) obj).isConnected();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public void activateObject(Object key, Object obj)
        throws Exception
    {
        if (!(key instanceof SalesforceConnectorConnectionKey)) {
            throw new RuntimeException("Invalid key type");
        }
        if (!(obj instanceof SalesforceConnectorConnectionIdentifierAdapter)) {
            throw new RuntimeException("Invalid connector type");
        }
        try {
            if (!((SalesforceConnectorConnectionIdentifierAdapter) obj).isConnected()) {
                ((SalesforceConnectorConnectionIdentifierAdapter) obj).connect(((SalesforceConnectorConnectionKey) key).getUsername(), ((SalesforceConnectorConnectionKey) key).getPassword(), ((SalesforceConnectorConnectionKey) key).getSecurityToken(), ((SalesforceConnectorConnectionKey) key).getUrl(), ((SalesforceConnectorConnectionKey) key).getProxyHost(), ((SalesforceConnectorConnectionKey) key).getProxyPort(), ((SalesforceConnectorConnectionKey) key).getProxyUsername(), ((SalesforceConnectorConnectionKey) key).getProxyPassword(), ((SalesforceConnectorConnectionKey) key).getSessionId(), ((SalesforceConnectorConnectionKey) key).getServiceEndpoint());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void passivateObject(Object key, Object obj)
        throws Exception
    {
    }

}
