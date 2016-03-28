package com.blackshift.verbis.ui.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.wordlist.Word;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.prashantsolanki3.snaplibrary.snap.adapter.AbstractSnapSelectableAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapSelectableViewHolder;

/**
 * Package com.blackshift.verbis.ui.viewholders
 * <p/>
 * Created by Prashant on 3/19/2016.
 * <p/>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
public class WordViewHolder extends SnapSelectableViewHolder<Word> {

    @Bind(R.id.wordlist_title)
    TextView title;
    /*@Bind(R.id.wordlist_item_count)
    TextView count;
*/

    public WordViewHolder(View itemView, Context context) {
        super(itemView, context);
        ButterKnife.bind(this, itemView);
    }

    public WordViewHolder(View itemView, Context context, AbstractSnapSelectableAdapter adapter) {
        super(itemView, context, adapter);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void populateViewHolder(Word wordList, int i) {
        title.setText(wordList.getHeadword());
  //      count.setText(wordList.getId());
    }

    @Override
    public void onSelectionEnabled(SnapSelectableViewHolder snapSelectableViewHolder, Word word, int i) {
        itemView.setBackgroundColor(getContext().getResources().getColor(android.R.color.darker_gray));
    }

    @Override
    public void onSelectionDisabled(SnapSelectableViewHolder snapSelectableViewHolder, Word word, int i) {
        itemView.setBackgroundColor(getContext().getResources().getColor(android.R.color.white));
    }

    @Override
    public void onSelected(SnapSelectableViewHolder snapSelectableViewHolder, Word word, int i) {
        itemView.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_blue_bright));
    }

    @Override
    public void onDeselected(SnapSelectableViewHolder snapSelectableViewHolder, Word word, int i) {
        itemView.setBackgroundColor(getContext().getResources().getColor(android.R.color.darker_gray));
    }
}
