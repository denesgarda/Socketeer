package com.denesgarda.Socketeer;

import com.denesgarda.Socketeer.event.*;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class SocketeerServer extends End {
    private int connectionThrottle = 50;
    private boolean listening = false;

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

    public LinkedList<Connection> getPendingConnections() {
        return pendingConnections;
    }

    public LinkedList<Connection> getConnections() {
        return connections;
    }

    public void listen(int port) {
        SocketeerServer THIS = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!listening) {
                        listening = true;
                        ServerSocket serverSocket = new ServerSocket(port);
                        while (listening) {
                            Socket accept = serverSocket.accept();
                            String otherAddress = ((InetSocketAddress) accept.getRemoteSocketAddress()).getAddress().toString().replace("/", "");
                            final BufferedReader in = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                            final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(accept.getOutputStream()));
                            Connection connection = new Connection(THIS, new End(otherAddress), accept, in, out);
                            pendingConnections.add(connection);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        while (!accept.isClosed()) {
                                            String incoming = read();
                                            if (incoming != null) {
                                                if (pendingConnections.contains(connection)) {
                                                    if(!End.VERSION.equals(incoming)) {
                                                        write("version: " + End.VERSION);
                                                        connection.close();
                                                        Event.callEvent(eventListener, new ClientRejectedEvent(connection, ClientRejectedEvent.Reason.INCOMPATIBLE_VERSION));
                                                    }
                                                    else if (connectionThrottle == 0 || connections.size() < connectionThrottle) {
                                                        write("code: 0");
                                                        pendingConnections.remove(connection);
                                                        connections.add(connection);
                                                        Event.callEvent(eventListener, new ClientConnectedEvent(connection));
                                                    } else {
                                                        write("code: 1");
                                                        connection.close();
                                                        Event.callEvent(eventListener, new ClientRejectedEvent(connection, ClientRejectedEvent.Reason.CONNECTION_THROTTLE));
                                                    }
                                                } else if (connections.contains(connection)) {
                                                    Event.callEvent(eventListener, new ReceivedEvent(connection, incoming));
                                                }
                                            } else {
                                                connection.close();
                                                Event.callEvent(eventListener, new ClientDisconnectedEvent(connection));
                                            }
                                        }
                                    } catch (SocketException e) {
                                        try {
                                            connection.close();
                                        } catch (IOException ioException) {
                                            ioException.printStackTrace();
                                        }
                                        Event.callEvent(eventListener, new ClientDisconnectedEvent(connection));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                private String read() throws IOException {
                                    return in.readLine();
                                }

                                private void write(String content) throws IOException {
                                    out.write(content);
                                    out.newLine();
                                    out.flush();
                                }
                            }).start();
                        }
                    } else {
                        throw new IllegalStateException("Already listening");
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stopListening() {
        if(listening) {
            listening = false;
        }
        else {
            throw new IllegalStateException("Nothing to stop");
        }
    }

    public boolean isListening() {
        return listening;
    }
}
