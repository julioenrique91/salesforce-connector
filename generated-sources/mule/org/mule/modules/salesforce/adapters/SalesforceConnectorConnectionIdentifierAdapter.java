
package org.mule.modules.salesforce.adapters;

import javax.annotation.Generated;
import org.mule.modules.salesforce.SalesforceConnector;
import org.mule.modules.salesforce.connection.Connection;


/**
 * A <code>SalesforceConnectorConnectionIdentifierAdapter</code> is a wrapper around {@link SalesforceConnector } that implements {@link org.mule.devkit.dynamic.api.helper.Connection} interface.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-M4", date = "2014-03-10T01:07:10-05:00", comments = "Build M4.1875.17b58a3")
public class SalesforceConnectorConnectionIdentifierAdapter
    extends SalesforceConnectorProcessAdapter
    implements Connection
{


    public String getConnectionIdentifier() {
        return super.getSessionId();
    }

}
