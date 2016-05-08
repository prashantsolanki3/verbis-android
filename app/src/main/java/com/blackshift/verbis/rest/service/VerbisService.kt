package com.blackshift.verbis.rest.service

import com.blackshift.verbis.rest.model.verbismodels.Response
import com.blackshift.verbis.rest.model.verbismodels.WordOfTheDay
import retrofit.Call
import retrofit.http.GET
import retrofit.http.Query

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
    fun getWordOfTheDay(@Query("from") from: Long,@Query("to") to:Long):Call<Response<List<WordOfTheDay>>>

}
