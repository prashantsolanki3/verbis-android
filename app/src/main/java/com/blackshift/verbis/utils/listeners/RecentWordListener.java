package com.blackshift.verbis.utils.listeners;

import com.blackshift.verbis.rest.model.RecentWord;
import com.google.firebase.database.DatabaseError;

/**
 * Created by Devika on 28-03-2016.
 */
public abstract class RecentWordListener {
    /**
     * Called if the execution was successful.
     *
     * */
    public abstract void onSuccess(RecentWord word);

    /**
     * Called if the execution was unsuccessful.
     *
     * @param firebaseError object containing the information about the error.
     * */
    public abstract void onFailure(DatabaseError firebaseError);
}
