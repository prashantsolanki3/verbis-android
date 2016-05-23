package com.blackshift.verbis.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.blackshift.verbis.rest.model.wordlist.WordList
import com.blackshift.verbis.ui.fragments.WordListFragment
import java.util.*

/**
 * A  that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class WordListViewPagerAdapter(fm: FragmentManager, internal var wordLists:

MutableList<WordList>?, internal var context: Context) : FragmentStatePagerAdapter(fm) {

    constructor(fm: FragmentManager, context: Context) : this(fm, ArrayList<WordList>(), context) {
    }

    fun set(wordLists: MutableList<WordList>) {
        this.wordLists!!.clear()
        this.wordLists = wordLists
        this.notifyDataSetChanged()
    }

    fun add(wordList: WordList) {
        this.wordLists!!.add(wordList)
        this.notifyDataSetChanged()
    }

    operator fun get(pos: Int): WordList {
        return this.wordLists!![pos]
    }

    val all: List<WordList>?
        get() = this.wordLists

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a WordListFragment (defined as a static inner class below).
            return WordListFragment.newInstance(wordLists!![position].id)
    }

    override fun getCount(): Int {
        var size = 0
        if (wordLists != null)
            size = wordLists!!.size

        return size

    }

    override fun getPageTitle(position: Int): CharSequence {
        if (count > position && wordLists != null)
            return wordLists!![position].title

        return ""
    }

    //TODO: Should not refresh all the Fragments.
    override fun getItemPosition(`object`: Any?): Int {
        //To update fragment every time user navigates to it.
        return PagerAdapter.POSITION_NONE
    }

    override fun getPageWidth(position: Int): Float {
        return 0.95f
    }
}