
package org.mule.modules.salesforce.adapters;

import javax.annotation.Generated;
import org.mule.api.MuleException;
import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.lifecycle.Startable;
import org.mule.api.lifecycle.Stoppable;
import org.mule.common.MuleVersion;
import org.mule.config.MuleManifest;
import org.mule.config.i18n.CoreMessages;
import org.mule.modules.salesforce.SalesforceOAuthConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A <code>SalesforceOAuthConnectorLifecycleAdapter</code> is a wrapper around {@link SalesforceOAuthConnector } that adds lifecycle methods to the pojo.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-SNAPSHOT", date = "2014-04-11T04:37:26-05:00", comments = "Build master.1913.fb52000")
public class SalesforceOAuthConnectorLifecycleAdapter
    extends SalesforceOAuthConnectorMetadataAdapater
    implements Disposable, Initialisable, Startable, Stoppable
{


    @Override
    public void start()
        throws MuleException
    {
        super.init();
    }

    @Override
    public void stop()
        throws MuleException
    {
    }

    @Override
    public void initialise()
        throws InitialisationException
    {
        Logger log = LoggerFactory.getLogger(SalesforceOAuthConnectorLifecycleAdapter.class);
        MuleVersion connectorVersion = new MuleVersion("3.5");
        MuleVersion muleVersion = new MuleVersion(MuleManifest.getProductVersion());
        if (!muleVersion.atLeastBase(connectorVersion)) {
            throw new InitialisationException(CoreMessages.minMuleVersionNotMet(this.getMinMuleVersion()), this);
        }
    }

    @Override
    public void dispose() {
    }

}
