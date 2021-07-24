package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.Connection;

/**
 * This is the event that's fired when the server receives on object from a client
 * @author denesgarda
 */
public abstract class ReceivedEvent extends Event {
    /**
     * The connection used
     */
    private final Connection connection;

    /**
     * The constructor of ReceivedEvent
     * @param connection The connection through which the object was sent
     */
    public ReceivedEvent(Connection connection) {
        this.connection = connection;
    }

    /**
     * Gets the connection
     * @return The connection
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Read what the client sent
     * @return The object read form the ObjectInputStream
     * @throws Exception
     */
    public abstract Object read() throws Exception;

    /**
     * Reply to the client
     * @param object The object to be sent to the client through the ObjectOutputStream
     * @throws Exception
     */
    public abstract void reply(Object object) throws Exception;
}
