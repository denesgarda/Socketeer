package com.denesgarda.Socketeer.err;

import java.net.ConnectException;

public class ConnectionRejectedException extends ConnectException {
    public ConnectionRejectedException(String message) {
        super(message);
    }
}
