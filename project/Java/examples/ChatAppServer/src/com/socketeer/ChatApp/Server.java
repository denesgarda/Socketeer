package com.socketeer.ChatApp;

import com.denesgarda.Socketeer.Connection;
import com.denesgarda.Socketeer.Queueable;
import com.denesgarda.Socketeer.SocketeerServer;
import com.denesgarda.Socketeer.event.ClientConnectEvent;
import com.denesgarda.Socketeer.event.ClientDisconnectEvent;
import com.denesgarda.Socketeer.event.Event;
import com.denesgarda.Socketeer.event.ReceiveEvent;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class Server extends SocketeerServer {
    public static LinkedList<String> nicknames = new LinkedList<>();
    public static LinkedList<Connection> clients = new LinkedList<>();

    protected Server() throws UnknownHostException {
        this.listen(12000);
    }

    @Override
    public void onEvent(Event event) {
        try {
            if (event instanceof ClientConnectEvent) {
                ((ClientConnectEvent) event).getConnection().nextIn(new Queueable() {
                    @Override
                    public void nextIn(String s) throws IOException {
                        if (nicknames.contains(s)) {
                            ((ClientConnectEvent) event).getConnection().send("Nickname is taken");
                        } else {
                            for (Connection connection : connections) {
                                connection.send(s + " joined");
                                nicknames.add(s);
                                clients.add(((ClientConnectEvent) event).getConnection());
                            }
                            System.out.println(s + " joined");
                        }
                    }
                });
            }
            if (event instanceof ClientDisconnectEvent) {
                for (Connection connection : connections) {
                    connection.send(nicknames.get(clients.indexOf()) + " left");
                    nicknames.remove(clients.indexOf(connection));
                    clients.remove(connection);
                }
                System.out.println(nicknames.get(clients.indexOf(connection)) + " left");
            }
            if (event instanceof ReceiveEvent) {
                for (Connection connection : connections) {
                    connection.send("[" + nicknames.get(clients.indexOf(connection)) + "]: " + ((ReceiveEvent) event).getData());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
