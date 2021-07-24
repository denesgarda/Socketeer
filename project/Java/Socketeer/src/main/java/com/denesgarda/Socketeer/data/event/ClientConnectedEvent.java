package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.Connection;

/**
 * This is the event that's fired when a client successfully establishes a connection with the server
 * @author denesgarda
 */
public class ClientConnectedEvent extends Event {

    /**
     * The connection that is created
     */
    private final Connection connection;

    /**
     * The type of connection the client used
     */
    private final Connection.ConnectionType connectionType;

    /**
     * The constructor of ClientConnectedEvent
     * @param connection The newly created connection between the server and client
     * @param connectionType The type of connection the client used to connect to the server
     */
    public ClientConnectedEvent(Connection connection, Connection.ConnectionType connectionType) {
        this.connection = connection;
        this.connectionType = connectionType;
    }

    /**
     * Gets the connection
     * @return The connection
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Gets the connection type
     * @return The connection type
     */
    public Connection.ConnectionType getConnectionType() {
        return this.connectionType;
    }
}
