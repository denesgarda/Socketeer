package com.denesgarda.Socketeer.data;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class End {
    private final String address;

    protected End() throws UnknownHostException {
        this.address = InetAddress.getLocalHost().getHostName();
    }

    protected End(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
