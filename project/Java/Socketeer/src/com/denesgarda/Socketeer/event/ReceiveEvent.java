package com.denesgarda.Socketeer.event;

import com.denesgarda.Socketeer.Connection;

/**
 * ReceiveEvent is called when unanticipated data is received through the input stream reader of either a server or client
 * This event is called by both servers and clients
 */
public class ReceiveEvent extends ConnectionEvent {
    /**
     * The data received
     */
    private final String data;

    /**
     * The default constructor
     * @param connection The connection that the data was received through
     * @param data The data received
     */
    public ReceiveEvent(Connection connection, String data) {
        super(connection);
        this.data = data;
    }

    /**
     * Gets the data received
     * @return The data
     */
    public String getData() {
        return data;
    }
}
