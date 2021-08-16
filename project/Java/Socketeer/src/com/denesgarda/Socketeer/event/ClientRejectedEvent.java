package com.denesgarda.Socketeer.event;

import com.denesgarda.Socketeer.Connection;

public class ClientRejectedEvent extends ConnectionEvent {
    private final Connection connection;
    private final Reason reason;

    public ClientRejectedEvent(Connection connection, Reason reason) {
        this.connection = connection;
        this.reason = reason;
    }

    public Connection getConnection() {
        return connection;
    }

    public Reason getReason() {
        return reason;
    }

    public enum Reason {
        CONNECTION_THROTTLE
    }
}
