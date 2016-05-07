package com.blackshift.verbis.rest.service

import com.blackshift.verbis.rest.model.verbismodels.Response
import com.blackshift.verbis.rest.model.verbismodels.WordOfTheDay
import retrofit2.Call
import retrofit2.http.GET

/**
 * Package com.blackshift.verbis.rest.service
 *
 *
 * Created by Prashant on 5/7/2016.
 *
 *
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
internal interface VerbisService{

    @GET("word-of-the-day/")
    fun getWordOfTheDay():Call<Response<List<WordOfTheDay>>>

}
