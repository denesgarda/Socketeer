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
    private Socket socket;

    protected Connection(End THIS, End THAT, int port, Listener listener, Socket socket) {
        this.THIS = THIS;
        this.THAT = THAT;
        this.port = port;
        this.listener = listener;
        this.socket = socket;
    }

    public void send(Object object) throws IOException {
        /*this.socket = new Socket(THAT.getAddress(), this.port);
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        outputStream.close();
        objectOutputStream.close();*/
        try (Socket socket = new Socket(THAT.getAddress(), this.port);
             OutputStream os = socket.getOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(object);
        }
    }

    protected static void sendThroughSocket(Socket socket, Object object) throws IOException {
        new ObjectOutputStream(socket.getOutputStream()).writeObject(object);
    }

    public End getOtherEnd() {
        return this.THAT;
    }
}
