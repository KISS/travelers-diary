package com.example.travelapp.models;

public class Trip {

    private String trip_id;
    private String user_id;
    private String image;
    private String city;
    private int state;
//    private String arrivalDate;
//    private String departureDate;
    private String tags;
    private String description;

    public Trip() {

    }

    public Trip(String trip_id, String user_id, String image, String city, int state, String tags, String description) {
        this.trip_id = trip_id;
        this.user_id = user_id;
        this.image = image;
        this.city = city;
        this.state = state;
        this.tags = tags;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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
                ", city='" + city + '\'' +
                ", state=" + state +
                ", tags='" + tags + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
