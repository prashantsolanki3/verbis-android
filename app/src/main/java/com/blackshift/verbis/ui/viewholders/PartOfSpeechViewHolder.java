package com.blackshift.verbis.ui.viewholders;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blackshift.verbis.R;

import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapViewHolder;

/**
 * Created by Devika on 02-04-2016.
 */
public class PartOfSpeechViewHolder extends SnapViewHolder<String>{

    TextView partOfSpeech;

    public PartOfSpeechViewHolder(View itemView, Context context) {
        super(itemView, context);
        partOfSpeech = (TextView) itemView.findViewById(R.id.part_of_speech);
    }

    @Override
    public void populateViewHolder(String s, int i) {
        Log.d("tag in vh", s);
        partOfSpeech.setText(s);
    }
}
