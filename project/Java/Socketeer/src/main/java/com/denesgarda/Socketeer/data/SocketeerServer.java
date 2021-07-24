package com.denesgarda.Socketeer.data;

import com.denesgarda.Socketeer.data.event.*;
import com.denesgarda.Socketeer.util.ArrayModification;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This is the Server object.
 * @author denesgarda
 */
public class SocketeerServer extends End {

    /**
     * The event listener of the server.
     */
    private Listener listener = new Listener() {};

    /**
     * If the server is listening or not.
     */
    private boolean listening = false;

    /**
     * The connection timeout of the server.
     * How long the server should wait for a client response before it registers it as disconnected.
     * @apiNote Not yet implemented
     */
    private int connectionTimeout = 10;

    /**
     * The connection throttle of the server.
     * The max amount of connections allowed on the server at one given time.
     */
    private int connectionThrottle = 50;

    /**
     * The addresses of all clients currently connected to the server.
     */
    public String[] openConnections = new String[]{};

    /**
     * The constructor of SocketeerServer.
     * @throws UnknownHostException
     */
    protected SocketeerServer() throws UnknownHostException {

    }

    /**
     * Set the event listener of the server.
     * @param listener The event listener to be set
     */
    public void setEventListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * Gets the connection timeout.
     * @return The connection timeout
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * Sets the connection timeout.
     * @param seconds The desired connection timeout time in seconds
     */
    public void setConnectionTimeout(int seconds) {
        connectionTimeout = seconds;
    }

    /**
     * Gets the connection throttle.
     * @return The connection throttle
     */
    public int getConnectionThrottle() {
        return connectionThrottle;
    }

    /**
     * Sets the connection throttle.
     * @param connections The desired amount of connections for the connection throttle
     */
    public void setConnectionThrottle(int connections) {
        this.connectionThrottle = connections;
    }

    /**
     * The method that starts the server.
     * This should only be called if the server is not already running.
     * @param port The port the server should be started on
     * @throws IOException
     */
    public void listen(int port) throws IOException {
        if(!listening) {
            listening = true;
            ServerSocket serverSocket = new ServerSocket(port);
            End THIS = this;
            new Runnable() {
                @Override
                public void run() {
                    while(listening) {
                        try {
                            Socket socket = serverSocket.accept();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                            Connection connection = new Connection(THIS, new End((((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","")), port, listener, socket);
                            Object read = objectInputStream.readObject();
                            if(connectionThrottle == 0 || openConnections.length < connectionThrottle) {
                                boolean oneTimeConnection = false;
                                openConnections = ArrayModification.append(openConnections, connection.getOtherEnd().getAddress());
                                if (read.equals("01101111 01101110 01100101 01010100 01101001 01101101 01100101 01000011 01101111 01101110 01101110 01100101 01100011 01110100 01101001 01101111 01101110")) {
                                    objectOutputStream.writeObject("01101111 01101011");
                                    Event.callEvent(listener, new ClientConnectedEvent(connection, Connection.ConnectionType.ONE_TIME_CONNECTION));
                                    oneTimeConnection = true;
                                } else {
                                    Event.callEvent(listener, new ReceivedEvent(connection) {
                                        @Override
                                        public Object read() throws Exception {
                                            return read;
                                        }

                                        @Override
                                        public void reply(Object object) throws Exception {
                                            objectOutputStream.writeObject(object);
                                        }
                                    });
                                }
                                if(oneTimeConnection) {
                                    connection.open = false;
                                    Event.callEvent(listener, new ClientDisconnectedEvent(connection));
                                    openConnections = ArrayModification.remove(openConnections, connection.getOtherEnd().getAddress());
                                }
                            }
                            else {
                                objectOutputStream.writeObject("Connection refused; connection throttle");
                            }
                            objectOutputStream.flush();
                            objectOutputStream.close();
                            objectInputStream.close();
                            socket.close();
                            connection.open = false;
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.run();
        }
        else {
            throw new IllegalStateException("Already listening");
        }
    }

    /**
     * The method that stops the server.
     * Should only be called if the server is running.
     */
    public void stopListening() {
        if(listening) {
            listening = false;
        }
        else {
            throw new IllegalStateException("Nothing to stop");
        }
    }

    /**
     * If the server is listening or not.
     * @return If the server is listening or not
     */
    public boolean isListening() {
        return listening;
    }
}
