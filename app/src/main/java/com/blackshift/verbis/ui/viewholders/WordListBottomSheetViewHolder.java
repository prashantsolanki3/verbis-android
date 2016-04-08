package com.blackshift.verbis.ui.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.wordlist.WordList;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapViewHolder;

/**
 * Created by Devika on 08-04-2016.
 */
public class WordListBottomSheetViewHolder extends SnapViewHolder<WordList> {

    TextView textView;
    ImageView imageView;

    public WordListBottomSheetViewHolder(View itemView, Context context) {
        super(itemView, context);
        imageView = (ImageView) itemView.findViewById(R.id.icon);
        textView = (TextView) itemView.findViewById(R.id.title);
    }

    @Override
    public void populateViewHolder(WordList wordList, int i) {
        imageView.setImageDrawable(new IconDrawable(getContext(), MaterialIcons.md_collections)
                                    .actionBarSize());
        textView.setText(wordList.getTitle());
    }

}
