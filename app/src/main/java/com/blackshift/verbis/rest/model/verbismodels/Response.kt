package com.blackshift.verbis.rest.model.verbismodels

/**
 * Created by Prashant on 5/5/2016.
 */
data class Response<T>(val status:String,val statusCode: Int) {

    /*Secondary Constructor*/
    constructor(status:String,statusCode: Int,data:T) : this(status,statusCode){
        this.data = data
    }

    constructor(data:T) : this("okay",200){
        this.data = data
    }

    var data:T? = null

}