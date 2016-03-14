package com.blackshift.verbis;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Devika on 13-03-2016.
 */
public class HomePageBaseAdapter extends FragmentStatePagerAdapter {

    public HomePageBaseAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Tab 1";
            case 1: return "Tab 2";
            default: return "Tab 1";
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return HomePageTab1Fragment.newInstance("", "");
            case 1: return HomePageTab2Fragment.newInstance("", "");
            default: return HomePageTab1Fragment.newInstance("", "");
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
