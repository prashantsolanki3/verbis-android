package com.blackshift.verbis.ui.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.recyclerviewmodels.Meaning;

import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapViewHolder;

/**
 * Created by Devika on 02-04-2016.
 */
public class MeaningViewHolder extends SnapViewHolder<Meaning>{

    TextView meaning;

    public MeaningViewHolder(View itemView, Context context) {
        super(itemView, context);
        meaning = (TextView) itemView.findViewById(R.id.meaning_of_word);
    }

    @Override
    public void populateViewHolder(Meaning meaningAndExample, int i) {

        meaning.setText(null);
        meaning.setText(meaningAndExample.getMeaning());
        meaning.setPadding(100, 2, 2, 2);

        /*
        for (int j = 1; j < meaningLinearLayout.getChildCount(); j++){
            meaningLinearLayout.removeViewAt(j);
        }
        Log.d("after removing ", meaningLinearLayout.getChildCount() + "");
        for (String s : meaningAndExample.getExample()) {
            TextView textView = new TextView(getContext());
            textView.setPadding(200, 2, 2, 2);
            textView.setTextAppearance(getContext(), R.style.ExampleText);
            textView.setText(null);
            textView.setText(s);

            meaningLinearLayout.addView(textView);
        }
        TextView tv = (TextView) meaningLinearLayout.getChildAt(meaningLinearLayout.getChildCount() - 1);
        tv.setText(tv.getText() + "\n");
        Log.d("after adding ", meaningLinearLayout.getChildCount() + "");
    */
    }
}
