package com.socketeer.ChatApp;

import com.denesgarda.Socketeer.Connection;
import com.denesgarda.Socketeer.SocketeerClient;
import com.denesgarda.Socketeer.event.Event;
import com.denesgarda.Socketeer.event.ReceiveEvent;

import java.io.IOException;

public class Client extends SocketeerClient {
    public static Connection connection;

    protected Client(String address, String nickname) throws IOException {
        connection = this.connect(address, 12000);
        connection.send(nickname);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof ReceiveEvent) {
            System.out.println(((ReceiveEvent) event).getData());
        }
    }
}
