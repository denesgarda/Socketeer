package com.denesgarda.Socketeer.data.lang;

public class ConnectionFailedException extends RuntimeException {
    public ConnectionFailedException() {
        super("The connection failed for an unknown reason.");
    }

    public ConnectionFailedException(String messageFromServer) {
        super("Message from server: " + messageFromServer);
    }
}
