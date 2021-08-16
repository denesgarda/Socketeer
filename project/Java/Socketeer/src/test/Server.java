package test;

import com.denesgarda.Socketeer.SocketeerServer;
import com.denesgarda.Socketeer.event.*;

import java.io.IOException;
import java.net.UnknownHostException;

public class Server extends SocketeerServer implements EventListener {
    public Server() throws UnknownHostException {
        this.setEventListener(this);
        this.listen(9000);
    }

    @EventHandler
    public void onClientRejected(ClientRejectedEvent event) {
        System.out.println(event.getConnection().getOtherEnd().getAddress() + " tried to connect but got rejected due to " + event.getReason());
    }

    @EventHandler
    public void onClientConnected(ClientConnectedEvent event) {
        System.out.println(event.getConnection().getOtherEnd().getAddress() + " connected to the server");
    }

    @EventHandler
    public void onReceived(ReceivedEvent event) throws IOException {
        System.out.println("[" + event.getConnection().getOtherEnd().getAddress() + "]: " + event.getData());
        event.getConnection().send("Response");
    }

    @EventHandler
    public void onClientDisconnected(ClientDisconnectedEvent event) {
        System.out.println(event.getConnection().getOtherEnd().getAddress() + " disconnected from the server");
    }

    public static void main(String[] args) throws UnknownHostException {
        new Server();
    }
}
