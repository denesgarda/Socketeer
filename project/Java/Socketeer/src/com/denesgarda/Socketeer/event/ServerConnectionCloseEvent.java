package com.denesgarda.Socketeer.event;

import com.denesgarda.Socketeer.Connection;

/**
 * ServerConnectionCloseEvent is called when a server closes the connection between it and a client
 * This event is only called by clients
 */
public class ServerConnectionCloseEvent extends ConnectionEvent {

    /**
     * The default constructor
     * @param connection The connection that was closed by the server
     */
    public ServerConnectionCloseEvent(Connection connection) {
        super(connection);
    }
}
