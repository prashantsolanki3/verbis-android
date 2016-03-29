package com.blackshift.verbis.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.blackshift.verbis.ui.fragments.WordListTitlesRecyclerFragment;

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
            //TODO: Use Xml.
            case 0: return "Wordlist";
            default: return "Wordlist";
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new WordListTitlesRecyclerFragment();
            default: return new WordListTitlesRecyclerFragment();
        }
    }

    @Override
    public int getCount() {
        return 1;
    }
}
