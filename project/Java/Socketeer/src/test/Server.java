package test;

import com.denesgarda.Socketeer.SocketeerServer;
import com.denesgarda.Socketeer.event.*;

import java.net.UnknownHostException;

public class Server extends SocketeerServer implements EventListener {
    public Server() throws UnknownHostException {
        this.setEventListener(this);
        this.listen(9000);
    }

    @EventHandler
    public void onClientConnected(ClientConnectedEvent event) { // Fires when a client successfully connects
        System.out.println(event.getConnection().getOtherEnd().getAddress() + " connected to the server");
    }

    @EventHandler
    public void onClientRejected(ClientRejectedEvent event) { // Fires when a client tried to connect but gets rejected
        System.out.println(event.getConnection().getOtherEnd().getAddress() + " tried to connect to the server but got rejected due to " + event.getReason());
    }

    @EventHandler
    public void onClientDisconnected(ClientDisconnectedEvent event) { // Fires when a client disconnects
        System.out.println(event.getConnection().getOtherEnd().getAddress() + " disconnected from the server");
    }

    @EventHandler
    public void onReceived(ReceivedEvent event) { // Fires when a message is received from a client
        System.out.println("[" + event.getConnection().getOtherEnd().getAddress() + "]: " + event.getData());
    }

    public static void main(String[] args) throws UnknownHostException {
        new Server();
    }
}
