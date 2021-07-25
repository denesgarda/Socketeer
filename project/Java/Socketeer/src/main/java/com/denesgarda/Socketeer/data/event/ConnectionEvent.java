package com.denesgarda.Socketeer.data.event;

/**
 * The superclass of all event relating to connections.
 * @author denesgarda
 */
public class ConnectionEvent extends Event {

    /**
     * The constructor of ConnectionEvent.
     * This should only be used within socketeer. Users should not initialize events, as to not cause unnecessary confusion and Exceptions.
     */
    public ConnectionEvent() {
        super();
    }
}
