package com.denesgarda.Socketeer;

import com.denesgarda.Socketeer.End;
import com.denesgarda.Socketeer.err.ConnectionRejectedException;
import com.denesgarda.Socketeer.event.Event;
import com.denesgarda.Socketeer.event.ReceivedEvent;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class SocketeerClient extends End {
    private LinkedList<Connection> connections = new LinkedList<>();

    protected SocketeerClient() throws UnknownHostException {

    }

    public Connection connect(String address, int port) throws IOException {
        Socket accept = new Socket(address, port);
        String otherAddress = ((InetSocketAddress) accept.getRemoteSocketAddress()).getAddress().toString().replace("/", "");
        Connection connection = new Connection(this, new End(otherAddress), accept, this);
        pendingConnections.add(connection);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(accept.getOutputStream()));
        bufferedWriter.write("?");
        bufferedWriter.newLine();
        bufferedWriter.flush();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
        String response = bufferedReader.readLine();
        if(response.equals("0")) {
            pendingConnections.remove(connection);
            connections.add(connection);
            new Thread(new Runnable() {
                private Socket socket;

                @Override
                public void run() {
                    try {
                        socket = accept;
                        while(!socket.isClosed()) {
                            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            if(in.ready()) {
                                String incoming = read();
                                Event.callEvent(eventListener, new ReceivedEvent(connection, incoming));
                            }
                        }
                    }
                    catch(SocketException e) {
                        try {
                            connection.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }

                private String read() throws IOException {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    return bufferedReader.readLine();
                }

                private void write(String content) throws IOException {
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    bufferedWriter.write(content);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            }).start();
        }
        else if(response.equals("1")) {
            connection.close();
            throw new ConnectionRejectedException("Connection throttle");
        }
        return connection;
    }
}
