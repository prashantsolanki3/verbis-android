package com.blackshift.verbis.ui.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.wordlist.WordList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapViewHolder;

/**
 * Package com.blackshift.verbis.ui.viewholders
 * <p/>
 * Created by Prashant on 3/19/2016.
 * <p/>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
public class WordListTitleViewHolder extends SnapViewHolder<WordList> {

    @Bind(R.id.wordlist_title)
    TextView title;
    @Bind(R.id.wordlist_item_count)
    TextView count;

    public WordListTitleViewHolder(View itemView, Context context) {
        super(itemView, context);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void populateViewHolder(WordList wordList, int i) {
        title.setText(wordList.getTitle());
        count.setText(wordList.getId());
    }
}
