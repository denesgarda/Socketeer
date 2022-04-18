package com.socketeer.ChatApp;

import com.socketeer.ChatApp.util.TextAreaOutputStream;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintStream;

public class Window extends JFrame {
    private JPanel panel;
    private JTextArea textArea;
    private JTextField textField;
    private JScrollPane scrollPane;

    public Window() {
        super("Chat App Server");
        this.setSize(600, 600);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        textArea.setEditable(false);
        System.setOut(new PrintStream(new TextAreaOutputStream(textArea, "")));
        this.setContentPane(panel);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    boolean b = Main.processInput(textField.getText());
                    if (b) {
                        textField.setText("");
                    } else {
                        System.out.println("Invalid input");
                    }
                }
            }
        });
        this.setVisible(true);
    }
}
