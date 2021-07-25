package com.denesgarda.Socketeer.data.lang;

/**
 * This is the exception that's thrown when an object is trying to be send through a closed connection.
 * @author denesgarda
 */
public class ConnectionClosedException extends RuntimeException {

    /**
     * The constructor of ConnectionClosedException.
     */
    public ConnectionClosedException() {
        super("Connection is closed");
    }
}
