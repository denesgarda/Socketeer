package com.denesgarda.Socketeer.event;

import com.denesgarda.Socketeer.Connection;
import com.denesgarda.Socketeer.End;

public class ClientDisconnectEvent extends ConnectionEvent {
    public ClientDisconnectEvent(Connection connection) {
        super(connection);
    }
}
