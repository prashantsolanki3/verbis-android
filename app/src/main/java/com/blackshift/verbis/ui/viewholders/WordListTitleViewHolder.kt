package com.blackshift.verbis.ui.viewholders

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.blackshift.verbis.R
import com.blackshift.verbis.rest.model.wordlist.WordList
import com.blackshift.verbis.utils.annotations.PrivacyLevel
import com.joanzapata.iconify.IconDrawable
import com.joanzapata.iconify.fonts.MaterialIcons
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
    internal val star: ImageView
    internal val privacy: ImageView

    constructor(itemView: View, context: Context) : super(itemView, context) {
        title = itemView.findViewById(R.id.wordlist_title) as TextView
        star = itemView.findViewById(R.id.wordlist_title_start) as ImageView
        privacy = itemView.findViewById(R.id.wordlist_title_privacy) as ImageView
    }

    constructor(itemView: View, context: Context, adapter: AbstractSnapSelectableAdapter<Any>) : super(itemView, context, adapter) {
        title = itemView.findViewById(R.id.wordlist_title) as TextView
        star = itemView.findViewById(R.id.wordlist_title_start) as ImageView
        privacy = itemView.findViewById(R.id.wordlist_title_privacy) as ImageView
    }

    override fun populateViewHolder(wordList: WordList, i: Int) {
        title.text = wordList.title
        setStarIcon(wordList.isStarred)
        setPrivacyIcon(wordList.privacy)
    }

    fun setPrivacyIcon(privacyLevel: Int){
        val privacyDrawable: MaterialIcons

        when(privacyLevel){
            PrivacyLevel.PUBLIC -> privacyDrawable = MaterialIcons.md_language

            PrivacyLevel.PRIVATE -> privacyDrawable = MaterialIcons.md_lock_outline

            PrivacyLevel.FRIENDS -> privacyDrawable = MaterialIcons.md_people_outline

            else -> privacyDrawable = MaterialIcons.md_language
        }

        privacy.setImageDrawable(IconDrawable(context,privacyDrawable).colorRes(android.R.color.darker_gray).sizeDp(16))

    }

    fun setStarIcon(isStarred: Boolean = false){
        if (isStarred)
            star.setImageDrawable(IconDrawable(context,MaterialIcons.md_star).colorRes(android.R.color.darker_gray).sizeDp(16))
        else
            star.visibility = View.GONE
    }

    //TODO: Complete this
    override fun onSelectionEnabled(snapSelectableViewHolder: SnapSelectableViewHolder<Any>, wordList: WordList, i: Int) {
        //ItemView is a CardView, use setCardBackgroundColor()
        //itemView.setBackgroundColor(context.resources.getColor(android.R.color.darker_gray))
    }

    override fun onSelectionDisabled(snapSelectableViewHolder: SnapSelectableViewHolder<Any>, wordList: WordList, i: Int) {
        //itemView.setBackgroundColor(context.resources.getColor(android.R.color.white))
    }

    override fun onSelected(snapSelectableViewHolder: SnapSelectableViewHolder<Any>, wordList: WordList, i: Int) {
        //itemView.setBackgroundColor(context.resources.getColor(android.R.color.holo_blue_bright))
    }

    override fun onDeselected(snapSelectableViewHolder: SnapSelectableViewHolder<Any>, wordList: WordList, i: Int) {
        //itemView.setBackgroundColor(context.resources.getColor(android.R.color.darker_gray))
    }
}
