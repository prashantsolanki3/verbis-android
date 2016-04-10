package com.blackshift.verbis.ui.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Sarneet Singh on 4/8/2016.
 */
public class TimePickerFragment extends DialogFragment {
    int hour, minutes;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
       TimePickerDialog dialog= new TimePickerDialog(getContext(),timePickerListener!=null?timePickerListener:new TimePickerDialog.OnTimeSetListener() {
           @Override
           public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

           }
       },hour,minute,false);

        return dialog;
    }


    public void setTimePickerListener(TimePickerDialog.OnTimeSetListener timePickerListener) {
        this.timePickerListener = timePickerListener;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = null;

}
