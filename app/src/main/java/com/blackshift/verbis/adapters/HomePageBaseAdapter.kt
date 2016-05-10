package com.blackshift.verbis.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import com.blackshift.verbis.ui.fragments.WordListTitlesRecyclerFragment

/**
 * Created by Devika on 13-03-2016.
 */
class HomePageBaseAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
        //TODO: Use Xml.
            0 -> return "Wordlist"
            else -> return "Wordlist"
        }
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return WordListTitlesRecyclerFragment()
            else -> return WordListTitlesRecyclerFragment()
        }
    }

    override fun getCount(): Int {
        return 1
    }
}
