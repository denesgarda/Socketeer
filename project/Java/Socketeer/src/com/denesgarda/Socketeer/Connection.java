package com.denesgarda.Socketeer;

import com.denesgarda.Socketeer.event.ClientDisconnectEvent;
import com.denesgarda.Socketeer.event.ReceiveEvent;
import com.denesgarda.Socketeer.event.ServerCloseEvent;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class Connection {
    private final End THIS;
    private final End THAT;
    private final Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    public Thread connectionThread;
    private LinkedList<Queueable> queue = new LinkedList<>();

    protected Connection(End THIS, End THAT, Socket socket, DataInputStream in, DataOutputStream out) {
        this.THIS = THIS;
        this.THAT = THAT;
        this.socket = socket;
        this.in = in;
        this.out = out;
        connectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!socket.isClosed()) {
                    try {
                        String data = in.readUTF();
                        if (queue.size() > 0) {
                            queue.get(0).nextIn(data);
                            queue.remove(queue.get(0));
                        } else {
                            THIS.onEvent(new ReceiveEvent(Connection.this, data));
                        }
                    } catch (IOException e) {
                        if (THIS instanceof SocketeerServer) {
                            THIS.onEvent(new ClientDisconnectEvent(THAT));
                        }
                        if (THIS instanceof SocketeerClient) {
                            THIS.onEvent(new ServerCloseEvent());
                        }
                        try {
                            close();
                        } catch (IOException ex) {
                           ex.printStackTrace();
                        }
                    }
                }
            }
        });
        connectionThread.start();
    }

    public void send(String data) throws IOException {
        out.writeUTF(data);
        out.flush();
    }

    public void nextIn(Queueable queueable) {
        queue.add(queueable);
    }

    public End getOtherEnd() {
        return THAT;
    }

    public Socket getSocket() {
        return socket;
    }

    public void close() throws IOException {
        socket.close();
        connectionThread.interrupt();
        THIS.connections.remove(this);
    }

    public boolean isClosed() {
        return socket.isClosed();
    }
}
