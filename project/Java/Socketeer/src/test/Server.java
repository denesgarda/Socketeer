package test;

import com.denesgarda.Socketeer.SocketeerServer;
import com.denesgarda.Socketeer.event.Event;
import com.denesgarda.Socketeer.event.ReceiveEvent;

import java.net.UnknownHostException;

public class Server extends SocketeerServer {
    protected Server() throws UnknownHostException {
        this.listen(9000);
    }

    @Override
    public void onEvent(Event event) {
        try {
            if (event instanceof ReceiveEvent) {
                ((ReceiveEvent) event).getConnection().close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        new Server();
    }
}
