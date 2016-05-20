package com.blackshift.verbis.utils.listeners;

import com.blackshift.verbis.rest.model.RecentWord;
import com.google.firebase.database.DatabaseError;

import java.util.List;

/**
 * Created by Devika on 07-04-2016.
 */
public abstract class RecentWordListListener {
    /**
     * Called if the execution was successful.
     *
     * */
    public abstract void onSuccess(List<RecentWord> words);

    /**
     * Called if the execution was unsuccessful.
     *
     * @param firebaseError object containing the information about the error.
     * */
    public abstract void onFailure(DatabaseError firebaseError);
}
