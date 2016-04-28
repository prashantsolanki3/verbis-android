package com.blackshift.verbis.ui.viewholders

import android.content.Context
import android.view.View
import android.widget.TextView
import com.blackshift.verbis.R
import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapViewHolder

/**
 * Created by Devika on 28-03-2016.
 */
class SuggestedWordsViewHolder(itemView: View, context: Context) : SnapViewHolder<String>(itemView, context) {

    internal var wordText: TextView

    init {
        wordText = itemView.findViewById(R.id.suggested_word) as TextView
    }

    override fun populateViewHolder(s: String, i: Int) {
        wordText.text = s
    }
}
