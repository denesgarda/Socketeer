import com.denesgarda.Socketeer.data.End;

import java.io.IOException;
import java.net.UnknownHostException;

public class Server extends End {
    public Server() throws IOException {
        this.listen(12000);
    }
}
