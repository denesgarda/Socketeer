package test;

import com.denesgarda.Socketeer.SocketeerClient;

import java.io.IOException;

public class Client extends SocketeerClient {
    public Client() throws IOException {
        this.connect("localhost", 9000);
    }

    public static void main(String[] args) throws IOException {
        new Client();
    }
}
