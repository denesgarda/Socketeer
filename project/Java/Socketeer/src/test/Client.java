package test;

import com.denesgarda.Socketeer.Connection;
import com.denesgarda.Socketeer.SocketeerClient;
import com.denesgarda.Socketeer.event.Event;
import com.denesgarda.Socketeer.event.ServerConnectionCloseEvent;

import java.io.IOException;

public class Client extends SocketeerClient {
    protected Client() throws IOException {
        Connection connection = this.connect("localhost", 9000);
        connection.send("Hello");
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof ServerConnectionCloseEvent) {
            System.out.println("Connection closed");
        }
    }

    public static void main(String[] args) throws IOException {
        new Client();
    }
}
