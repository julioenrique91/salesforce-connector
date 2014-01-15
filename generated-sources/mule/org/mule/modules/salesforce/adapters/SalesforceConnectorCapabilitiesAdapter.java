
package org.mule.modules.salesforce.adapters;

import javax.annotation.Generated;
import org.mule.api.devkit.capability.Capabilities;
import org.mule.api.devkit.capability.ModuleCapability;
import org.mule.modules.salesforce.SalesforceConnector;


/**
 * A <code>SalesforceConnectorCapabilitiesAdapter</code> is a wrapper around {@link SalesforceConnector } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-cascade", date = "2014-01-15T04:12:08-06:00", comments = "Build UNNAMED.1791.ad9d188")
public class SalesforceConnectorCapabilitiesAdapter
    extends SalesforceConnector
    implements Capabilities
{


    /**
     * Returns true if this module implements such capability
     * 
     */
    public boolean isCapableOf(ModuleCapability capability) {
        if (capability == ModuleCapability.LIFECYCLE_CAPABLE) {
            return true;
        }
        if (capability == ModuleCapability.CONNECTION_MANAGEMENT_CAPABLE) {
            return true;
        }
        return false;
    }

}
