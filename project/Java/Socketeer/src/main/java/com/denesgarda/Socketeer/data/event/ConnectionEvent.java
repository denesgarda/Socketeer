package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.Connection;
import org.jetbrains.annotations.NotNull;

public class ConnectionEvent extends Event {
    Connection connection;

    public ConnectionEvent(@NotNull Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
