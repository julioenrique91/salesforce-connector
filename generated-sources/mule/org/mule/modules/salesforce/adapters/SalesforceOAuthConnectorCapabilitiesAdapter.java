
package org.mule.modules.salesforce.adapters;

import javax.annotation.Generated;
import org.mule.api.devkit.capability.Capabilities;
import org.mule.api.devkit.capability.ModuleCapability;
import org.mule.modules.salesforce.SalesforceOAuthConnector;


/**
 * A <code>SalesforceOAuthConnectorCapabilitiesAdapter</code> is a wrapper around {@link SalesforceOAuthConnector } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-M4", date = "2014-03-28T02:36:29-05:00", comments = "Build M4.1875.17b58a3")
public class SalesforceOAuthConnectorCapabilitiesAdapter
    extends SalesforceOAuthConnector
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
        if (capability == ModuleCapability.OAUTH2_CAPABLE) {
            return true;
        }
        return false;
    }

}
