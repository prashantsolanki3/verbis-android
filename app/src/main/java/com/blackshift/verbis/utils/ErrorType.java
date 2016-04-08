package com.blackshift.verbis.utils;

/**
 * Created by Devika on 06-04-2016.
 */
public enum ErrorType {

    NETWORK_CONNECTION_FAILURE(0),
    WORD_NOT_FOUND_FAILURE(1);

    int errorType;

    ErrorType(int errorType){
        this.errorType = errorType;
    }

    public int getErrorType(){
        return errorType;
    }

}
