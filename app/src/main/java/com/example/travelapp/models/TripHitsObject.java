package com.example.travelapp.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@IgnoreExtraProperties
public class TripHitsObject {
    @SerializedName("hits")
    @Expose
    private TripHitsList tripHitsList;

    public TripHitsList getTripHitsList() {
        return tripHitsList;
    }

    public void setTripHitsList(TripHitsList tripHitsList) {
        this.tripHitsList = tripHitsList;
    }
}
