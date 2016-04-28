package com.blackshift.verbis.ui.viewholders;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.recyclerviewmodels.MeaningAndExample;

import io.github.prashantsolanki3.snaplibrary.snap.adapter.AbstractSnapSelectableAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapSelectableViewHolder;

/**
 * Created by Devika on 02-04-2016.
 */
public class MeaningExampleViewHolder extends SnapSelectableViewHolder<MeaningAndExample>{

    TextView pos;
    TextView meaning;
    LinearLayout meaningLinearLayout;

    public MeaningExampleViewHolder(View itemView, Context context, AbstractSnapSelectableAdapter adapter) {
        super(itemView, context, adapter);
    }

    public MeaningExampleViewHolder(View itemView, Context context) {
        super(itemView, context);

        meaning = (TextView) itemView.findViewById(R.id.meaning_of_word);
        meaningLinearLayout = (LinearLayout) itemView.findViewById(R.id.meaning_layout);
    }

    @Override
    public void onSelectionEnabled(SnapSelectableViewHolder snapSelectableViewHolder, MeaningAndExample meaningAndExample, int i) {

    }

    @Override
    public void onSelectionDisabled(SnapSelectableViewHolder snapSelectableViewHolder, MeaningAndExample meaningAndExample, int i) {

    }

    @Override
    public void onSelected(SnapSelectableViewHolder snapSelectableViewHolder, MeaningAndExample meaningAndExample, int i) {

    }

    @Override
    public void onDeselected(SnapSelectableViewHolder snapSelectableViewHolder, MeaningAndExample meaningAndExample, int i) {

    }

    @Override
    public void populateViewHolder(MeaningAndExample meaningAndExample, int i) {
        //TODO: This is nor the right way didi
        meaning.setText(null);
        meaning.setText(meaningAndExample.getMeaning());
        meaning.setPadding(100, 2, 2, 2);
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
    }
}
