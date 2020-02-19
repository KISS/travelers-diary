package com.example.travelapp.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@IgnoreExtraProperties
public class TripSource {

    @SerializedName("_source")
    @Expose
    private Trip trip;

    public TripSource(Trip trip) {
        this.trip = trip;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
