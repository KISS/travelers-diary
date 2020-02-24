package com.example.travelapp.models;

public class AllChatsData {
    String from;
    String to;

    public AllChatsData() {

    }

    public AllChatsData(String fromUser, String toUser) {
        from = fromUser;
        to = toUser;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
