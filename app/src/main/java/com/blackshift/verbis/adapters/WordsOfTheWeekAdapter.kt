package com.blackshift.verbis.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.blackshift.verbis.rest.model.verbismodels.WordOfTheDay
import com.blackshift.verbis.ui.fragments.WordOfTheDayFragment

/**
 *
 * Created by Devika on 11-03-2016.
 */
class WordsOfTheWeekAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    var result:List<WordOfTheDay>?

    init {
        result =null
    }

    fun setWords(res: List<WordOfTheDay>){
        result = res.sortedByDescending { it.date }
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        if(result!=null)
        return WordOfTheDayFragment.newInstance(result!![position])

        return Fragment()
    }

    override fun getCount(): Int {
        if(result!=null)
            if(result!!.size<=7)
                return result!!.size
            else
                return 7;

        return 0
    }
}
