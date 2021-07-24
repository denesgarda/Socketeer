import com.denesgarda.Socketeer.data.End;
import com.denesgarda.Socketeer.data.event.*;

import java.io.IOException;

public class Server extends End implements Listener {
    protected Server() throws IOException {
        this.setEventListener(this);
        this.listen(9000);
    }

    public static void main(String[] args) throws IOException {
        new Server();
    }

    @EventHandler
    public void onClientConnected(ClientConnectedEvent event) {
        System.out.println(event.getConnection().getOtherEnd().getAddress() + " connected using " + event.getConnectionType());
    }

    @EventHandler
    public void onReceived(ReceivedEvent event) throws Exception {
        String message = (String) event.read();
        System.out.println("Message received from " + event.getConnection().getOtherEnd().getAddress() + ": " + message);
        event.reply("This is the server's reply");
    }

    @EventHandler
    public void onClientDisconnected(ClientDisconnectedEvent event) {
        System.out.println(event.getConnection().getOtherEnd().getAddress() + " disconnected");
    }
}
