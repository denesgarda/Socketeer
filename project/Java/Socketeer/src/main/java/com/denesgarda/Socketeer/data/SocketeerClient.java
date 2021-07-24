package com.denesgarda.Socketeer.data;

import com.denesgarda.Socketeer.data.event.Listener;
import com.denesgarda.Socketeer.data.lang.ConnectionFailedException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketeerClient extends End {
    protected SocketeerClient() throws UnknownHostException {

    }

    public void connectOneTime(String address, int port, OneTimeAction oneTimeAction) throws Exception {
        Socket socket = new Socket(address, port);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream.writeObject("01101111 01101110 01100101 01010100 01101001 01101101 01100101 01000011 01101111 01101110 01101110 01100101 01100011 01110100 01101001 01101111 01101110");
        objectOutputStream.flush();
        Object reply = objectInputStream.readObject();
        objectOutputStream.close();
        objectInputStream.close();
        socket.close();
        if(reply.equals("01101111 01101011")) {
            Connection connection = new Connection(this, new End(address), port, new Listener() {}, socket);
            oneTimeAction.action(connection);
            connection.open = false;

        }
        else {
            throw new ConnectionFailedException((String) reply);
        }
    }
}
