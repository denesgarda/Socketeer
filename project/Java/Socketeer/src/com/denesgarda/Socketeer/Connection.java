package com.denesgarda.Socketeer;

import java.io.*;
import java.net.Socket;

public class Connection {
    private final End THIS;
    private final End THAT;
    private final Socket socket;
    private End host;

    protected Connection(End THIS, End THAT, Socket socket, End host) {
        this.THIS = THIS;
        this.THAT = THAT;
        this.socket = socket;
        this.host = host;
    }

    public void send(String data) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write(data);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    public End getOtherEnd() {
        return THAT;
    }

    public void close() throws IOException {
        socket.close();
        host.pendingConnections.remove(this);
        host.connections.remove(this);
    }

    public String awaitResponse() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return bufferedReader.readLine();
    }
}
