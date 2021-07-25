package com.denesgarda.Socketeer.data;

import com.denesgarda.Socketeer.data.event.Listener;
import com.denesgarda.Socketeer.data.lang.ConnectionClosedException;
import com.denesgarda.Socketeer.data.lang.RestrictedObjectException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * The connection established between a server and a client.
 * Contains information about both ends of the server.
 * @author denesgarda
 */
public class Connection {

    /**
     * This end of the connection.
     */
    private End THIS;

    /**
     * The other end of the connection.
     */
    private End THAT;

    /**
     * The port the connection was opened on.
     */
    private int port;

    /**
     * The listener of the server.
     */
    private Listener listener;

    /**
     * The socket of the connection.
     */
    private Socket socket;

    /**
     * If the connection is open or closed.
     */
    protected boolean open = true;

    /**
     * The type of connection.
     */
    private final ConnectionType connectionType;

    /**
     * The constructor of Connection.
     * This should only be used within socketeer. Users should not manually construct connections, as to not cause unnecessary confusion and Exceptions.
     * @param THIS This end of the connection
     * @param THAT The other end of the connection
     * @param port The port the connection was opened on
     * @param listener The listener of the server
     * @param socket The socket of the connection
     * @param connectionType The type of connection
     */
    protected Connection(End THIS, End THAT, int port, Listener listener, Socket socket, ConnectionType connectionType) {
        this.THIS = THIS;
        this.THAT = THAT;
        this.port = port;
        this.listener = listener;
        this.socket = socket;
        this.connectionType = connectionType;
    }

    /**
     * Get the other end of the connection.
     * @return The other end of the connection
     */
    public End getOtherEnd() {
        return THAT;
    }

    /**
     * Gets the connection's connection type
     * @return The connection's connection type
     */
    public ConnectionType getConnectionType() {
        return connectionType;
    }

    /**
     * Sends a message to the server that the client is connected to.
     * The server cannot reply to a client using this method.
     * To reply, a server has to use the method in ReceivedEvent called reply(Object object).
     * @param object The object to be send to the server
     * @return The server's response
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Object sendToServer(Object object) throws IOException, ClassNotFoundException {
        if(open) {
            if (object.equals("01101111 01101110 01100101 01010100 01101001 01101101 01100101 01000011 01101111 01101110 01101110 01100101 01100011 01110100 01101001 01101111 01101110") || object.equals("01101111 01101011")) {
                throw new RestrictedObjectException();
            }
            else {
                this.socket = new Socket(THAT.getAddress(), this.port);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                objectOutputStream.writeObject(object);
                objectOutputStream.flush();
                Object reply = objectInputStream.readObject();
                objectOutputStream.close();
                objectInputStream.close();
                socket.close();
                socket = new Socket();
                return reply;
            }
        }
        else {
            throw new ConnectionClosedException();
        }
    }

    /**
     * Check if the connection is open or closed.
     * @return If the connection is open or closed
     */
    public boolean isOpen() {
        return this.open;
    }

    /**
     * Closes the connection if it's open.
     * Only safely closes connections.
     * If a ONE_TIME_CONNECTION or UNKNOWN type of connection is to be closed (not recommended), then use the kill() method.
     */
    public void close() {
        if(open) {
            if (connectionType == ConnectionType.STATIC) {
                open = false;
            }
            else {
                throw new IllegalStateException("Cannot close connections with type " + connectionType);
            }
        }
        else {
            throw new IllegalStateException("Connection is already closed");
        }
    }

    /**
     * Closes the connection if it's open, regardless of what type of connection it is.
     * Not recommended for use and may not always work as intended.
     */
    public void kill() {
        if(open) {
            open = false;
        }
        else {
            throw new IllegalStateException("Connection is already closed");
        }
    }

    /**
     * Connection types.
     * Connection types can be used to differentiate between was a client tries to communicate with the server.
     */
    public enum ConnectionType {

        /**
         * A one-time connection.
         * A client can establish a one-time connection with the server using the connectOneTime method in SocketeerClient.
         * A one-time connection allows the client to connect to the server, execute a set a predetermined tasks, then immediately disconnect without keeping the connection open for longer than absolutely necessary.
         */
        ONE_TIME_CONNECTION,

        /**
         * A static connection that stays open until closed or killed
         * @apiNote Not yet implemented
         */
        STATIC,

        /**
         * An unknown type of connection.
         * The server cannot always determine what kind of connection a client is using, so it'll contain this.
         */
        UNKNOWN
    }
}
