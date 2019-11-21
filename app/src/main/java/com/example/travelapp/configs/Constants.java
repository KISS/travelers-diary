package com.example.travelapp.configs;

import android.Manifest;

public class Constants {
    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "uploads";

    public static final String DATABASE_PATH_USERS = "Users";
    public static final String DATABASE_PATH_TRAVELHISTORY = "TravelHistory";
    public static final String DATABASE_PATH_TRIPS = "Trips";
    public static final String DATABASE_PATH_Notifications = "Notifications";

    public static final String DATABASE_FIELD_TRIPID = "tripId";

    public static final String DATABASE_PATH_REVIEWS = "Reviews";

    public static final String[] MAP_IDS = new String[]{"US.MN", "US.MT",
            "US.ND", "US.ID", "US.WA", "US.AZ", "US.CA", "US.CO", "US.NV", "US.NM", "US.OR", "US.UT",
            "US.WY", "US.AR", "US.IA", "US.KS", "US.MO", "US.NE", "US.OK", "US.SD", "US.LA", "US.TX",
            "US.CT", "US.MA", "US.NH", "US.RI", "US.VT", "US.AL", "US.FL", "US.GA", "US.MS", "US.SC",
            "US.IL", "US.IN", "US.KY", "US.NC", "US.OH", "US.TN", "US.VA", "US.WI", "US.WY", "US.WV",
            "US.DE", "US.DC", "US.MD", "US.NJ", "US.NY", "US.PA", "US.ME", "US.HI", "US.AK", "US.MI"};

    public static final String[] MAP_NAMES = new String[]{"Minnesota", "Montana",
            "North Dakota", "Idaho", "Washington", "Arizona", "California", "Colorado", "Nevada", "New Mexico", "Oregon", "Utah",
            "Wyoming", "Arkansas", "Iowa", "Kansas", "Missouri", "Nebraska", "Oklahoma", "South Dakota", "Louisiana", "Texas",
            "Connecticut", "Massachusetts", "New Hampshire", "Rhode Island", "Vermont", "Alabama", "Florida", "Georgia", "Mississippi", "South Carolina",
            "Illinois", "Indiana", "Kentucky", "North Carolina", "Ohio", "Tennessee", "Virginia", "Wisconsin", "Wyoming", "West Virginia",
            "Delaware", "District of Columbia", "Maryland", "New Jersey", "New York", "Pennsylvania", "Maine", "Hawaii", "Alaska", "Michigan"};

    public static String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static String[] STORAGE_PERMISSION = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
}
