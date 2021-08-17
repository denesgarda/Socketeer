package test;

import com.denesgarda.Socketeer.Connection;
import com.denesgarda.Socketeer.SocketeerClient;
import com.denesgarda.Socketeer.event.EventHandler;
import com.denesgarda.Socketeer.event.EventListener;
import com.denesgarda.Socketeer.event.ReceivedEvent;

import java.io.IOException;

public class Client extends SocketeerClient implements EventListener {
    public Client() throws IOException {
        this.setEventListener(this);
        Connection connection = this.connect("localhost", 9000);
    }

    @EventHandler
    public void onReceived(ReceivedEvent event) {
        System.out.println("[Server]: " + event.getData());
    }

    public static void main(String[] args) throws IOException {
        new Client();
    }
}
