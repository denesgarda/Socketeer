package com.denesgarda.Socketeer.data;

import com.denesgarda.Prop4j.data.PropertiesFile;
import com.denesgarda.Socketeer.data.event.Listener;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Connection {
    private End THIS;
    private End THAT;
    private int port;
    private Listener listener;

    protected Connection(End THIS, End THAT, int port, Listener listener) {
        this.THIS = THIS;
        this.THAT = THAT;
        this.port = port;
        this.listener = listener;
    }

    public void send(Object object) throws IOException {
        Socket socket = new Socket(THAT.getAddress(), this.port);
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        socket.close();
    }

    public End getOtherEnd() {
        return this.THAT;
    }
}
