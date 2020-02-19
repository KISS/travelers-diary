package com.example.travelapp.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@IgnoreExtraProperties
public class UserHitsObject {

    @SerializedName("hits")
    @Expose
    private UserHitsList userHitsList;

    public UserHitsList getUserHitsList() {
        return userHitsList;
    }

    public void setUserHitsList(UserHitsList userHitsList) {
        this.userHitsList = userHitsList;
    }
}
