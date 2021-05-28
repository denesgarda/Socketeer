package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.Connection;

public class ConnectionEvent extends Event {
    private Connection connection;

    public ConnectionEvent(Connection connection) {
        super();
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
