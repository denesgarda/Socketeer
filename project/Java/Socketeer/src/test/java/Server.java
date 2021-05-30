import com.denesgarda.Socketeer.data.End;
import com.denesgarda.Socketeer.data.event.*;

import java.io.IOException;

public class Server extends End implements Listener {
    public Server() throws IOException {
        this.addEventListener(this);
        this.listen(11000);
    }

    @Override
    public void event(Event event) {
        if(event instanceof ConnectionSuccessfulEvent) {
            System.out.println("Client connected: " + ((ConnectionEvent) event).getConnection().getOtherEnd().getAddress());
        }
        else if(event instanceof ReceivedEvent) {
            System.out.println("Received: " + ((ReceivedEvent) event).getObject());
        }
        else if(event instanceof DisconnectEvent) {
            System.out.println("Client disconnected: " + ((DisconnectEvent) event).getConnection().getOtherEnd().getAddress());
        }
    }
}
