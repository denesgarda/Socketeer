package com.denesgarda.Socketeer.data;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The end of a connection.
 * This is either a server or a client.
 * It is the superclass of SocketeerClient and SocketeerServer
 * @author denesgarda
 */
public class End {
    /**
     * The address of the server or client.
     */
    private final String address;

    /**
     * The default constructor of End.
     * This should only be used within socketeer. Users should not manually construct Ends, as to not cause unnecessary confusion and Exceptions.
     * @throws UnknownHostException
     */
    protected End() throws UnknownHostException {
        this.address = InetAddress.getLocalHost().getHostName();
    }

    /**
     * The secondary constructor of End.
     * This should only be used within socketeer. Users should not manually construct Ends, as to not cause unnecessary confusion and Exceptions.
     * @param address An overwritten address of the End
     */
    protected End(String address) {
        this.address = address;
    }

    /**
     * Gets the address of the End.
     * @return The address of the End
     */
    public String getAddress() {
        return address;
    }
}
