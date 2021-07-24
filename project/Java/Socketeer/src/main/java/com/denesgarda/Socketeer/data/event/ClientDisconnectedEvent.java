package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.Connection;

/**
 * This is the event that's fired when a client disconnects from the server
 * @author denesgarda
 */
public class ClientDisconnectedEvent extends Event {
    /**
     * The already closed connection that was used
     */
    private final Connection connection;

    /**
     * The constructor of ClientDisconnectedEvent
     * @param connection The connection that was used when the client was still connected to the server
     */
    public ClientDisconnectedEvent(Connection connection) {
        this.connection = connection;
    }

    /**
     * Gets the connection
     * @return The connection
     */
    public Connection getConnection() {
        return this.connection;
    }
}
