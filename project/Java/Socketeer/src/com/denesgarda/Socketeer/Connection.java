package com.denesgarda.Socketeer;

import java.io.*;
import java.net.Socket;

public class Connection {
    private final End THIS;
    private final End THAT;
    private final Socket socket;
    private End host;
    private BufferedReader in;
    private BufferedWriter out;

    protected Connection(End THIS, End THAT, Socket socket, BufferedReader in, BufferedWriter out) {
        this.THIS = THIS;
        this.THAT = THAT;
        this.socket = socket;
        this.host = THIS;
        this.in = in;
        this.out = out;
    }

    public void send(String data) throws IOException {
        out.write(data);
        out.newLine();
        out.flush();
    }

    public End getOtherEnd() {
        return THAT;
    }

    public Socket getSocket() {
        return socket;
    }

    public void close() throws IOException {
        socket.close();
        host.pendingConnections.remove(this);
        host.connections.remove(this);
    }

    public String awaitResponse() throws IOException {
        return in.readLine();
    }
}
