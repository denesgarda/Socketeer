import com.denesgarda.Socketeer.data.Connection;
import com.denesgarda.Socketeer.data.DataType;
import com.denesgarda.Socketeer.data.End;
import com.denesgarda.Socketeer.data.OneTimeAction;

import java.io.IOException;

public class Client extends End {
    public Client() throws Exception {
        this.connectOneTime("localhost", 9000, new OneTimeAction() {
            @Override
            public void action(Connection connection) throws IOException, ClassNotFoundException {
                String reply = (String) connection.send("Hello There!", DataType.UTF, DataType.UTF);
                System.out.println(reply);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        new Client();
    }
}
