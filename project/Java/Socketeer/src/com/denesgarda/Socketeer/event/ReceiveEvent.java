package com.denesgarda.Socketeer.event;

import com.denesgarda.Socketeer.Connection;

public class ReceiveEvent extends Event {
    private final Connection connection;
    private final String data;

    public ReceiveEvent(Connection connection, String data) {
        this.connection = connection;
        this.data = data;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getData() {
        return data;
    }
}
