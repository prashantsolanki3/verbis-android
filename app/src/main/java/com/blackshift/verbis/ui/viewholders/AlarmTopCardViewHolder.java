package com.blackshift.verbis.ui.viewholders;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blackshift.verbis.App;
import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.AlarmTopCard;
import com.blackshift.verbis.ui.activity.HomePageActivity;
import com.blackshift.verbis.ui.fragments.TimePickerFragment;

import java.util.Calendar;

import io.github.prashantsolanki3.snaplibrary.snap.adapter.AbstractSnapSelectableAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapSelectableViewHolder;
import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapViewHolder;

/**
 * Created by Sarneet Singh on 4/6/2016.
 */
public class AlarmTopCardViewHolder extends SnapSelectableViewHolder<AlarmTopCard> {

        TextView alarmTime;
        TimePickerFragment timePicker;
        Button saveAlarmButton;

        static final int DIALOG_ID=0;
        int hour;
        int minute;
    public AlarmTopCardViewHolder(View itemView, Context context, AbstractSnapSelectableAdapter adapter) {

        super(itemView, context, adapter);
        alarmTime = (TextView) itemView.findViewById(R.id.alarm_time);
        timePicker = new TimePickerFragment();
        /********* display current time on screen Start ********/

        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);

        // set current time into output textview
        updateTime(hour, minute);

        /********* display current time on screen End ********/


    }

    public AlarmTopCardViewHolder(View itemView, Context context) {
        super(itemView, context);
        alarmTime = (TextView) itemView.findViewById(R.id.alarm_time);
        saveAlarmButton = (Button) itemView.findViewById(R.id.save_alarm_btn);
        timePicker = new TimePickerFragment();
        /********* display current time on screen Start ********/

        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);

        // set current time into output textview
        updateTime(hour, minute);

        /********* display current time on screen End ********/

    }

    @Override
    public void onSelectionEnabled(SnapSelectableViewHolder snapSelectableViewHolder, AlarmTopCard alarmTopCard, int i) {

    }

    @Override
    public void onSelectionDisabled(SnapSelectableViewHolder snapSelectableViewHolder, AlarmTopCard alarmTopCard, int i) {

    }

    @Override
    public void onSelected(SnapSelectableViewHolder snapSelectableViewHolder, AlarmTopCard alarmTopCard, int i) {

    }

    @Override
    public void onDeselected(SnapSelectableViewHolder snapSelectableViewHolder, AlarmTopCard alarmTopCard, int i) {

    }

    @Override
    public void populateViewHolder(AlarmTopCard alarmTopCard, int i) {

    }
    @Override
    public void attachOnClickListeners(SnapViewHolder viewHolder, AlarmTopCard item, int pos) {
        alarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timePicker.setTimePickerListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                        hour = hourOfDay;
                        minute = minutes;
                        updateTime(hour, minutes);
                    }
                });
                timePicker.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), "timePicker");
            }
        });

        saveAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    void updateTime(int hours, int mins){
        String timeSet="";
        if(hours>12){
            hours -= 12;
            timeSet="PM";
        }
        else if(hours<12){
            timeSet ="AM";
        } else if(hours==0){
            timeSet = "AM";
            hours+=12;
        }
        else
            timeSet ="AM";

        String minutes="";
        if(mins<10)
            minutes = "0"+mins;
        else
            minutes=String.valueOf(mins);

        String atime = new StringBuilder().append(hours).append(":").append(minutes).append(" ").append(timeSet).toString();
        Log.d("TIME RESULT", atime);
        alarmTime.setText(atime);


    }

}
