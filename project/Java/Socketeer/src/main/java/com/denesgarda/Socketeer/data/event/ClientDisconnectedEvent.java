package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.Connection;

/**
 * This is the event that's fired when a client disconnects from the server.
 * Once the client formally disconnects or a client hangs for the set connection timeout time of the server, the server will register it as disconnected and fire this event.
 * @author denesgarda
 */
public class ClientDisconnectedEvent extends Event {

    /**
     * The already closed connection that was used for server and client communication.
     */
    private final Connection connection;

    /**
     * The constructor of ClientDisconnectedEvent.
     * This should only be used within socketeer. Users should not initialize events, as to not cause unnecessary confusion and Exceptions.
     * @param connection The connection that was used when the client was still connected to the server
     */
    public ClientDisconnectedEvent(Connection connection) {
        this.connection = connection;
    }

    /**
     * Gets the connection.
     * @return The connection
     */
    public Connection getConnection() {
        return this.connection;
    }
}
