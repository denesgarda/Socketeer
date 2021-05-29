package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.Connection;

public class DisconnectEvent extends ConnectionEvent {
    public DisconnectEvent(Connection connection) {
        super(connection);
    }
}
