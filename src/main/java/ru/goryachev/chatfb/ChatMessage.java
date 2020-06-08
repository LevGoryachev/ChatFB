package ru.goryachev.chatfb;

import java.util.Date;

public class ChatMessage {

    private String userName;
    private long messageTime;
    private String messageText;

    public ChatMessage (String messageText, String userName) {

        this.userName = userName;
        this.messageText = messageText;
        this.messageTime = new Date().getTime();
    }

    public String getUserName() {
        return userName;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageText() {
        return messageText;
    }
}
