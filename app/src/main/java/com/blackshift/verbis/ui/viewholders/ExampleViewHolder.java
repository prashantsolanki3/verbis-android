package com.blackshift.verbis.ui.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.recyclerviewmodels.Example;

import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapViewHolder;

/**
 * Created by Devika on 04-05-2016.
 */
public class ExampleViewHolder extends SnapViewHolder<Example> {

    TextView exampleText;

    public ExampleViewHolder(View itemView, Context context) {
        super(itemView, context);
        exampleText = (TextView) itemView.findViewById(R.id.example);
    }

    @Override
    public void populateViewHolder(Example example, int i) {

        exampleText.setText(null);
        exampleText.setText(example.getExample());

    }

}
