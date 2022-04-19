package com.denesgarda.Socketeer;

import java.io.IOException;

/**
 * An interface that can be queued in a connection
 * If there is a Queueable in the queue, when something is read through the connection's input stream reader, instead of firing a ReceiveEvent, the received data is passed through the interface's method
 */
public interface Queueable {

    /**
     * The method that is called when the Queueable is next up
     * @param data The data read from the input stream reader
     * @throws IOException To perform operations inside the method without using try/catch
     */
    void nextIn(String data) throws IOException;
}
