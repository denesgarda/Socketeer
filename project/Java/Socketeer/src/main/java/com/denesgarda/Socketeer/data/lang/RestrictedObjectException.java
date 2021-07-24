package com.denesgarda.Socketeer.data.lang;

/**
 * This is the exception that's thrown when something that is not allowed for use gets sent through a connection.
 * @author denesgarda
 */
public class RestrictedObjectException extends RuntimeException {
    
    /**
     * The constructor of RestrictedObjectException.
     */
    public RestrictedObjectException() {
        super("This object is reserved for internal server-client communication and, therefore, is prohibited for use.");
    }
}
