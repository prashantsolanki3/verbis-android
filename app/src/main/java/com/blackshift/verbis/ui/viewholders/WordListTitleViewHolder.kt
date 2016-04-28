package com.blackshift.verbis.ui.viewholders

import android.content.Context
import android.view.View
import android.widget.TextView

import com.blackshift.verbis.R
import com.blackshift.verbis.rest.model.wordlist.WordList

import io.github.prashantsolanki3.snaplibrary.snap.adapter.AbstractSnapSelectableAdapter
import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapSelectableViewHolder

/**
 * Package com.blackshift.verbis.ui.viewholders
 *
 *
 * Created by Prashant on 3/19/2016.
 *
 *
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
class WordListTitleViewHolder : SnapSelectableViewHolder<WordList> {

    internal val title: TextView
    internal val count: TextView

    constructor(itemView: View, context: Context) : super(itemView, context) {
        title = itemView.findViewById(R.id.wordlist_title) as TextView
        count = itemView.findViewById(R.id.wordlist_item_count) as TextView
    }

    constructor(itemView: View, context: Context, adapter: AbstractSnapSelectableAdapter<Any>) : super(itemView, context, adapter) {
        title = itemView.findViewById(R.id.wordlist_title) as TextView
        count = itemView.findViewById(R.id.wordlist_item_count) as TextView
    }

    override fun populateViewHolder(wordList: WordList, i: Int) {
        title.text = wordList.title
        count.text = wordList.id
    }

    //TODO: Complete this
    override fun onSelectionEnabled(snapSelectableViewHolder: SnapSelectableViewHolder<Any>, wordList: WordList, i: Int) {
        itemView.setBackgroundColor(context.resources.getColor(android.R.color.darker_gray))
    }

    override fun onSelectionDisabled(snapSelectableViewHolder: SnapSelectableViewHolder<Any>, wordList: WordList, i: Int) {
        itemView.setBackgroundColor(context.resources.getColor(android.R.color.white))
    }

    override fun onSelected(snapSelectableViewHolder: SnapSelectableViewHolder<Any>, wordList: WordList, i: Int) {
        itemView.setBackgroundColor(context.resources.getColor(android.R.color.holo_blue_bright))
    }

    override fun onDeselected(snapSelectableViewHolder: SnapSelectableViewHolder<Any>, wordList: WordList, i: Int) {
        itemView.setBackgroundColor(context.resources.getColor(android.R.color.darker_gray))
    }
}
