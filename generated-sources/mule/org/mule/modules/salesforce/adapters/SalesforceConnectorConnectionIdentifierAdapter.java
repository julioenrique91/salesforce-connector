
package org.mule.modules.salesforce.adapters;

import javax.annotation.Generated;
import org.mule.modules.salesforce.SalesforceConnector;
import org.mule.modules.salesforce.connection.Connection;


/**
 * A <code>SalesforceConnectorConnectionIdentifierAdapter</code> is a wrapper around {@link SalesforceConnector } that implements {@link org.mule.devkit.dynamic.api.helper.Connection} interface.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-SNAPSHOT", date = "2014-04-15T02:41:26-05:00", comments = "Build master.1915.dd1962d")
public class SalesforceConnectorConnectionIdentifierAdapter
    extends SalesforceConnectorProcessAdapter
    implements Connection
{


    public String getConnectionIdentifier() {
        return super.getSessionId();
    }

}
