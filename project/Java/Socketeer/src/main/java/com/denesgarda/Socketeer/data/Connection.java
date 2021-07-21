package com.denesgarda.Socketeer.data;

import com.denesgarda.Socketeer.data.event.Listener;

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

    protected Connection(End THIS, End THAT, int port, Listener listener, Socket socket) {
        this.THIS = THIS;
        this.THAT = THAT;
        this.port = port;
        this.listener = listener;
        this.socket = socket;
    }

    public Object send(Object object, DataType writeDataType, DataType readDataType) throws IOException, ClassNotFoundException {
        this.socket = new Socket(THAT.getAddress(), this.port);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(this.socket.getInputStream());
        switch(writeDataType) {
            case UTF -> objectOutputStream.writeUTF((String) object);
            case INTEGER -> objectOutputStream.write((int) object);
            case BYTE -> objectOutputStream.writeByte((byte) object);
            case BYTE_ARRAY -> objectOutputStream.write((byte[]) object);
            case CHAR -> objectOutputStream.writeChar((char) object);
            case LONG -> objectOutputStream.writeLong((long) object);
            case FLOAT -> objectOutputStream.writeFloat((float) object);
            case DOUBLE -> objectOutputStream.writeDouble((double) object);
            case BOOLEAN -> objectOutputStream.writeBoolean((boolean) object);
            case OBJECT -> objectOutputStream.writeObject(object);
        }
        Object response;
        switch(readDataType) {
            case UTF -> response = objectInputStream.readUTF();
            case INTEGER -> response = objectInputStream.read();
            case BYTE -> response = objectInputStream.readByte();
            case BYTE_ARRAY -> response = objectInputStream.readAllBytes();
            case CHAR -> response = objectInputStream.readChar();
            case LONG -> response = objectInputStream.readLong();
            case FLOAT -> response = objectInputStream.readFloat();
            case DOUBLE -> response = objectInputStream.readDouble();
            case BOOLEAN -> response = objectInputStream.readBoolean();
            case OBJECT -> response = objectInputStream.readObject();
            default -> throw new IllegalStateException("Unexpected value: " + readDataType);
        }
        objectOutputStream.close();
        objectInputStream.close();
        socket.close();
        socket = null;
        return response;
    }
}
