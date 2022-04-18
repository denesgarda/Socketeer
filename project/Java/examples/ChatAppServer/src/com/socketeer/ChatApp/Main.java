package com.socketeer.ChatApp;

import com.denesgarda.Socketeer.Connection;

import java.net.UnknownHostException;

public class Main {
    public static Window window;
    public static Server server;

    public static void main(String[] args) throws UnknownHostException {
        window = new Window();
        server = new Server();
    }

    public static boolean processInput(String input) {
        try {
            for (Connection connection : Server.clients) {
                connection.send("[Server]: " + input);
            }
            System.out.println("[Server]: " + input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
