package com.denesgarda.Socketeer.data;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The end of a connection; either a server or a client
 * @author denesgarda
 */
public class End {
    /**
     * The address of the end
     */
    private final String address;

    /**
     * The default constructor of End
     * @throws UnknownHostException
     */
    protected End() throws UnknownHostException {
        this.address = InetAddress.getLocalHost().getHostName();
    }

    /**
     * The secondary constructor of End
     * @param address An overwritten address of the End
     */
    protected End(String address) {
        this.address = address;
    }

    /**
     * Gets the address of the End
     * @return The address of the End
     */
    public String getAddress() {
        return address;
    }
}
