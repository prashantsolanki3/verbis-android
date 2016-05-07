package com.blackshift.verbis.ui.viewholders

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.blackshift.verbis.R
import com.blackshift.verbis.rest.model.wordlist.WordList
import com.joanzapata.iconify.IconDrawable
import com.joanzapata.iconify.fonts.MaterialIcons

import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapViewHolder

/**
 * Created by Devika on 08-04-2016.
 */
class WordListBottomSheetViewHolder(itemView: View, context: Context) : SnapViewHolder<WordList>(itemView, context) {

    internal var textView: TextView
    internal var imageView: ImageView

    init {
        imageView = itemView.findViewById(R.id.icon) as ImageView
        textView = itemView.findViewById(R.id.title) as TextView
    }

    override fun populateViewHolder(wordList: WordList, i: Int) {
        imageView.setImageDrawable(IconDrawable(context, MaterialIcons.md_format_list_bulleted).actionBarSize().colorRes(android.R.color.darker_gray))
        textView.text = wordList.title
    }

}
