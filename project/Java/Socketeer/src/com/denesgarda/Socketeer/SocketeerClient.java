package com.denesgarda.Socketeer;

import com.denesgarda.Socketeer.event.Event;

import java.io.*;
import java.net.*;

/**
 * The SocketeerClient class is the superclass of manually created clients
 */
public abstract class SocketeerClient extends End {

    /**
     * The default constructor that must be declared
     * @throws UnknownHostException If Java cannot get the current address
     */
    protected SocketeerClient() throws UnknownHostException {

    }

    /**
     * Connects to a server
     * @param address The address of the server to be connected to
     * @param port THe port to be used to connect
     * @return The newly created connection between the server and client
     * @throws IOException If the connection fails to be established
     */
    public Connection connect(String address, int port) throws IOException {
        Socket socket = new Socket(address, port);
        String otherAddress = ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().toString().replace("/", "");
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        Connection connection = new Connection(this, new End(otherAddress) {
            @Override
            public void onEvent(Event event) {

            }
        }, socket, in, out);
        connections.add(connection);
        return connection;
    }
}
