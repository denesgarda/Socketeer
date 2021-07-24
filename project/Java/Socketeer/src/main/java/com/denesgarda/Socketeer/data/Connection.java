package com.denesgarda.Socketeer.data;

import com.denesgarda.Socketeer.data.event.Listener;

import java.io.IOException;
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

    public Object send(Object object, DataType writeDataType, DataType readDataType) throws IOException, ClassNotFoundException {
        this.socket = new Socket(THAT.getAddress(), this.port);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
        //ObjectInputStream objectInputStream = new ObjectInputStream(this.socket.getInputStream());
        if(writeDataType == DataType.UTF) {
            objectOutputStream.writeUTF((String) object);
        }
        else if(writeDataType == DataType.INTEGER) {
            objectOutputStream.write((int) object);
        }
        else if(writeDataType == DataType.BYTE) {
            objectOutputStream.writeByte((byte) object);
        }
        else if(writeDataType == DataType.BYTE_ARRAY) {
            objectOutputStream.write((byte[]) object);
        }
        else if(writeDataType == DataType.CHAR) {
            objectOutputStream.writeChar((char) object);
        }
        else if(writeDataType == DataType.LONG) {
            objectOutputStream.writeLong((long) object);
        }
        else if(writeDataType == DataType.FLOAT) {
            objectOutputStream.writeFloat((float) object);
        }
        else if(writeDataType == DataType.DOUBLE) {
            objectOutputStream.writeDouble((double) object);
        }
        else if(writeDataType == DataType.BOOLEAN) {
            objectOutputStream.writeBoolean((boolean) object);
        }
        else if(writeDataType == DataType.OBJECT) {
            objectOutputStream.writeObject(object);
        }
        System.out.println(3);
        Object response;
        /*if(readDataType == DataType.UTF) {
            System.out.println(4);
            response = objectInputStream.readUTF();
            System.out.println(5);
        }
        else if(readDataType == DataType.INTEGER) {
            response = objectInputStream.read();
        }
        else if(readDataType == DataType.BYTE) {
            response = objectInputStream.readByte();
        }
        else if(readDataType == DataType.BYTE_ARRAY) {
            response = objectInputStream.readAllBytes();
        }
        else if(readDataType == DataType.CHAR) {
            response = objectInputStream.readChar();
        }
        else if(readDataType == DataType.LONG) {
            response = objectInputStream.readLong();
        }
        else if(readDataType == DataType.FLOAT) {
            response = objectInputStream.readFloat();
        }
        else if(readDataType == DataType.DOUBLE) {
            response = objectInputStream.readDouble();
        }
        else if(readDataType == DataType.BOOLEAN) {
            response = objectInputStream.readBoolean();
        }
        else if(readDataType == DataType.OBJECT) {
            response = objectInputStream.readObject();
        }
        else {
            throw new IllegalStateException("Unexpected value: " + readDataType);
        }
        objectOutputStream.close();
        objectInputStream.close();
        socket.close();
        socket = null;
        return response;*/
        return null;
    }

    public boolean isOpen() {
        return this.open;
    }
}
