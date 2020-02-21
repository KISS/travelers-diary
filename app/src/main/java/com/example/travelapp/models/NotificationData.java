package com.example.travelapp.models;

public class NotificationData {
    String from;
    String to;

    public NotificationData() {

    }

    public NotificationData(String fromUser, String toUser) {
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
