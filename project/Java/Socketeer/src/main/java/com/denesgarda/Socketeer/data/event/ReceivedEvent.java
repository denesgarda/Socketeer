package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.Connection;

import java.util.Objects;

public class ReceivedEvent extends Event {
    private final Connection connection;
    private final Object object;

    public ReceivedEvent(Connection connection, Object object) {
        this.connection = connection;
        this.object = object;
    }

    public Connection getConnection() {
        return connection;
    }
    public Object getObject() {
        return object;
    }
}
