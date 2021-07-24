package com.denesgarda.Socketeer.data.lang;

public class RestrictedObjectException extends RuntimeException {
    public RestrictedObjectException() {
        super("This object is reserved for internal server-client communication and, therefore, is prohibited for use.");
    }
}
