package com.denesgarda.Socketeer.data;

import com.denesgarda.Socketeer.data.event.*;
import com.denesgarda.Socketeer.data.lang.ConnectionFailedException;
import com.denesgarda.Socketeer.util.ArrayModification;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class End {
    private final String address;
    private Listener listener = new Listener() {};
    private boolean listening = false;
    private int connectionTimeout = 10;
    private int connectionThrottle = 50;
    private String[] openConnections = new String[]{};

    protected End() throws UnknownHostException {
        this.address = InetAddress.getLocalHost().getHostName();
    }

    private End(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    public void setEventListener(Listener listener) {
        this.listener = listener;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        if(connectionTimeout < 1) {
            throw new IllegalStateException("Connection timeout cannot be less than 1 second");
        }
        this.connectionTimeout = connectionTimeout;
    }

    public int getConnectionThrottle() {
        return connectionThrottle;
    }

    public void setConnectionThrottle(int connectionThrottle) {
        if(connectionThrottle < 0) {
            throw new IllegalStateException("Connection throttle must be positive");
        }
        this.connectionThrottle = connectionThrottle;
    }

    public void connectOneTime(String address, int port, OneTimeAction oneTimeAction) throws Exception {
        Socket socket = new Socket(address, port);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream.writeObject("01101111 01101110 01100101 01010100 01101001 01101101 01100101 01000011 01101111 01101110 01101110 01100101 01100011 01110100 01101001 01101111 01101110");
        objectOutputStream.flush();
        Object reply = objectInputStream.readObject();
        objectOutputStream.close();
        objectInputStream.close();
        socket.close();
        if(reply.equals("01101111 01101011")) {
            Connection connection = new Connection(this, new End(address), port, listener, socket);
            oneTimeAction.action(connection);
            connection.open = false;

        }
        else {
            throw new ConnectionFailedException((String) reply);
        }
    }

    public void listen(int port) throws IOException {
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
                        if(connectionThrottle == 0) {
                            doConnection(connection, read, objectOutputStream);
                        }
                        else if(openConnections.length >= connectionThrottle) {
                            objectOutputStream.writeObject("Connection refused; connection throttle");
                        }
                        else {
                            doConnection(connection, read, objectOutputStream);
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

    public void stopListening() {
        listening = false;
    }

    private void doConnection(Connection connection, Object read, ObjectOutputStream objectOutputStream) throws IOException {
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
}
