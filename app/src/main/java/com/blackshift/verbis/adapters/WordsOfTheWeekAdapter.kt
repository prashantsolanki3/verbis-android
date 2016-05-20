package com.blackshift.verbis.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import com.blackshift.verbis.rest.model.verbismodels.WordOfTheDay
import com.blackshift.verbis.ui.fragments.WordOfTheDayFragment
import com.crashlytics.android.Crashlytics

/**
 *
 * Created by Devika on 11-03-2016.
 */
class WordsOfTheWeekAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    var result:List<WordOfTheDay>?

    init {
        result = arrayListOf()
    }

    fun setWords(res: List<WordOfTheDay>){
        result = res.sortedByDescending { it.date }
        try {
            notifyDataSetChanged()
        }catch(e:IllegalStateException){
            e.printStackTrace()
            Crashlytics.logException(e)
        }
    }

    override fun getItem(position: Int): Fragment {
        return WordOfTheDayFragment.newInstance(result!![position])
    }

    override fun getCount(): Int {
            if(result!!.size<=7)
                return result!!.size
            else
                return 7;
    }

    override fun getItemPosition(`object`: Any?): Int {
        //To update fragment every time user navigates to it.
        return PagerAdapter.POSITION_NONE
    }
}
