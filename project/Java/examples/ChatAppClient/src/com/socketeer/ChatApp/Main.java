package com.socketeer.ChatApp;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static Window window;
    public static Client client;

    public static void main(String[] args) throws IOException {
        String address = JOptionPane.showInputDialog("Enter Server Address");
        String nickname = JOptionPane.showInputDialog("Enter Nickname");
        window = new Window();
        client = new Client(address, nickname);
    }

    public static boolean processInput(String input) {
        try {
            Client.connection.send(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
