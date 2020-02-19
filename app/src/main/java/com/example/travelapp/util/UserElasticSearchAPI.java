package com.example.travelapp.util;

import com.example.travelapp.models.TripHitsObject;
import com.example.travelapp.models.UserHitsObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;

public interface UserElasticSearchAPI {

    @GET("_search/")
    Call<UserHitsObject> search(
            @HeaderMap Map<String, String> headers,
            @Query("default_operator") String operator,
            // retrofit automatically prepend a "?" to the url to the 1st query
            @Query("q") String query
            // prepends || appends a "&" to the queries after
    );

}
