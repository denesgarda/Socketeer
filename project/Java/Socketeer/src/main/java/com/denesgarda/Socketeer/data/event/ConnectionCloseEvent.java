package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.Connection;

public class ConnectionCloseEvent extends ConnectionEvent {
    public ConnectionCloseEvent(Connection connection) {
        super(connection);
    }
}
