
package org.mule.modules.salesforce.processors;

import java.lang.reflect.Type;
import javax.annotation.Generated;


/**
 * Interface used to unify all message processors (those which use (or not) pagination) from the ManagedConnectionProcessInterceptor
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-SNAPSHOT", date = "2014-04-11T04:37:26-05:00", comments = "Build master.1913.fb52000")
public interface ConnectivityProcessor {


    public Object getUsername();

    public Object getPassword();

    public Object getSecurityToken();

    public Object getUrl();

    public Object getProxyHost();

    public Object getProxyPort();

    public Object getProxyUsername();

    public Object getProxyPassword();

    public Object getSessionId();

    public Object getServiceEndpoint();

    /**
     * Retrieves the concrete java.lang.reflect.Type of a connectivity argument, needed for the @Connect
     * 
     * @param fieldName
     *     Name of the field to look for
     * @return
     *     The {@link java.lang.reflect.Type} associated with the field {@code fieldName}
     * @throws NoSuchFieldException
     *     Thrown when the {@code fieldName} is not a field from the current class
     */
    public Type typeFor(String fieldName)
        throws NoSuchFieldException
    ;

}
