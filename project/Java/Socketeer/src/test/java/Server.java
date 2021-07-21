import com.denesgarda.Socketeer.data.DataType;
import com.denesgarda.Socketeer.data.End;
import com.denesgarda.Socketeer.data.event.EventHandler;
import com.denesgarda.Socketeer.data.event.Listener;
import com.denesgarda.Socketeer.data.event.ReceivedEvent;

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
    public void onReceived(ReceivedEvent event) throws Exception {
        String message = (String) event.read(DataType.UTF);
        event.reply("Mabes", DataType.UTF);
        System.out.println(message);
    }
}
