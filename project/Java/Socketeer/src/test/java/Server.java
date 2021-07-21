import com.denesgarda.Socketeer.data.End;
import com.denesgarda.Socketeer.data.event.*;

import java.io.IOException;

public class Server extends End {
    public Server() throws IOException {
        this.addEventListener(this);
        this.listen(11111);
    }

    @EventHandler
    public void onConnectionAttempt(ConnectionAttemptEvent event) {
        System.out.println("Client connecting: " + event.getConnection().getOtherEnd().getAddress());
    }

    @EventHandler
    public void onConnectionSuccessful(ConnectionSuccessfulEvent event) {
        System.out.println("Client connected: " + event.getConnection().getOtherEnd().getAddress());
    }

    @EventHandler
    public void onReceived(ReceivedEvent event) throws IOException {
        System.out.println("Received: " + event.getObject());
    }

    @EventHandler
    public void onDisconnect(DisconnectEvent event) {
        System.out.println("Client disconnected: " + event.getConnection().getOtherEnd().getAddress());
    }
}
