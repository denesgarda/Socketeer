package com.denesgarda.Socketeer.data.lang;

/**
 * This is the exception that's thrown when a client fails to connect to the server
 * @author denesgarda
 */
public class ConnectionFailedException extends RuntimeException {

    /**
     * The default constructor of ConnectionFailedException
     */
    public ConnectionFailedException() {
        super("The connection failed for an unknown reason.");
    }

    /**
     * The secondary constructor of ConnectionFailedException
     * @param messageFromServer The message that the server sent to the client with information about why the connection failed
     */
    public ConnectionFailedException(String messageFromServer) {
        super("Message from server: " + messageFromServer);
    }
}
