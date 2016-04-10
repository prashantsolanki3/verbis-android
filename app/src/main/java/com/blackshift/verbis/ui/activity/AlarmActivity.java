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

import io.github.prashantsolanki3.snaplibrary.snap.adapter.SnapAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.layout.wrapper.SnapLayoutWrapper;
import io.github.prashantsolanki3.snaplibrary.snap.layout.wrapper.SnapSelectableLayoutWrapper;

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
        wrappers.add(new SnapSelectableLayoutWrapper(AlarmTopCard.class, AlarmTopCardViewHolder.class, R.layout.alarm_card, 1, false));
        wrappers.add(new SnapSelectableLayoutWrapper(Alarm.class, AlarmViewHolder.class,R.layout.alarm_item,2,true));

        SnapAdapter adapter =new SnapAdapter<>(this,wrappers,recyclerView);
        recyclerView.setAdapter(adapter);

        adapter.add(new AlarmTopCard());

        adapter.add(new Alarm());
        adapter.add(new Alarm());
        adapter.add(new Alarm());
    }

}
