package com.denesgarda.Socketeer.event;

import com.denesgarda.Socketeer.Connection;

public class ConnectionEvent extends Event {
    private final Connection connection;

    public ConnectionEvent(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
