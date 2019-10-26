package com.example.travelapp.models;

public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String profilePictureUrl;
    private Long statesInfo;
    private String uid;

    public User() {

    }

    public User(String userEmail, String fName, String lName, String userProfilePictureurl, Long statesInfo, String userId) {
        this.email = userEmail;
        this.firstName = fName;
        this.lastName = lName;
        this.profilePictureUrl = userProfilePictureurl;
        this.statesInfo = statesInfo;
        this.uid = userId;

    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public long getStatesInfo() {
        return statesInfo;
    }

    public String getUid() {
        return uid;
    }
}
