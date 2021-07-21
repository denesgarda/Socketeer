package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.Connection;

public class ConnectionSuccessfulEvent extends ConnectionEvent {
    public ConnectionSuccessfulEvent(Connection connection) {
        super(connection);
    }
}
