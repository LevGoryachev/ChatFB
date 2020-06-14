package ru.goryachev.chatfb;

import javax.swing.*;

public class App extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {

                ChatDataAccess cda = new ChatDataAccess();
                new ChatWindow("UserName", cda);

            }
        });
        System.out.println("start application");
    }
}
