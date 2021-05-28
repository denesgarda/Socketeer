import com.denesgarda.Socketeer.data.End;
import com.denesgarda.Socketeer.data.event.ConnectionEvent;
import com.denesgarda.Socketeer.data.event.Event;
import com.denesgarda.Socketeer.data.event.Listener;
import com.denesgarda.Socketeer.data.event.ReceivedEvent;

import java.io.IOException;
import java.net.UnknownHostException;

public class Server extends End implements Listener {
    public Server() throws IOException {
        this.addEventListener(this);
        this.listen(11000);
    }

    @Override
    public void event(Event event) {
        if(event instanceof ConnectionEvent) {
            System.out.println("Client connected: " + ((ConnectionEvent) event).getConnection().getOtherEnd().getAddress());
        }
        else if(event instanceof ReceivedEvent) {
            System.out.println("Received: " + ((ReceivedEvent) event).getObject());
        }
    }
}
