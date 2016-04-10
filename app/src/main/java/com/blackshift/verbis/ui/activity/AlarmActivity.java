package com.blackshift.verbis.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.Alarm;
import com.blackshift.verbis.rest.model.AlarmTopCard;
import com.blackshift.verbis.ui.viewholders.AlarmTopCardViewHolder;
import com.blackshift.verbis.ui.viewholders.AlarmViewHolder;

import java.util.ArrayList;

import io.github.prashantsolanki3.snaplibrary.snap.adapter.AbstractSnapSelectableAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.adapter.SnapAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.adapter.SnapSelectableAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.layout.wrapper.SnapLayoutWrapper;
import io.github.prashantsolanki3.snaplibrary.snap.layout.wrapper.SnapSelectableLayoutWrapper;
import io.github.prashantsolanki3.snaplibrary.snap.listeners.selection.SelectionListener;

public class AlarmActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.alarm_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<SnapLayoutWrapper> wrappers = new ArrayList<>();
        wrappers.add(new SnapSelectableLayoutWrapper(Alarm.class, AlarmViewHolder.class,R.layout.alarm_item,2,true));
        wrappers.add(new SnapSelectableLayoutWrapper(AlarmTopCard.class, AlarmTopCardViewHolder.class, R.layout.alarm_card, 1, false));

        SnapSelectableAdapter adapter =new SnapSelectableAdapter<>(this,wrappers,recyclerView, AbstractSnapSelectableAdapter.SelectionType.MULTIPLE_ON_LONG_PRESS);
        recyclerView.setAdapter(adapter);

        adapter.setOnSelectionListener(new SelectionListener() {
            @Override
            public void onSelectionModeEnabled(AbstractSnapSelectableAdapter.SelectionType selectionType) {

            }

            @Override
            public void onSelectionModeDisabled(AbstractSnapSelectableAdapter.SelectionType selectionType) {

            }

            @Override
            public void onItemSelected(Object o, int i) {

            }

            @Override
            public void onItemDeselected(Object o, int i) {

            }

            @Override
            public void onSelectionLimitReached() {

            }

            @Override
            public void onSelectionLimitExceeding() {

            }

            @Override
            public void onNoneSelected() {

            }
        });

        adapter.add(new AlarmTopCard());

        adapter.add(new Alarm());
        adapter.add(new Alarm());
        adapter.add(new Alarm());
    }

}
