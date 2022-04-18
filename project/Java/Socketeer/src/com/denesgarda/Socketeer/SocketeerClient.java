package com.denesgarda.Socketeer;

import com.denesgarda.Socketeer.event.Event;

import java.io.*;
import java.net.*;

public abstract class SocketeerClient extends End {
    protected SocketeerClient() throws UnknownHostException {

    }

    public Connection connect(String address, int port) throws IOException {
        Socket socket = new Socket(address, port);
        String otherAddress = ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().toString().replace("/", "");
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        return new Connection(this, new End(otherAddress) {
            @Override
            public void onEvent(Event event) {

            }
        }, socket, in, out);
    }
}
