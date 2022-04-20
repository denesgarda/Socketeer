package com.denesgarda.Socketeer;

import com.denesgarda.Socketeer.event.ClientDisconnectEvent;
import com.denesgarda.Socketeer.event.ReceiveEvent;
import com.denesgarda.Socketeer.event.ServerConnectionCloseEvent;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

/**
 * The Connection class holds the information of a connection between a server and a client
 */
public class Connection {

    /**
     * The local end of the connection
     */
    private final End THIS;

    /**
     * The other end of the connection
     */
    private final End THAT;

    /**
     * The socket that is used for communication between the server and client
     */
    private final Socket socket;

    /**
     * The input stream reader for the socket
     */
    private DataInputStream in;

    /**
     * The output stream writer for the socket
     */
    private DataOutputStream out;

    /**
     * Each connection has its own thread that handles all incoming and outgoing requests
     */
    public Thread connectionThread;

    /**
     * A queue of methods waiting to read from the input stream reader and override a ReceiveEvent
     */
    private LinkedList<Queueable> queue = new LinkedList<>();

    /**
     * The connection constructor
     * Should only be used by Socketeer
     * @param THIS The local end of the connection
     * @param THAT The other end of the connection
     * @param socket The socket for communication
     * @param in The input stream reader
     * @param out The output stream writer
     */
    protected Connection(End THIS, End THAT, Socket socket, DataInputStream in, DataOutputStream out) {
        this.THIS = THIS;
        this.THAT = THAT;
        this.socket = socket;
        this.in = in;
        this.out = out;
        connectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!socket.isClosed()) {
                    try {
                        String data = in.readUTF();
                        if (queue.size() > 0) {
                            queue.get(0).nextIn(data);
                            queue.remove(queue.get(0));
                        } else {
                            THIS.onEvent(new ReceiveEvent(Connection.this, data));
                        }
                    } catch (IOException e) {
                        if (THIS instanceof SocketeerServer) {
                            THIS.onEvent(new ClientDisconnectEvent(Connection.this));
                        }
                        if (THIS instanceof SocketeerClient) {
                            THIS.onEvent(new ServerConnectionCloseEvent(Connection.this));
                        }
                        try {
                            close();
                        } catch (IOException ex) {
                           ex.printStackTrace();
                        }
                    }
                }
            }
        });
        connectionThread.start();
    }

    /**
     * Sends data to the other end of the connection
     * @param data The message to be sent
     * @throws IOException If the message fails to send
     */
    public void send(String data) throws IOException {
        out.writeUTF(data);
        out.flush();
    }

    /**
     * Adds a queueable to the queue to wait for the next input read by the input stream reader
     * @param queueable The queueable to be added to the queue
     */
    public void nextIn(Queueable queueable) {
        queue.add(queueable);
    }

    /**
     * Returns the next response in the queue while pausing the current thread
     * @return The next response
     */
    public String nextResponse() {
        final String[] response = new String[1];
        nextIn(new Queueable() {
            @Override
            public void nextIn(String data) throws IOException {
                response[0] = data;
            }
        });
        while (response[0] == null) {

        }
        return response[0];
    }

    /**
     * Returns the immediate response in the queue while pausing the current thread
     * @return The immediate response
     */
    public String awaitResponse() {
        final String[] response = new String[1];
        queue.addFirst(new Queueable() {
            @Override
            public void nextIn(String data) throws IOException {
                response[0] = data;
            }
        });
        while (response[0] == null) {

        }
        return response[0];
    }

    /**
     * Gets the local end of the connection
     * @return The local end
     */
    public End getLocalEnd() {
        return THIS;
    }

    /**
     * Gets the other end of the connection
     * @return The other end
     */
    public End getOtherEnd() {
        return THAT;
    }

    /**
     * Gets the socket used for communication between the two ends of the connection
     * @return The socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Closes the connection and stops the connection thread from running
     * @throws IOException If the socket fails to be closed
     */
    public void close() throws IOException {
        socket.close();
        connectionThread.interrupt();
        THIS.connections.remove(this);
    }

    /**
     * Checks if the connection is open
     * @return The status of the connection
     */
    public boolean isClosed() {
        return socket.isClosed();
    }
}
