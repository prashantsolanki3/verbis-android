package com.blackshift.verbis.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.blackshift.verbis.App;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
