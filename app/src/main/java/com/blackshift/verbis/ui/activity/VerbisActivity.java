package com.blackshift.verbis.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.blackshift.verbis.App;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import static com.blackshift.verbis.App.getApp;

/**
 * Package com.blackshift.verbis.ui.activity
 * <p/>
 * Created by Prashant on 3/31/2016.
 * <p/>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
public abstract class VerbisActivity extends AppCompatActivity {

    //TODO: Implement lifecycle method logs.
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTracker = App.getApp().getDefaultTracker();

        App.getApp().getFirebase().child(".info").child("connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    onConnected();
                } else {
                    onDisconnected();
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {

            }
        });
    }

    public void onConnected(){}
    public void onDisconnected(){}

    public void callTracker(String screenName) {
        Log.i(screenName, "Setting screen name: " + screenName);
        mTracker.setScreenName("Activity~" + screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
