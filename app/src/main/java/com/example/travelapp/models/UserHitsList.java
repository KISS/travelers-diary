package com.example.travelapp.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@IgnoreExtraProperties
public class UserHitsList {

    @SerializedName("hits")
    @Expose
    private List<UserSource> userIndex;

    public List<UserSource> getUserIndex() {
        return userIndex;
    }

    public void setUserIndex(List<UserSource> userIndex) {
        this.userIndex = userIndex;
    }
}
