package com.denesgarda.Socketeer;

import com.denesgarda.Socketeer.event.*;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

/**
 * The SocketeerServer class is the superclass of manually created servers
 */
public abstract class SocketeerServer extends End {

    /**
     * The server's connection throttle
     * If number of clients connected to the server is equal to the connection throttle, other clients will not be allowed to connect
     * The default value, if not changed, is 50
     */
    private int connectionThrottle = 50;

    /**
     * The status of the server
     */
    private boolean listening = false;

    /**
     * The server has a thread that continuously accepts new client connections
     */
    public Thread serverThread;

    /**
     * The server socket that accepts connections
     */
    private ServerSocket serverSocket;

    /**
     * The default constructor that must be declared
     * @throws UnknownHostException If Java cannot get the current address
     */
    protected SocketeerServer() throws UnknownHostException {

    }

    /**
     * Sets the connection throttle for the server
     * @param connectionThrottle The maximum number of allowed connections
     */
    public void setConnectionThrottle(int connectionThrottle) {
        if(connectionThrottle < 0) {
            throw new IndexOutOfBoundsException("Connection throttle cannot be negative");
        }
        else {
            this.connectionThrottle = connectionThrottle;
        }
    }

    /**
     * Gets the connection throttle
     * @return The maximum number of allowed connections
     */
    public int getConnectionThrottle() {
        return connectionThrottle;
    }

    /**
     * Starts the server on a certain port
     * @param port The port that the server is to be started on
     */
    public void listen(int port) {
        if (listening) {
            throw new IllegalStateException("Already listening");
        } else {
            listening = true;
            serverThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        serverSocket = new ServerSocket(port);
                        while (listening) {
                            if (connectionThrottle != 0) {
                                if (connections.size() >= connectionThrottle) {
                                    continue;
                                }
                            }
                            Socket socket = serverSocket.accept();
                            String otherAddress = ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().toString().replace("/", "");
                            DataInputStream in = new DataInputStream(socket.getInputStream());
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                            Connection connection = new Connection(SocketeerServer.this, new End(otherAddress) {
                                @Override
                                public void onEvent(Event event) {

                                }
                            }, socket, in, out);
                            connections.add(connection);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(buffer);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    onEvent(new ClientConnectEvent(connection));
                                }
                            }).start();
                        }
                    } catch (SocketException e) {
                        if (listening) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            serverThread.start();
        }
    }

    /**
     * Stops the server thread from accepting new client connections
     * @throws IOException If the server socket fails to be closed
     */
    public void stopListening() throws IOException {
        if(listening) {
            serverSocket.close();
            listening = false;
            serverThread.interrupt();
        }
        else {
            throw new IllegalStateException("Nothing to stop");
        }
    }

    /**
     * Stops all the operations of the server
     * Stops the server thread from accepting new client connection
     * Closes all current connections
     * @throws IOException If the server socket or the connections fail to close
     */
    public void stop() throws IOException {
        if(listening) {
            serverSocket.close();
            listening = false;
            serverThread.interrupt();
            for (Connection connection : connections) {
                connection.close();
            }
        }
        else {
            throw new IllegalStateException("Nothing to stop");
        }
    }

    /**
     * Checks if the server is currently accepting new client connections
     * @return The status of the server
     */
    public boolean isListening() {
        return listening;
    }
}
