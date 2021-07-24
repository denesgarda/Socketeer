package com.denesgarda.Socketeer.data.lang;

/**
 * This is the exception that's thrown when something that is not allowed for use get's send through a connection
 * @author denesgarda
 */
public class RestrictedObjectException extends RuntimeException {
    
    /**
     * The default constructor of RestrictedObjectException
     */
    public RestrictedObjectException() {
        super("This object is reserved for internal server-client communication and, therefore, is prohibited for use.");
    }
}
