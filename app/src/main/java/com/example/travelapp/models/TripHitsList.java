package com.example.travelapp.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@IgnoreExtraProperties
public class TripHitsList {
    @SerializedName("hits")
    @Expose
    private List<TripSource> tripIndex;

    public List<TripSource> getTripIndex() {
        return tripIndex;
    }

    public void setTripIndex(List<TripSource> tripIndex) {
        this.tripIndex = tripIndex;
    }
}
