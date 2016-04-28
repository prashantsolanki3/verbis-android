package com.blackshift.verbis.ui.viewholders

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView

import com.blackshift.verbis.R

import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapViewHolder

/**
 * Created by Devika on 02-04-2016.
 */
class PartOfSpeechViewHolder(itemView: View, context: Context) : SnapViewHolder<String>(itemView, context) {

    internal var partOfSpeech: TextView

    init {
        partOfSpeech = itemView.findViewById(R.id.part_of_speech) as TextView
    }

    override fun populateViewHolder(s: String, i: Int) {
        Log.d("tag in vh", s)
        partOfSpeech.text = s
    }
}
