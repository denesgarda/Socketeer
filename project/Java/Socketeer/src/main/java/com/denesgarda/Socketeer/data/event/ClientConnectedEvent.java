package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.Connection;

/**
 * This is the event that's fired when a client successfully establishes a connection with the server.
 * If the connection is blocked because of a connection throttle for example, this event will not fire.
 * It will only work if the server recognizes the client and OKs the connection.
 * @author denesgarda
 */
public class ClientConnectedEvent extends ConnectionEvent {

    /**
     * The connection that is created when the client and the server connect.
     */
    private final Connection connection;

    /**
     * The type of connection the client used to connect to the server.
     */
    private final Connection.ConnectionType connectionType;

    /**
     * The constructor of ClientConnectedEvent.
     * This should only be used within socketeer. Users should not initialize events, as to not cause unnecessary confusion and Exceptions.
     * @param connection The newly created connection between the server and client
     * @param connectionType The type of connection the client used to connect to the server
     */
    public ClientConnectedEvent(Connection connection, Connection.ConnectionType connectionType) {
        this.connection = connection;
        this.connectionType = connectionType;
    }

    /**
     * Gets the connection.
     * @return The connection
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Gets the connection type.
     * @return The connection type
     */
    public Connection.ConnectionType getConnectionType() {
        return this.connectionType;
    }
}
