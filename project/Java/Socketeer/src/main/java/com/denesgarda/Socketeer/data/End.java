package com.denesgarda.Socketeer.data;

import com.denesgarda.Socketeer.data.event.*;

import java.io.*;
import java.net.*;
import java.util.TimerTask;

public class End {
    private String address;
    private Listener listener;
    private boolean listening = false;

    protected End() throws UnknownHostException {
        this.address = InetAddress.getLocalHost().getHostName();
    }

    private End(String address) throws UnknownHostException {
        this.address = address;
    }

    public void addEventListener(Listener listener) {
        this.listener = listener;
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
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Connection.sendThroughSocket(socket, "01101100 01101001 01110011 01110100 01100101 01101110 00100000 01110011 01110100 01100001 01110010 01110100");
        //connection.send("01101100 01101001 01110011 01110100 01100101 01101110 00100000 01110011 01110100 01100001 01110010 01110100");
        return new Connection(this, new End(address), port, listener, socket);
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
                        if (o.equals("01101100 01101001 01110011 01110100 01100101 01101110 00100000 01110011 01110100 01100001 01110010 01110100")) {
                            listener.event(new ConnectionEvent(connection));
                            listener.event(new ConnectionSuccessfulEvent(connection));
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
