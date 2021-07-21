package com.denesgarda.Socketeer.data;

import com.denesgarda.Socketeer.data.event.Event;
import com.denesgarda.Socketeer.data.event.Listener;
import com.denesgarda.Socketeer.data.event.ReceivedEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class End {
    private final String address;
    private Listener listener = new Listener() {};
    private boolean listening = false;
    private int connectionTimeout = 10;
    private int connectionThrottle = 50;

    protected End() throws UnknownHostException {
        this.address = InetAddress.getLocalHost().getHostName();
    }

    private End(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    public void setEventListener(Listener listener) {
        this.listener = listener;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getConnectionThrottle() {
        return connectionThrottle;
    }

    public void setConnectionThrottle(int connectionThrottle) {
        this.connectionThrottle = connectionThrottle;
    }

    public void connectOneTime(String address, int port, OneTimeAction oneTimeAction) throws Exception {
        Socket socket = new Socket(address, port);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        Connection connection = new Connection(this, new End(address), port, listener, socket);
        oneTimeAction.action(connection);
        connection.open = false;
        objectOutputStream.close();
        objectInputStream.close();
        socket.close();
    }

    public void listen(int port) throws IOException {
        listening = true;
        ServerSocket serverSocket = new ServerSocket(port);
        End THIS = this;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while(THIS.listening) {
                    try {
                        Socket socket = serverSocket.accept();
                        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        Connection connection = new Connection(THIS, new End((((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","")), port, listener, socket);
                        Event.callEvent(listener, new ReceivedEvent(connection) {
                            @Override
                            public Object read(DataType readDataType) throws IOException, ClassNotFoundException {
                                Object content;
                                if(readDataType == DataType.UTF) {
                                    content = objectInputStream.readUTF();
                                }
                                else if(readDataType == DataType.INTEGER) {
                                    content = objectInputStream.read();
                                }
                                else if(readDataType == DataType.BYTE) {
                                    content = objectInputStream.readByte();
                                }
                                else if(readDataType == DataType.BYTE_ARRAY) {
                                    content = objectInputStream.readAllBytes();
                                }
                                else if(readDataType == DataType.CHAR) {
                                    content = objectInputStream.readChar();
                                }
                                else if(readDataType == DataType.LONG) {
                                    content = objectInputStream.readLong();
                                }
                                else if(readDataType == DataType.FLOAT) {
                                    content = objectInputStream.readFloat();
                                }
                                else if(readDataType == DataType.DOUBLE) {
                                    content = objectInputStream.readDouble();
                                }
                                else if(readDataType == DataType.BOOLEAN) {
                                    content = objectInputStream.readBoolean();
                                }
                                else if(readDataType == DataType.OBJECT) {
                                    content = objectInputStream.readObject();
                                }
                                else {
                                    throw new IllegalStateException("Unexpected value: " + readDataType);
                                }
                                return content;
                            }

                            @Override
                            public void reply(Object object, DataType writeDataType) throws IOException {
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
                            }
                        });
                        objectInputStream.close();
                        objectOutputStream.close();
                        socket.close();
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        runnable.run();
    }

    public void stopListening() {
        listening = false;
    }
}
