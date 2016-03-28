package com.blackshift.verbis.utils.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blackshift.verbis.App;
import com.blackshift.verbis.rest.model.RecentWord;
import com.blackshift.verbis.utils.FirebaseKeys;
import com.blackshift.verbis.utils.listeners.RecentWordListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by Devika on 20-03-2016.
 */
public class RecentWordsManager {

    Context context;

    public RecentWordsManager(Context context) {
        this.context = context;
    }

    /**
     * @param word recent word to be added into the database
     * @param listener listener assist the handling of type of response received
     */
    public void addRecentWord(@NonNull String word, final RecentWordListener listener){

        //Go to RecentWords then push to create a new word
        Firebase firebase = getBaseFirebaseRef().push();

        final RecentWord recentWord = new RecentWord(firebase.getKey(), word);

        firebase.setValue(recentWord, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                handleListener(firebaseError, recentWord, listener);
            }
        });

    }

    private void handleListener(FirebaseError firebaseError,@Nullable RecentWord word, RecentWordListener listener){
        if(firebaseError!=null){
            //Some Error has occurred.
            if (listener!=null)
                listener.onFailure(firebaseError);
        }else{
            //List saved successfully.
            if(listener!=null)
                listener.onSuccess(word);
        }
    }

    private Firebase getBaseFirebaseRef(){
        Firebase firebase = App.getApp().getFirebase();
        return firebase.child(FirebaseKeys.RECENT_WORDS).child(firebase.getAuth().getUid());
    }

}
