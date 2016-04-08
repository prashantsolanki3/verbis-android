package com.blackshift.verbis.utils.listeners;

import com.firebase.client.FirebaseError;

/**
 * Created by Devika on 29-03-2016.
 */
public abstract class OurDictionaryListener {
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
        public abstract void onFailure(FirebaseError firebaseError);
    }

