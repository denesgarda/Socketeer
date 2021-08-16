package com.denesgarda.Socketeer;

import com.denesgarda.Socketeer.End;
import com.denesgarda.Socketeer.err.ConnectionRejectedException;
import com.denesgarda.Socketeer.event.Event;
import com.denesgarda.Socketeer.event.ReceivedEvent;

import java.io.*;
import java.net.*;
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
        bufferedWriter.write(End.VERSION);
        bufferedWriter.newLine();
        bufferedWriter.flush();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
        String response = bufferedReader.readLine();
        if(response.equals("code: 0")) {
            pendingConnections.remove(connection);
            connections.add(connection);
            new Thread(new Runnable() {
                private Socket socket;

                @Override
                public void run() {
                    try {
                        socket = accept;
                        while(!socket.isClosed()) {
                            /*BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            if(in.ready()) {
                                String incoming = read();
                                Event.callEvent(eventListener, new ReceivedEvent(connection, incoming));
                            }*/
                            throw new ConnectException("Connection lost");
                            /*String incoming = read();
                            if(incoming != null) {
                                Event.callEvent(eventListener, new ReceivedEvent(connection, incoming));
                            }
                            else {
                                throw new ConnectException("Connection lost");
                            }*/
                        }
                    }
                    catch(SocketException e) {
                        try {
                            connection.close();
                            throw e;
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
        else if(response.equals("code: 1")) {
            connection.close();
            throw new ConnectionRejectedException("Connection throttle");
        }
        else if(response.startsWith("version: ")) {
            connection.close();
            throw new ConnectionRejectedException("Incompatible version; client on " + End.VERSION + "; server on " + response.substring(9));
        }
        return connection;
    }
}
