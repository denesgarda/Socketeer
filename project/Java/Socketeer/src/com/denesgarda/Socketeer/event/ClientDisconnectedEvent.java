package com.denesgarda.Socketeer.event;

import com.denesgarda.Socketeer.Connection;

public class ClientDisconnectedEvent extends ConnectionEvent {
    private final Connection connection;

    public ClientDisconnectedEvent(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return  connection;
    }
}
