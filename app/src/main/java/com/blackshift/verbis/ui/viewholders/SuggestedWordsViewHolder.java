package com.blackshift.verbis.ui.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.blackshift.verbis.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapViewHolder;

/**
 * Created by Devika on 28-03-2016.
 */
public class SuggestedWordsViewHolder extends SnapViewHolder<String> {

    @Bind(R.id.suggested_word)
    public TextView wordText;

    public SuggestedWordsViewHolder(View itemView, Context context) {
        super(itemView, context);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void populateViewHolder(String s, int i) {
        wordText.setText(s);
    }
}
