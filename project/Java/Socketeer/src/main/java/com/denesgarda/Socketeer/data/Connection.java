package com.denesgarda.Socketeer.data;

import com.denesgarda.Prop4j.data.PropertiesFile;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Connection {
    private End THIS;
    private End THAT;
    private int port;
    private Timer timer;

    protected Connection(End THIS, End THAT, int port) {
        this.THIS = THIS;
        this.THAT = THAT;
        this.port = port;
        this.keep();
    }

    private void keep() {
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(THAT.getAddress(), port);
                    Connection connection = new Connection((((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/",""), port, listener);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject("01101011 01100101 01100101 01110000");
                    socket.close();
                }
                catch(IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        this.timer.scheduleAtFixedRate(this.timerTask, 0, 5);
        this.send("01101100 01101001 01110011 01110100 01100101 01101110 00100000 01110011 01110100 01100001 01110010 01110100");
        Runtime.getRuntime().addShutdownHook(new Thread(this::stopListening));
    }

    public void kill() {

    }

    public void send(Object object) throws IOException {
        Socket socket = new Socket(THAT.getAddress(), this.port);
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        socket.close();
    }
}
