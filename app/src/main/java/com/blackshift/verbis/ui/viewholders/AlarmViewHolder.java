package com.blackshift.verbis.ui.viewholders;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.Alarm;

import io.github.prashantsolanki3.snaplibrary.snap.adapter.AbstractSnapSelectableAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapSelectableViewHolder;
import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapViewHolder;

/**
 * Created by Sarneet Singh on 4/6/2016.
 */
public class AlarmViewHolder extends SnapSelectableViewHolder<Alarm> {

    final TextView time;

    public AlarmViewHolder(View itemView, Context context, AbstractSnapSelectableAdapter adapter) {
        super(itemView, context, adapter);
        time = (TextView) itemView.findViewById(R.id.alarm_time);
    }

    public AlarmViewHolder(View itemView, Context context) {
        super(itemView, context);
        time = (TextView) itemView.findViewById(R.id.alarm_time);
    }

    @Override
    public void populateViewHolder(Alarm alarm, int i) {

        time.setText("Position: "+i+1);
    }

    @Override
    public void attachOnClickListeners(SnapViewHolder viewHolder, Alarm item, int pos) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),time.getText(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSelectionEnabled(SnapSelectableViewHolder snapSelectableViewHolder, Alarm alarm, int i) {
        ((CardView)itemView).setCardBackgroundColor(getContext().getResources().getColor(android.R.color.darker_gray));
    }

    @Override
    public void onSelectionDisabled(SnapSelectableViewHolder snapSelectableViewHolder, Alarm alarm, int i) {
        ((CardView)itemView).setCardBackgroundColor(getContext().getResources().getColor(android.R.color.white));
    }

    @Override
    public void onSelected(SnapSelectableViewHolder snapSelectableViewHolder, Alarm alarm, int i) {
        ((CardView)itemView).setCardBackgroundColor(getContext().getResources().getColor(android.R.color.holo_blue_bright));
    }

    @Override
    public void onDeselected(SnapSelectableViewHolder snapSelectableViewHolder, Alarm alarm, int i) {
        ((CardView)itemView).setCardBackgroundColor(getContext().getResources().getColor(android.R.color.darker_gray));
    }
}
