package com.denesgarda.Socketeer.data;

import com.denesgarda.Socketeer.data.event.*;
import com.denesgarda.Socketeer.util.ArrayModification;
import com.denesgarda.Socketeer.util.Events;

import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class End {
    private String address;
    private End listener;
    private boolean listening = false;
    private Timeout[] connections = new Timeout[]{};
    private int connectionTimeout = 10;
    private int connectionThrottle = 50;

    protected End() throws UnknownHostException {
        this.address = InetAddress.getLocalHost().getHostName();
    }

    private End(String address) throws UnknownHostException {
        this.address = address;
    }

    public void addEventListener(End listener) {
        this.listener = listener;
    }

    public void setConnectionTimeout(int seconds) {
        if(seconds <= 5) throw new IndexOutOfBoundsException("Connection timeout must be at least 6 seconds.");
        else this.connectionTimeout = seconds;
    }
    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }
    public void resetDefaultConnectionTimeout() {
        this.connectionTimeout = 10;
    }

    public void setConnectionThrottle(int connections) {
        if(connections < 0) throw new IndexOutOfBoundsException("Cannot have negative connection throttle.");
        else this.connectionThrottle = connections;
    }
    public int getConnectionThrottle() {
        return this.connectionThrottle;
    }
    public void resetDefaultConnectionThrottle() {
        this.connectionThrottle = 50;
    }

    private void voidListener() {
        this.listener = this;
    }

    public Connection connect(String address, int port) throws IOException {
        if(listener == null) this.voidListener();
        Socket socket = new Socket(address, port);
        Connection.sendThroughSocket(socket, "01101011 01100101 01100101 01110000");
        Timer keeper = new Timer();
        Connection connection = new Connection(this, new End(address), port, listener, socket, keeper);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    connection.overrideSend("01101011 01100101 01100101 01110000");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        keeper.scheduleAtFixedRate(timerTask, 0, 5000);
        return connection;
    }

    public void kill(Connection connection) throws IOException {
        connection.close();
    }

    public void listen(int port) throws IOException {
        listening = true;
        if(listener == null) this.voidListener();
        ServerSocket serverSocket = new ServerSocket(port);
        End THIS = this;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    Socket socket = serverSocket.accept();
                    socket.setSoTimeout(10000);
                    Connection connection = new Connection(THIS, new End((((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","")), port, listener, socket, new Timer());
                    try {
                        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        Object o = objectInputStream.readObject();
                        if(o.equals("01101011 01100101 01100101 01110000")) {
                            boolean contains = false;
                            for (Timeout timeout : connections) {
                                if (timeout.getAddress().equals(connection.getOtherEnd().getAddress())) {
                                    contains = true;
                                    timeout.setTimerTask(new TimerTask() {
                                        @Override
                                        public void run() {
                                            connections = ArrayModification.remove(connections, timeout);
                                            Events.callEvent(listener, new DisconnectEvent(connection));
                                        }
                                    });
                                    timeout.startTimer();
                                    break;
                                }
                            }
                            if(!contains) {
                                Events.callEvent(listener, new ConnectionAttemptEvent(connection));
                                if(connectionThrottle != 0 && connections.length + 1 > connectionThrottle) {
                                    Events.callEvent(listener, new ConnectionFailedEvent(connection));
                                    //send back failed message
                                }
                                else {
                                    Timeout timeout = new Timeout(connection.getOtherEnd().getAddress());
                                    timeout.setDelay(connectionTimeout * 1000);
                                    timeout.setTimerTask(new TimerTask() {
                                        @Override
                                        public void run() {
                                            connections = ArrayModification.remove(connections, timeout);
                                            Events.callEvent(listener, new DisconnectEvent(connection));
                                        }
                                    });
                                    connections = ArrayModification.append(connections, timeout);
                                    Events.callEvent(listener, new ConnectionSuccessfulEvent(connection));
                                }
                            }
                        }
                        else {
                            Events.callEvent(listener, new ReceivedEvent(connection, o));
                        }
                        objectInputStream.close();
                        socket.close();
                    }
                    catch(EOFException e) {
                        e.printStackTrace();
                    }
                    if(listening) this.run();
                    else serverSocket.close();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timerTask.run();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                THIS.stopListening();
            }
        });
    }

    public void stopListening() {
        listening = false;
    }

    public String getAddress() {
        return this.address;
    }
}
