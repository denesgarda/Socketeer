package com.denesgarda.Socketeer.event;

import com.denesgarda.Socketeer.Connection;

/**
 * ClientConnectEvent is called when a client connects to the server
 * This event is only called by servers
 */
public class ClientConnectEvent extends ConnectionEvent {

    /**
     * The default constructor
     * @param connection The connection of the client that connected
     */
    public ClientConnectEvent(Connection connection) {
        super(connection);
    }
}
