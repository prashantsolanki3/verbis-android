package com.blackshift.verbis.utils.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blackshift.verbis.App;
import com.blackshift.verbis.rest.model.RecentWord;
import com.blackshift.verbis.utils.DateUtils;
import com.blackshift.verbis.utils.keys.FirebaseKeys;
import com.blackshift.verbis.utils.listeners.RecentWordListListener;
import com.blackshift.verbis.utils.listeners.RecentWordListener;
import com.blackshift.verbis.utils.listeners.WordListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
        DatabaseReference firebase = getBaseFirebaseRef().push();
        
        final RecentWord recentWord = new RecentWord(firebase.getKey(), word,
                new Timestamp(DateUtils.getDateTimeUTC().getMillis()).getTime(),
                new Timestamp(DateUtils.getDateTimeUTC().getMillis()).getTime());

        firebase.setValue(recentWord, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                handleListener(firebaseError, recentWord, listener);
            }
        });

    }

    public void getRecentWords(final RecentWordListListener listener){
        DatabaseReference firebase = getBaseFirebaseRef();
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
                    listener.onFailure(DatabaseError.fromException(e));
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                listener.onFailure(firebaseError);
            }
        });
    }

    /*public void deleteAllWords(final WordListener listener){
        DatabaseReference firebase = getBaseFirebaseRef();
        firebase.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                handleDeleteListener(firebaseError, firebase.getPath().toString(), listener);
            }
        });
    }*/

    private void handleDeleteListener(DatabaseError firebaseError, String string, WordListener listener) {
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

    private void handleListener(DatabaseError firebaseError,@Nullable RecentWord word, RecentWordListener listener){
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

    private DatabaseReference getBaseFirebaseRef(){
        DatabaseReference firebase = App.getApp().getFirebaseDatabase();
        if (string.equals(FirebaseKeys.RECENT_WORDS)){
            return firebase.child(FirebaseKeys.RECENT_WORDS).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }else{
            return firebase.child(FirebaseKeys.WORDS_NOT_FOUND);
        }
    }
}
