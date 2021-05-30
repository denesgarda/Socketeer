package com.denesgarda.Socketeer.data;

import com.denesgarda.Socketeer.data.event.*;
import com.denesgarda.Socketeer.util.ArrayModification;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class End {
    private String address;
    private Listener listener;
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

    public void addEventListener(Listener listener) {
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
        this.listener = new Listener() {
            @Override
            public void event(Event event) {

            }
        };
    }

    public Connection connect(String address, int port) throws IOException {
        if(listener == null) this.voidListener();
        Socket socket = new Socket(address, port);
        Connection.sendThroughSocket(socket, "01101011 01100101 01100101 01110000");
        Connection connection = new Connection(this, new End(address), port, listener, socket);
        Timer timer = new Timer();
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
        timer.scheduleAtFixedRate(timerTask, 5000, 5000);
        return connection;
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
                    Connection connection = new Connection(THIS, new End((((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","")), port, listener, socket);
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
                                            listener.event(new DisconnectEvent(connection));
                                        }
                                    });
                                    timeout.startTimer();
                                    break;
                                }
                            }
                            if(!contains) {
                                listener.event(new ConnectionAttemptEvent(connection));
                                if(connectionThrottle != 0 && connections.length + 1 > connectionThrottle) {
                                    listener.event(new ConnectionFailedEvent(connection));
                                    //send back failed message
                                }
                                else {
                                    Timeout timeout = new Timeout(connection.getOtherEnd().getAddress());
                                    timeout.setDelay(connectionTimeout * 1000);
                                    timeout.setTimerTask(new TimerTask() {
                                        @Override
                                        public void run() {
                                            connections = ArrayModification.remove(connections, timeout);
                                            listener.event(new DisconnectEvent(connection));
                                        }
                                    });
                                    connections = ArrayModification.append(connections, timeout);
                                    listener.event(new ConnectionSuccessfulEvent(connection));
                                }
                            }
                        }
                        else {
                            listener.event(new ReceivedEvent(connection, o));
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
