package com.example.travelapp.models;

public class Trip {

    private String trip_id;
    private String user_id;
    private String image;
    private String title;
    private String city;
    private int state;
    private String date;
    private int days;
    private String description;

    public Trip() {

    }

    public Trip(String trip_id, String user_id, String image, String title, String city, int state, String date, int days, String description) {
        this.trip_id = trip_id;
        this.user_id = user_id;
        this.image = image;
        this.title = title;
        this.city = city;
        this.state = state;
        this.date = date;
        this.days = days;
        this.description = description;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "trip_id='" + trip_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", city='" + city + '\'' +
                ", state=" + state +
                ", date='" + date + '\'' +
                ", days=" + days +
                ", description='" + description + '\'' +
                '}';
    }
}
