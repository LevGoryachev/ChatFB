package ru.goryachev.chatfb;

import com.google.firebase.database.*;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class ChatDataAccess {

    private String username;
    private JLabel labelUsersCounter;
    private JTextPane textPaneMessages;
    private SimpleAttributeSet localUserAttributeSet;
    private SimpleAttributeSet otherUsersAttributeSet;
    private SimpleAttributeSet authorAttributeSet;

    private DatabaseReference dbRefOnlineUsersCounter;
    private DatabaseReference dbRefMessages;



    public void goOnline() {
        dbRefOnlineUsersCounter.child(username).setValueAsync(true);
    }

    public void goOffline() {
        dbRefOnlineUsersCounter.child(username).removeValueAsync();
    }





    private String emptyLine() {
        return "\n\n";
    }



    public void setupDBReferences() {
        ConnectConfig dbManager = ConnectConfig.getInstance();

        dbRefOnlineUsersCounter = dbManager.getDBRef("/session1/onlineUsers");
        dbRefOnlineUsersCounter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                updateLabelUsersCounter((int) data.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError de) {
            }
        });

        dbRefMessages = dbManager.getDBRef("/session1/messages");
        dbRefMessages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot data, String prevChildKey) {
                HashMap<String, String> messageObj = (HashMap<String, String>) data.getValue();
                String text = messageObj.get("text");
                String author = messageObj.get("author");
                appendMessageLocally(new ChatMessage(text, author));
            }

            @Override
            public void onChildChanged(DataSnapshot ds, String string) {
            }

            @Override
            public void onChildRemoved(DataSnapshot ds) {
            }

            @Override
            public void onChildMoved(DataSnapshot ds, String string) {
            }

            @Override
            public void onCancelled(DatabaseError de) {
            }
        });
    }

    private void appendMessageLocally(ChatMessage message) {
        boolean messageSentByLocalUser = message.getUserName().equals(this.username);

        SimpleAttributeSet as = messageSentByLocalUser ? localUserAttributeSet : otherUsersAttributeSet;
        StyledDocument doc = textPaneMessages.getStyledDocument();

        try {
            doc.setParagraphAttributes(doc.getLength(), message.getMessageText().length(), as, false);
            doc.insertString(doc.getLength(), message.getMessageText() + "\n", null);

            String authorAlias = messageSentByLocalUser ? "Me" : message.getUserName();

            doc.insertString(doc.getLength(), "- " + authorAlias + emptyLine(), authorAttributeSet);
            emptyLine();

        } catch (BadLocationException ex) {

        }
    }

    private void updateLabelUsersCounter(int newCounter) {
        int oldCounter = Integer.valueOf(labelUsersCounter.getText());

        if (oldCounter == newCounter) {
            return;
        }

        labelUsersCounter.setText(String.valueOf(newCounter));
    }

    private class SendMessageHandler implements ActionListener {

        private final JTextField textFieldInput;
        private final String author;

        public SendMessageHandler(JTextField textFieldInput, String author) {
            this.textFieldInput = textFieldInput;
            this.author = author;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (textFieldInput.getText().trim().length() > 0) {
                sendMessage(new ChatMessage(textFieldInput.getText(), author));
                textFieldInput.setText("");
                textFieldInput.requestFocus();
            }
        }
    }

    public void sendMessage(ChatMessage message) {
        DatabaseReference newMessageRef = dbRefMessages.push();
        newMessageRef.setValueAsync(message);
    }

    public SendMessageHandler getHandler (JTextField textFieldInput, String username){
        SendMessageHandler sendMessageHandler = new SendMessageHandler(textFieldInput, username);
        return sendMessageHandler;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLabelUsersCounter(JLabel labelUsersCounter) {
        this.labelUsersCounter = labelUsersCounter;
    }

    public void setLocalUserAttributeSet(SimpleAttributeSet localUserAttributeSet) {
        this.localUserAttributeSet = localUserAttributeSet;
    }

    public void setOtherUsersAttributeSet(SimpleAttributeSet otherUsersAttributeSet) {
        this.otherUsersAttributeSet = otherUsersAttributeSet;
    }

    public void setAuthorAttributeSet(SimpleAttributeSet authorAttributeSet) {
        this.authorAttributeSet = authorAttributeSet;
    }

    public JTextPane getTextPaneMessages() {
        return textPaneMessages;
    }

}
