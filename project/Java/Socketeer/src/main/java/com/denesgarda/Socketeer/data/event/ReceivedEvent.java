package com.denesgarda.Socketeer.data.event;

import com.denesgarda.Socketeer.data.Connection;

/**
 * This is the event that's fired when the server receives on object from a client.
 * It is only fired if the server does not recognize the message as the client trying to send internal commands to the server.
 * To prevent that from happening, if the user tries to send an object that is reserved for internal server and client communication, the system will throw RestrictedObjectException.
 * @author denesgarda
 */
public abstract class ReceivedEvent extends Event {
    /**
     * The connection used to send the object.
     */
    private final Connection connection;

    /**
     * The constructor of ReceivedEvent.
     * This should only be used within socketeer. Users should not initialize events, as to not cause unnecessary confusion and Exceptions.
     * @param connection The connection through which the object was sent
     */
    public ReceivedEvent(Connection connection) {
        this.connection = connection;
    }

    /**
     * Gets the connection.
     * @return The connection
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Read what the client sent to the server.
     * Can be cast to anything.
     * The server reads objects so that anything can be sent without many limits.
     * @return The object read form the ObjectInputStream
     * @throws Exception
     */
    public abstract Object read() throws Exception;

    /**
     * Send a reply to the client.
     * This method must be called when a client send on object.
     * Otherwise, an Exception will be thrown on the client's side as it expected a response and got nothing.
     * @param object The object to be sent to the client through the ObjectOutputStream
     * @throws Exception
     */
    public abstract void reply(Object object) throws Exception;
}
