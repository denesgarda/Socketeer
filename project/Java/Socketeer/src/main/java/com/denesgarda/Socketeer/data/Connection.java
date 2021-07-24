package com.denesgarda.Socketeer.data;

import com.denesgarda.Socketeer.data.event.Listener;
import com.denesgarda.Socketeer.data.lang.RestrictedObjectException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {
    private End THIS;
    private End THAT;
    private int port;
    private Listener listener;
    private Socket socket;

    protected boolean open = true;

    protected Connection(End THIS, End THAT, int port, Listener listener, Socket socket) {
        this.THIS = THIS;
        this.THAT = THAT;
        this.port = port;
        this.listener = listener;
        this.socket = socket;
    }

    public End getOtherEnd() {
        return THAT;
    }

    public Object send(Object object) throws IOException, ClassNotFoundException {
        if(object.equals("01101111 01101110 01100101 01010100 01101001 01101101 01100101 01000011 01101111 01101110 01101110 01100101 01100011 01110100 01101001 01101111 01101110") || object.equals("01101111 01101011")) {
            throw new RestrictedObjectException();
        }
        else {
            this.socket = new Socket(THAT.getAddress(), this.port);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            Object reply = objectInputStream.readObject();
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();
            socket = new Socket();
            return reply;
        }
    }

    public boolean isOpen() {
        return this.open;
    }

    public enum ConnectionType {
        ONE_TIME_CONNECTION,
        STATIC
    }
}
