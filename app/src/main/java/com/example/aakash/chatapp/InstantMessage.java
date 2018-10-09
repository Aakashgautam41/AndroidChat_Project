package com.example.aakash.chatapp;

public class InstantMessage {
    private String sender;
    private String message;

    public InstantMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
