package com.denesgarda.Socketeer;

import com.denesgarda.Socketeer.event.Event;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

/**
 * The superclass for SocketeerServer and SocketeerClient
 * Holds the information about one of the ends of a connection
 */
public abstract class End {

    /**
     * The current Socketeer version
     */
    public static final String VERSION = "3.5";

    /**
     * A list of all the open connections
     */
    protected LinkedList<Connection> connections = new LinkedList<>();

    /**
     * The address of either the server or client
     */
    private final String address;

    /**
     * The time in milliseconds that the server or client waits before firing an event
     */
    protected int buffer;

    /**
     * The default constructor of End
     * @throws UnknownHostException If Java cannot get the current address
     */
    protected End() throws UnknownHostException {
        address = InetAddress.getLocalHost().getHostName();
    }

    /**
     * A manual constructor for End
     * Should only be used by Socketeer
     * @param address The address wished to be assigned to the End
     */
    protected End(String address) {
        this.address = address;
    }

    /**
     * Gets the current address for either the server or client
     * @return The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the event buffer
     * @param milliseconds The buffer time in milliseconds
     */
    public void setBuffer(int milliseconds) {
        buffer = milliseconds;
    }

    /**
     * Gets the event buffer
     * @return The buffer time in milliseconds
     */
    public int getBuffer() {
        return buffer;
    }

    /**
     * Gets all the connections currently alive
     * @return A list of the connections
     */
    public LinkedList<Connection> getConnections() {
        return connections;
    }

    /**
     * Closes all the currently open connections
     * @throws IOException If the connections fail to close
     */
    public void close() throws IOException {
        for (Connection connection : connections) {
            connection.close();
        }
    }

    /**
     * The event handler that must be declared when creating either a server or client
     * It manages all the internal events called by Socketeer
     * @param event The event that is called
     */
    public abstract void onEvent(Event event);
}
