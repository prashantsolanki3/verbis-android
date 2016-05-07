package com.blackshift.verbis.utils.listeners;

import com.firebase.client.FirebaseError;

/**
 * Package com.blackshift.verbis.utils.listeners
 * <p/>
 * Created by Prashant on 5/7/2016.
 * <p/>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
public interface ExistenceListener {
    /**
     * Called if the execution was successful.
     *
     * */
    void onSuccess(boolean exists);

    /**
     * Called if the execution was unsuccessful.
     *
     * @param firebaseError object containing the information about the error.
     * */
    void onFailure(FirebaseError firebaseError);

}
