package com.denesgarda.Socketeer.event;

import com.denesgarda.Socketeer.Connection;

public class ReceiveEvent extends ConnectionEvent {
    private final String data;

    public ReceiveEvent(Connection connection, String data) {
        super(connection);
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
