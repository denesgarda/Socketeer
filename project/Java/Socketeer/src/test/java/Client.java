import com.denesgarda.Socketeer.data.Connection;
import com.denesgarda.Socketeer.data.End;
import com.denesgarda.Socketeer.data.event.EventHandler;
import com.denesgarda.Socketeer.data.event.ReceivedEvent;

import java.io.IOException;

public class Client extends End {
    public Client() throws IOException {
        this.addEventListener(this);
        Connection connection = this.connect("localhost", 11111);
        connection.send("Hello");
        connection.send("Hello again");
        //connection.close();
    }

    @EventHandler
    public void onReceived(ReceivedEvent event) {
        System.out.println("Received: " + event.getObject());
    }
}
