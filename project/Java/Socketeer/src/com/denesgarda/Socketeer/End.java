package com.denesgarda.Socketeer;

import com.denesgarda.Socketeer.event.EventListener;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class End {
    public static String VERSION = "2.1";

    private final String address;
    protected LinkedList<Connection> pendingConnections = new LinkedList<>();
    protected LinkedList<Connection> connections = new LinkedList<>();

    protected End() throws UnknownHostException {
        address = InetAddress.getLocalHost().getHostName();
    }

    protected End(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
