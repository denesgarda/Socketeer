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

public class SocketeerServer extends End {
    private Listener listener = new Listener() {};
    private boolean listening = false;
    private int connectionTimeout = 10;
    private int connectionThrottle = 50;
    private String[] openConnections = new String[]{};

    protected SocketeerServer() throws UnknownHostException {

    }

    public void setEventListener(Listener listener) {
        this.listener = listener;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int seconds) {
        connectionTimeout = seconds;
    }

    public int getConnectionThrottle() {
        return connectionThrottle;
    }

    public void setConnectionThrottle(int connections) {
        this.connectionThrottle = connections;
    }

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

    public void stopListening() {
        listening = false;
    }

    public boolean isListening() {
        return listening;
    }

    public String[] getOpenConnections() {
        return openConnections;
    }
}
