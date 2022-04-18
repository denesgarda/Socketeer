package com.denesgarda.Socketeer.event;

import com.denesgarda.Socketeer.End;

public class ClientDisconnectEvent extends ConnectionEvent {
    private End client;

    public ClientDisconnectEvent(End client) {
        this.client = client;
    }

    public End getClient() {
        return client;
    }
}
