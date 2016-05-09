package com.blackshift.verbis.ui.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.blackshift.verbis.App;
import com.blackshift.verbis.R;
import com.blackshift.verbis.utils.manager.WordListManager;
import com.blackshift.verbis.utils.listeners.WordListListener;
import com.firebase.client.FirebaseError;

public class WordListRecyclerViewActivity extends VerbisActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WordListManager wordListManager = new WordListManager(App.getContext());
                    wordListManager.createWordList("Title" + SystemClock.elapsedRealtime(), new WordListListener() {
                        @Override
                        public void onSuccess(String s) {

                        }

                        @Override
                        public void onFailure(FirebaseError firebaseError) {
                            Toast.makeText(App.getContext(),firebaseError.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    }

}
