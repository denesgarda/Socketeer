package com.denesgarda.Socketeer.data;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class End {
    private String address;

    protected End() throws UnknownHostException {
        this.address = InetAddress.getLocalHost().getHostName();
    }
    private End(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public Connection connection(String address, int port) {
        return new Connection(this, new End(address), port);
    }
}
