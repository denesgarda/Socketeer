package com.denesgarda.Socketeer.data;

import com.denesgarda.Socketeer.data.event.ConnectionEvent;
import com.denesgarda.Socketeer.data.event.Event;
import com.denesgarda.Socketeer.data.event.Listener;
import com.denesgarda.Socketeer.data.event.ReceivedEvent;

import java.io.*;
import java.net.*;
import java.util.TimerTask;

public class End {
    private String address;
    private Listener listener;

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
        return new Connection(this, new End(address), port, listener);
    }

    public void listen(int port) throws IOException {
        if(listener == null) this.voidListener();
        ServerSocket serverSocket = new ServerSocket(port);
        End THIS = this;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    Socket socket = serverSocket.accept();
                    Connection connection = new Connection(THIS, new End((((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","")), port, listener);
                    listener.event(new ConnectionEvent(connection));
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    Object o = objectInputStream.readObject();
                    listener.event(new ReceivedEvent(connection, o));
                    socket.close();
                    this.run();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timerTask.run();
    }

    public String getAddress() {
        return this.address;
    }
}
