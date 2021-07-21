package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.Connection;

public class ConnectionAttemptEvent extends ConnectionEvent {
    public ConnectionAttemptEvent(Connection connection) {
        super(connection);
    }
}
