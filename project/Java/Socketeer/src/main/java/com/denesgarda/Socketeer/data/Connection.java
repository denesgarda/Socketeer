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
    private Timer timer;
    private Listener listener;

    protected Connection(End THIS, End THAT, int port, Listener listener) throws IOException {
        this.THIS = THIS;
        this.THAT = THAT;
        this.port = port;
        this.listener = listener;
        this.send("01101100 01101001 01110011 01110100 01100101 01101110 00100000 01110011 01110100 01100001 01110010 01110100");
    }

    protected void keep() throws IOException {
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(THAT.getAddress(), port);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject("01101011 01100101 01100101 01110000");
                    socket.close();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        };
        this.timer.scheduleAtFixedRate(timerTask, 0, 5000);
        Runtime.getRuntime().addShutdownHook(new Thread(this::kill));
    }

    public void kill() {
        //kill
    }

    public void send(Object object) throws IOException {
        Socket socket = new Socket(THAT.getAddress(), this.port);
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        socket.close();
    }

    public End getEnd() {
        return this.THAT;
    }
}
