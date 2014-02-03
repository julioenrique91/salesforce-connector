
package org.mule.modules.salesforce.adapters;

import javax.annotation.Generated;
import org.mule.api.MetadataAware;
import org.mule.modules.salesforce.SalesforceOAuthConnector;


/**
 * A <code>SalesforceOAuthConnectorMetadataAdapater</code> is a wrapper around {@link SalesforceOAuthConnector } that adds support for querying metadata about the extension.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-cascade", date = "2014-02-03T12:06:26-06:00", comments = "Build UNNAMED.1791.ad9d188")
public class SalesforceOAuthConnectorMetadataAdapater
    extends SalesforceOAuthConnectorCapabilitiesAdapter
    implements MetadataAware
{

    private final static String MODULE_NAME = "Salesforce (OAuth)";
    private final static String MODULE_VERSION = "5.4.5-SNAPSHOT";
    private final static String DEVKIT_VERSION = "3.5.0-cascade";
    private final static String DEVKIT_BUILD = "UNNAMED.1791.ad9d188";
    private final static String MIN_MULE_VERSION = "3.5";

    public String getModuleName() {
        return MODULE_NAME;
    }

    public String getModuleVersion() {
        return MODULE_VERSION;
    }

    public String getDevkitVersion() {
        return DEVKIT_VERSION;
    }

    public String getDevkitBuild() {
        return DEVKIT_BUILD;
    }

    public String getMinMuleVersion() {
        return MIN_MULE_VERSION;
    }

}
