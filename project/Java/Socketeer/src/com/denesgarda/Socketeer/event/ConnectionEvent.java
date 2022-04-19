package com.denesgarda.Socketeer.event;

import com.denesgarda.Socketeer.Connection;

/**
 * The superclass of all events that have to do with a conection
 */
public class ConnectionEvent extends Event {

    /**
     * The relevant connection of the event
     */
    private final Connection connection;

    /**
     * The default constructor
     * @param connection The relevant connection of the event
     */
    public ConnectionEvent(Connection connection) {
        this.connection = connection;
    }

    /**
     * Gets the relevant connection of the event
     * @return The connection
     */
    public Connection getConnection() {
        return connection;
    }
}
