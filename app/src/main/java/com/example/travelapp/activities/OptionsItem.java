package com.example.travelapp.activities;

public class OptionsItem {
    private String mOptionsNames;
    private int mOptionsImage;


    public OptionsItem(String optionName, int optionImage) {
        mOptionsNames = optionName;
        mOptionsImage = optionImage;

    }

    public String getOptionsName() {
        return mOptionsNames;
    }

    public int getOptionImage() {
        return mOptionsImage;
    }

}
