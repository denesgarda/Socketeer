package com.denesgarda.Socketeer.event;

import com.denesgarda.Socketeer.Connection;

public class ClientConnectedEvent extends ConnectionEvent {
    private final Connection connection;

    public ClientConnectedEvent(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return  connection;
    }
}
