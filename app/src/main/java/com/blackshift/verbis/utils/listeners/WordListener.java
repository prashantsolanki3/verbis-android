package com.blackshift.verbis.utils.listeners;

import com.google.firebase.database.DatabaseError;

/**
 * Package com.blackshift.verbis.utils.listeners
 * <p/>
 * Created by Prashant on 3/19/2016.
 * <p/>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
public abstract class WordListener {

    /**
     * Called if the execution was successful.
     *
     * */
    public abstract void onSuccess(String firebaseReferenceString);

    /**
     * Called if the execution was unsuccessful.
     *
     * @param firebaseError object containing the information about the error.
     * */
    public abstract void onFailure(DatabaseError firebaseError);
}
