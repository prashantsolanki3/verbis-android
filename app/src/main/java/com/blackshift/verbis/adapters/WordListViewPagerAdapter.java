package com.blackshift.verbis.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.blackshift.verbis.rest.model.wordlist.WordList;
import com.blackshift.verbis.ui.fragments.WordListFragment;

import java.util.ArrayList;
import java.util.List;

/**
     * A  that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class WordListViewPagerAdapter extends FragmentPagerAdapter {

        List<WordList> wordLists;
        Context context;

        public WordListViewPagerAdapter(FragmentManager fm, Context context) {
            this(fm,new ArrayList<WordList>(),context);
        }

        public WordListViewPagerAdapter(FragmentManager fm, List<WordList> wordLists, Context context) {
            super(fm);
            this.wordLists = wordLists;
            this.context = context;
        }

        public void set(List<WordList> wordLists){
            this.wordLists.clear();
            this.wordLists = wordLists;
            this.notifyDataSetChanged();
        }

        public void add(WordList wordList){
            this.wordLists.add(wordList);
            this.notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a WordListFragment (defined as a static inner class below).
            return WordListFragment.newInstance(wordLists.get(position).getId());
        }

        @Override
        public int getCount() {
            if(wordLists!=null)
                return wordLists.size();

            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(getCount()>position&&wordLists!=null)
                return wordLists.get(position).getTitle();

            return "";
        }

        //TODO: Should not refresh all the Fragments.
        @Override
        public int getItemPosition(Object object) {
            //To update fragment every time user navigates to it.
            return POSITION_NONE;
        }

        @Override
        public float getPageWidth(int position) {
            return 0.90f;
        }
    }