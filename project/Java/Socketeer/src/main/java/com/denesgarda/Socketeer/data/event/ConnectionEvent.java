package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.Connection;

public class ConnectionEvent extends Event {
    Connection connection;

    public ConnectionEvent(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
