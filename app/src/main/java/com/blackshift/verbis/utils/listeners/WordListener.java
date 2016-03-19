package com.blackshift.verbis.utils.listeners;

import android.support.annotation.Nullable;

import com.firebase.client.FirebaseError;

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
     * @param word object containing the information about the word the current operation (create, update) was performed on.
     *                 or
     *                 null if the wordlist was deleted.
     * */
    public abstract void onSuccess(@Nullable Object word);

    /**
     * Called if the execution was unsuccessful.
     *
     * @param firebaseError object containing the information about the error.
     * */
    public abstract void onFailure(FirebaseError firebaseError);
}
