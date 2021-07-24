package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.Connection;

public class ClientDisconnectedEvent extends Event {
    private final Connection connection;

    public ClientDisconnectedEvent(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
