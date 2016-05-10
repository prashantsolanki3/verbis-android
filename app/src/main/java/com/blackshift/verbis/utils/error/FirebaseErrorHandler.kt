package com.blackshift.verbis.utils.error

import com.firebase.client.FirebaseError

/**
 * Created by Sarneet Singh on 4/20/2016.
 */
class FirebaseErrorHandler (val firebaseError: FirebaseError){

    fun checkErrorCode(): String {
        var message:String
        when (firebaseError.code) {
            FirebaseError.INVALID_CREDENTIALS -> message = "Invalid credentials, Contact your account provider for more details"
            FirebaseError.NETWORK_ERROR -> message = "Network Error. Please connect to the Internet"
            FirebaseError.LIMITS_EXCEEDED -> message = "Max Limits attained. Kindly try again after some time"
            FirebaseError.PERMISSION_DENIED -> message = "Permission Denied"
            FirebaseError.PROVIDER_ERROR -> message = "Some error occured. Contact your account provider"
            FirebaseError.UNKNOWN_ERROR -> message = "Unknown error occured"
            FirebaseError.EXPIRED_TOKEN -> message = "Expired Token. Kindly login again"
            else -> message = "Some error occured. Please try again after sometime"
        }
        return message
    }
}
