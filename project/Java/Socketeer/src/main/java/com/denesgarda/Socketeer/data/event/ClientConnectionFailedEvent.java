package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.End;

/**
 * This is the event that's fired when a client attempts to connect to the server and gets rejected.
 * It must establish a connection first, to be able to reach the server.
 * If the server decides that the client must be rejected, it tells the client and calls this event.
 * @author denesgarda
 */
public class ClientConnectionFailedEvent extends Event {

    /**
     * The client that tried to connect
     */
    private final End end;

    /**
     * The constructor of ClientConnectionFailedEvent.
     * This should only be used within socketeer. Users should not initialize events, as to not cause unnecessary confusion and Exceptions.
     * @param end The client that tried to connect
     */
    public ClientConnectionFailedEvent(End end) {
        this.end = end;
    }

    /**
     * Gets the client that tried to connect.
     * @return The client that tried to connect
     */
    public End getEnd() {
        return end;
    }
}
