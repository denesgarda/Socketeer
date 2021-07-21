package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.Connection;

public class ConnectionFailedEvent extends ConnectionEvent {
    public ConnectionFailedEvent(Connection connection) {
        super(connection);
    }
}
