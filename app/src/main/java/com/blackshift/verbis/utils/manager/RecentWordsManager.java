package com.blackshift.verbis.utils.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blackshift.verbis.App;
import com.blackshift.verbis.rest.model.RecentWord;
import com.blackshift.verbis.utils.DateUtils;
import com.blackshift.verbis.utils.FirebaseKeys;
import com.blackshift.verbis.utils.listeners.RecentWordListListener;
import com.blackshift.verbis.utils.listeners.RecentWordListener;
import com.blackshift.verbis.utils.listeners.WordListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devika on 20-03-2016.
 */
public class RecentWordsManager {

    Context context;
    String string;

    public RecentWordsManager(Context context, String string) {
        this.context = context;
        this.string = string;
    }

    /**
     * @param word recent word to be added into the database
     * @param listener listener assist the handling of type of response received
     */
    public void addRecentWord(@NonNull String word, final RecentWordListener listener){

        //Go to RecentWords then push to create a new word
        Firebase firebase = getBaseFirebaseRef().push();

        final RecentWord recentWord = new RecentWord(firebase.getKey(), word,
                new Timestamp(DateUtils.getDateTimeUTC().getMillis()).getTime(),
                new Timestamp(DateUtils.getDateTimeUTC().getMillis()).getTime());

        firebase.setValue(recentWord, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                handleListener(firebaseError, recentWord, listener);
            }
        });

    }

    public void getRecentWords(final RecentWordListListener listener){
        Firebase firebase = getBaseFirebaseRef();
        Query base = firebase.orderByChild("modifiedAt");

        base.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<RecentWord> recentWords = new ArrayList<>();
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        RecentWord recentWord = snapshot.getValue(RecentWord.class);
                        recentWords.add(recentWord);
                    }
                    listener.onSuccess(recentWords);
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(FirebaseError.fromException(e));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onFailure(firebaseError);
            }
        });
    }

    public void deleteAllWords(final WordListener listener){
        Firebase firebase = getBaseFirebaseRef();
        firebase.removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                handleDeleteListener(firebaseError, firebase.getPath().toString(), listener);
            }
        });
    }

    private void handleDeleteListener(FirebaseError firebaseError, String string, WordListener listener) {
        if(firebaseError!=null){
            //Some Error has occurred.
            if (listener!=null)
                listener.onFailure(firebaseError);
        }else{
            //List saved successfully.
            if(listener!=null)
                listener.onSuccess(string);
        }
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
        if (string.equals(FirebaseKeys.RECENT_WORDS)){
            return firebase.child(FirebaseKeys.RECENT_WORDS).child(firebase.getAuth().getUid());
        }else{
            return firebase.child(FirebaseKeys.WORDS_NOT_FOUND);
        }
    }
}
