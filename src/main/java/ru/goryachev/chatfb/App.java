package ru.goryachev.chatfb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                new ChatWindow();
            }
        });
        System.out.println("start application");
    }
}
