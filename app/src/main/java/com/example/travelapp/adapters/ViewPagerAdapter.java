package com.example.travelapp.adapters;

import java.util.List;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mPages;
    private List<String> mTitles;

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        mPages = new ArrayList<>();
        mTitles = new ArrayList<>();
    }

    public void addFragment(Fragment page, String title) {
        mPages.add(page);
        mTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mPages.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public int getCount() {
        return mPages.size();
    }
}
