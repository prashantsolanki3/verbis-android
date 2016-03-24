package com.blackshift.verbis.utils.listeners;

import android.support.annotation.Nullable;

import com.blackshift.verbis.rest.model.wordlist.WordList;
import com.firebase.client.FirebaseError;

import java.util.List;

/**
 * Package com.blackshift.verbis.utils.listeners
 * <p/>
 * Created by Prashant on 3/19/2016.
 * <p/>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
public abstract class WordListArrayListener {

    /**
     * Called if the execution was successful.
     *
     * @param wordList object containing the information about the wordlist on which the current operation (create, update) was performed.
     *                 or
     *                 null if the wordlist was deleted.
     * */
    public abstract void onSuccess(@Nullable List<WordList> wordList);

    /**
     * Called if the execution was unsuccessful.
     *
     * @param firebaseError object containing the information about the error.
     * */
    public abstract void onFailure(FirebaseError firebaseError);
}
