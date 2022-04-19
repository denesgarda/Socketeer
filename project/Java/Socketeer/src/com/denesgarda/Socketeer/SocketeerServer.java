package com.denesgarda.Socketeer;

import com.denesgarda.Socketeer.event.*;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

public abstract class SocketeerServer extends End {
    private int connectionThrottle = 50;
    private boolean listening = false;
    public Thread serverThread;
    private ServerSocket serverSocket;

    protected SocketeerServer() throws UnknownHostException {

    }

    public void setConnectionThrottle(int connectionThrottle) {
        if(connectionThrottle < 0) {
            throw new IndexOutOfBoundsException("Connection throttle cannot be negative");
        }
        else {
            this.connectionThrottle = connectionThrottle;
        }
    }

    public int getConnectionThrottle() {
        return connectionThrottle;
    }

    public LinkedList<Connection> getConnections() {
        return connections;
    }

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
                            onEvent(new ClientConnectEvent(connection));
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

    public void close() throws IOException {
        for (Connection connection : connections) {
            connection.close();
        }
    }

    public boolean isListening() {
        return listening;
    }
}
