package com.denesgarda.Socketeer.data;

import com.denesgarda.Socketeer.data.event.ConnectionCloseEvent;
import com.denesgarda.Socketeer.data.event.ConnectionOpenEvent;
import com.denesgarda.Socketeer.data.event.Event;
import com.denesgarda.Socketeer.data.event.Listener;

import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class End implements Listener{
    private String address;
    private Listener listener;

    protected End() throws UnknownHostException {
        this.address = InetAddress.getLocalHost().getHostName();
    }
    private End(String address) {
        this.address = address;
    }

    public void addEventListener(Listener listener) {
        this.listener = listener;
    }

    public String getAddress() {
        return address;
    }

    public Connection connect(String address, int port) throws IOException {
        Connection connection = new Connection(this, new End(address), port, listener);
        //connection.keep();
        return connection;
    }

    public void listen(int port) throws IOException {
        if(listener == null) {
            listener = this;
        }
        Logger logger = Logger.getLogger(End.class.getName());
        logger.info("Socketeer, version 1.0");
        ServerSocket serverSocket = new ServerSocket(port);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    Socket socket = serverSocket.accept();
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
                    Connection connection = new Connection(new End(address), new End((((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","")), port, listener);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    try {
                        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                        String reading = (String) objectInputStream.readObject();
                        if(reading.equals("01101011 01100101 01100101 01110000")) {
                            objectOutputStream.writeObject("01101011 01100101 01100101 01110000");
                        }
                        else if(reading.equals("01101100 01101001 01110011 01110100 01100101 01101110 00100000 01110011 01110100 01100001 01110010 01110100")) {
                            listener.event(new ConnectionOpenEvent(connection));
                        }
                        else {
                            //listener.event(new DataReceivedEvent(connection, reading));
                        }
                    }
                    catch(EOFException e) {
                        listener.event(new ConnectionCloseEvent(connection));
                    }
                    socket.close();
                }
                catch(IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 5);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                serverSocket.close();
            }
            catch(IOException ignored) {}
        }));
    }

    @Override
    public void event(Event event) {

    }
}
