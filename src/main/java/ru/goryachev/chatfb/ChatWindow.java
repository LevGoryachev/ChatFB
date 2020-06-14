package ru.goryachev.chatfb;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChatWindow extends JFrame {

    private final String username;
    private ChatDataAccess chatDataAccess;

    private JLabel labelUsersCounter;
    private JTextPane textPaneMessages;
    private JTextField textFieldInput;
    private JButton buttonSend;

    private SimpleAttributeSet localUserAttributeSet;
    private SimpleAttributeSet otherUsersAttributeSet;
    private SimpleAttributeSet authorAttributeSet;

    public ChatWindow(String username, ChatDataAccess chatDataAccess) {
        this.username = username;
        this.chatDataAccess = chatDataAccess;
        initComponents();

    }

    private void initComponents() {

        chatDataAccess.setUsername(this.username);          //GOT TO REDO
        chatDataAccess.setLocalUserAttributeSet(localUserAttributeSet);         //GOT TO REDO
        chatDataAccess.setOtherUsersAttributeSet(otherUsersAttributeSet);           //GOT TO REDO
        //       chatDataAccess.setTextPaneMessages(textPaneMessages);                 //GOT TO REDO
        chatDataAccess.setAuthorAttributeSet(authorAttributeSet);               //GOT TO REDO
        chatDataAccess.setLabelUsersCounter(labelUsersCounter);             //GOT TO REDO

        setTitle("ChatFB: " + username);
        setSize(600, 450);
        setLocation(500, 300);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initAttributeSets();


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                chatDataAccess.goOffline();
            }
        });

        JPanel panelTop = new JPanel();
        panelTop.add(new JLabel("Online Users: "));
        labelUsersCounter = new JLabel("0");
        panelTop.add(labelUsersCounter);

        add(panelTop, BorderLayout.NORTH);

        textPaneMessages = new JTextPane(new DefaultStyledDocument());
        textPaneMessages.setEditable(false);
        DefaultCaret caret = (DefaultCaret) textPaneMessages.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane scrollPane = new JScrollPane(textPaneMessages);
        this.textPaneMessages = chatDataAccess.getTextPaneMessages();

        JPanel panelBottom = new JPanel(new BorderLayout());

        textFieldInput = new JTextField();

        //SendMessageHandler sendMessageHandler = new SendMessageHandler(textFieldInput, username);
        //chatDataAccess.getHandler();

        textFieldInput.addActionListener(chatDataAccess.getHandler(textFieldInput, username));

        panelBottom.add(textFieldInput, BorderLayout.CENTER);

        buttonSend = new JButton("Send");
        buttonSend.addActionListener(chatDataAccess.getHandler(textFieldInput, username));

        panelBottom.add(buttonSend, BorderLayout.EAST);
        add(scrollPane);
        add(panelBottom, BorderLayout.SOUTH);

        chatDataAccess.setupDBReferences();
        chatDataAccess.goOnline();
    }

    private void initAttributeSets() {
        localUserAttributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(localUserAttributeSet, Color.CYAN);

        otherUsersAttributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(otherUsersAttributeSet, Color.GREEN);

        authorAttributeSet = new SimpleAttributeSet();
        StyleConstants.setBold(authorAttributeSet, true);
    }

}
