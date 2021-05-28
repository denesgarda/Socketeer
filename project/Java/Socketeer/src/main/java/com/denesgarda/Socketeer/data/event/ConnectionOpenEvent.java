package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.Connection;

public class ConnectionOpenEvent extends ConnectionEvent {
    public ConnectionOpenEvent(Connection connection) {
        super(connection);
    }
}
