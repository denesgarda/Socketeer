package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.End;

/**
 * This is the event that's fired when a client attempts a connection.
 * If the client is able to connect with the server, even if the server rejects the connection, this event will still fire.
 * @author denesgarda
 */
public class ClientConnectionAttemptedEvent extends ConnectionEvent {

    /**
     * The client that is trying to connect
     */
    private final End end;

    /**
     * The constructor of ClientConnectionAttemptedEvent.
     * This should only be used within socketeer. Users should not initialize events, as to not cause unnecessary confusion and Exceptions.
     * @param end The client that is trying to connect
     */
    public ClientConnectionAttemptedEvent(End end) {
        this.end = end;
    }

    /**
     * Gets the client that is trying to connect.
     * @return The client that is trying to connect
     */
    public End getClient() {
        return end;
    }
}
