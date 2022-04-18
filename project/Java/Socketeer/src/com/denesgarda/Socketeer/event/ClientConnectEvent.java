package com.denesgarda.Socketeer.event;

import com.denesgarda.Socketeer.Connection;

public class ClientConnectEvent extends ConnectionEvent {
    public ClientConnectEvent(Connection connection) {
        super(connection);
    }
}
