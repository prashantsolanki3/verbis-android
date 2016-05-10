package com.blackshift.verbis.ui.viewholders

import android.content.Context
import android.view.View
import android.widget.TextView

import com.blackshift.verbis.R
import com.blackshift.verbis.rest.model.recyclerviewmodels.Meaning

import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapViewHolder

/**
 * Created by Devika on 02-04-2016.
 */
class MeaningViewHolder(itemView: View, context: Context) : SnapViewHolder<Meaning>(itemView, context) {

    internal var meaning: TextView

    init {
        meaning = itemView.findViewById(R.id.meaning_of_word) as TextView
    }

    override fun populateViewHolder(meaningAndExample: Meaning, i: Int) {
        meaning.text = null
        meaning.text = meaningAndExample.meaning
        meaning.setPadding(100, 2, 2, 2)
    }
}
