package com.denesgarda.Socketeer.data;

import com.denesgarda.Socketeer.data.event.Listener;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class End {
    private final String address;
    private Listener listener = new Listener() {};
    private boolean listening = false;
    private int connectionTimeout = 10;
    private int connectionThrottle = 50;

    protected End() throws UnknownHostException {
        this.address = InetAddress.getLocalHost().getHostName();
    }

    private End(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    public void setEventListener(Listener listener) {
        this.listener = listener;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getConnectionThrottle() {
        return connectionThrottle;
    }

    public void setConnectionThrottle(int connectionThrottle) {
        this.connectionThrottle = connectionThrottle;
    }
}
