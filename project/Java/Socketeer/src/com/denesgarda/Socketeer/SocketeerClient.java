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
        final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(accept.getOutputStream()));
        final BufferedReader in = new BufferedReader(new InputStreamReader(accept.getInputStream()));
        Connection connection = new Connection(this, new End(otherAddress), accept, in, out);
        pendingConnections.add(connection);
        out.write(End.VERSION);
        out.newLine();
        out.flush();
        String response = in.readLine();
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
                            String incoming = read();
                            if(incoming == null) {
                                throw new ConnectException("Connection lost");
                            }
                            Event.callEvent(eventListener, new ReceivedEvent(connection, incoming));
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
                    return in.readLine();
                }

                private void write(String content) throws IOException {
                    out.write(content);
                    out.newLine();
                    out.flush();
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
