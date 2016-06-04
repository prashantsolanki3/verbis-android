package com.blackshift.verbis.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import com.blackshift.verbis.ui.fragments.WordListTitlesRecyclerFragment
import com.blackshift.verbis.utils.annotations.PrivacyLevel

/**
 * Created by Devika on 13-03-2016.
 */
class HomePageBaseAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
        //TODO: Use Xml.
            0 -> return "Wordlists"
            1 -> return "Your Wordlists"
            else -> return "Wordlist"
        }
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return WordListTitlesRecyclerFragment.newInstance(PrivacyLevel.PUBLIC)
            1 -> return WordListTitlesRecyclerFragment.newInstance(PrivacyLevel.PRIVATE)
            else -> return WordListTitlesRecyclerFragment.newInstance(PrivacyLevel.PRIVATE)
        }
    }

    override fun getCount(): Int {
        return 2
    }
}
