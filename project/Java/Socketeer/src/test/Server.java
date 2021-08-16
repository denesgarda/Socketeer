package test;

import com.denesgarda.Socketeer.SocketeerServer;
import com.denesgarda.Socketeer.event.EventListener;

import java.net.UnknownHostException;

public class Server extends SocketeerServer implements EventListener {
    public Server() throws UnknownHostException {
        this.setEventListener(this);
        this.listen(9000);
    }

    public static void main(String[] args) throws UnknownHostException {
        new Server();
    }
}
