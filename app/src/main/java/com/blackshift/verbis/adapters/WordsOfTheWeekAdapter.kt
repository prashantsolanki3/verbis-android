package com.blackshift.verbis.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.Log
import com.blackshift.verbis.rest.model.verbismodels.WordOfTheDay

import com.blackshift.verbis.ui.fragments.WordOfTheDayFragment
import io.realm.Realm
import io.realm.RealmResults

/**
 * Created by Devika on 11-03-2016.
 */
class WordsOfTheWeekAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    val realm: Realm
    val result: RealmResults<WordOfTheDay>

    init {
        realm = Realm.getDefaultInstance()
        result = realm.where(WordOfTheDay::class.java).findAll()
        Log.d("word count","${getCount()}")

        result.addChangeListener { notifyDataSetChanged() }
    }

    override fun getItem(position: Int): Fragment {
        return WordOfTheDayFragment.newInstance(result[position].word)
    }

    override fun getCount(): Int {
        return result.size
    }
}
