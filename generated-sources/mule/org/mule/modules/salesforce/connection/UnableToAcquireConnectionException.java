
package org.mule.modules.salesforce.connection;

import javax.annotation.Generated;


/**
 * Exception thrown when the connection needed for executing an
 *  operation is null.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-cascade", date = "2014-01-13T09:19:38-06:00", comments = "Build UNNAMED.1791.ad9d188")
public class UnableToAcquireConnectionException
    extends Exception
{

    /**
     * Create a new exception
     */
    public UnableToAcquireConnectionException() {
    }

    /**
     * Create a new exception
     */
    public UnableToAcquireConnectionException(String message) {
        super(message);
    }

    /**
     * Create a new exception
     *
     * @param throwable Inner exception
     */
    public UnableToAcquireConnectionException(Throwable throwable) {
        super(throwable);
    }
}
