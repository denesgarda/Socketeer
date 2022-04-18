package com.denesgarda.Socketeer.event;

import com.denesgarda.Socketeer.Connection;

public class ClientConnectEvent extends ConnectionEvent {
    private final Connection connection;

    public ClientConnectEvent(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
