package ru.goryachev.chatfb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatWindow extends JFrame implements ActionListener {


        private final JTextArea poleText = new JTextArea();
        private final JTextField poleNick = new JTextField("");
        private final JTextField poleInput = new JTextField();
        private static final int W = 500;
        private static final int H = 300;

        public ChatWindow () {
            setVisible(true);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setSize(W, H);
            setLocationRelativeTo(null);
            setAlwaysOnTop(true);
            poleText.setEditable(false);
            add (poleText, BorderLayout.CENTER);
            poleInput.addActionListener(this);
            add (poleInput, BorderLayout.SOUTH);
            add (poleNick, BorderLayout.NORTH);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String mes = poleInput.getText();
            if(mes.equals("")) return;
            poleInput.setText(null);
        }
}
