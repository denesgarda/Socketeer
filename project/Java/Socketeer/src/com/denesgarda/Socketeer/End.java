package com.denesgarda.Socketeer;

import com.denesgarda.Socketeer.event.Event;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

public abstract class End {
    public static final String VERSION = "3.2";
    protected LinkedList<Connection> connections = new LinkedList<>();

    private final String address;

    protected End() throws UnknownHostException {
        address = InetAddress.getLocalHost().getHostName();
    }

    protected End(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public abstract void onEvent(Event event);
}
