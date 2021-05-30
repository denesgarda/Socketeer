package com.denesgarda.Socketeer.data;

import com.denesgarda.Socketeer.data.event.Listener;
import com.denesgarda.Socketeer.data.lang.RestrictedObjectException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Connection {
    private End THIS;
    private End THAT;
    private int port;
    private Listener listener;
    private Socket socket;
    private Timer ping;

    protected Connection(End THIS, End THAT, int port, Listener listener, Socket socket, Timer ping) {
        this.THIS = THIS;
        this.THAT = THAT;
        this.port = port;
        this.listener = listener;
        this.socket = socket;
        this.ping = ping;
    }

    public void send(Object object) throws IOException {
        if(object.equals("01101011 01100101 01100101 01110000")) throw new RestrictedObjectException();
        else try (Socket socket = new Socket(THAT.getAddress(), this.port);
             OutputStream os = socket.getOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(object);
        }
    }
    protected void overrideSend(Object object) throws IOException {
        try (Socket socket = new Socket(THAT.getAddress(), this.port);
                  OutputStream os = socket.getOutputStream();
                  ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(object);
        }
    }

    public void close() throws IOException {
        ping.cancel();
        ping.purge();
    }

    @Override
    protected void finalize() throws IOException {
        this.close();
    }

    protected static void sendThroughSocket(Socket socket, Object object) throws IOException {
        new ObjectOutputStream(socket.getOutputStream()).writeObject(object);
    }

    public End getOtherEnd() {
        return this.THAT;
    }
}
