package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.Connection;

public class ClientConnectedEvent extends Event {
    private final Connection connection;
    private final Connection.ConnectionType connectionType;

    public ClientConnectedEvent(Connection connection, Connection.ConnectionType connectionType) {
        this.connection = connection;
        this.connectionType = connectionType;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public Connection.ConnectionType getConnectionType() {
        return this.connectionType;
    }
}
