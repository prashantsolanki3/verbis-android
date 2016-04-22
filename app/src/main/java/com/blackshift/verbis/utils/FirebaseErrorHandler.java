package com.blackshift.verbis.utils;

import com.firebase.client.FirebaseError;

/**
 * Created by Sarneet Singh on 4/20/2016.
 */
public class FirebaseErrorHandler {
    private FirebaseError firebaseError;
    private String message;

    public FirebaseErrorHandler(){

    }

    public FirebaseErrorHandler(FirebaseError fbaseError){
        firebaseError =fbaseError;
    }

    public String checkErrorCode(){
        switch (firebaseError.getCode()){
            case FirebaseError.INVALID_CREDENTIALS: message = "Invalid credentials, Contact your account provider for more details";break;
            case FirebaseError.NETWORK_ERROR: message = "Network Error. Please connect to the Internet";break;
            case FirebaseError.LIMITS_EXCEEDED: message = "Max Limits attained. Kindly try again after some time"; break;
            case FirebaseError.PERMISSION_DENIED: message="Permission Denied"; break;
            case FirebaseError.PROVIDER_ERROR: message="Some error occured. Contact your account provider"; break;
            case FirebaseError.UNKNOWN_ERROR: message="Unknown error occured";break;
            case FirebaseError.EXPIRED_TOKEN: message = "Expired Token. Kindly login again"; break;
            default:message="Some error occured. Please try again after sometime"; break;
        }
        return  message;
    }
}
