package com.denesgarda.Socketeer.event;

import com.denesgarda.Socketeer.Connection;

public class ServerConnectionCloseEvent extends ConnectionEvent {
    public ServerConnectionCloseEvent(Connection connection) {
        super(connection);
    }
}
