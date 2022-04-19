package com.denesgarda.Socketeer.event;

import com.denesgarda.Socketeer.Connection;

/**
 * ClientDisconnectEvent is called when a client disconnects from the server
 * This event is only called by servers
 */
public class ClientDisconnectEvent extends ConnectionEvent {

    /**
     * The default constructor
     * @param connection The connection of the client that disconnected
     */
    public ClientDisconnectEvent(Connection connection) {
        super(connection);
    }
}
