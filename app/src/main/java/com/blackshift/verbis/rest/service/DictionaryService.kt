package com.blackshift.verbis.rest.service

import com.blackshift.verbis.rest.model.wordapimodels.WordsApiResult

import retrofit.Call
import retrofit.http.GET
import retrofit.http.Header
import retrofit.http.Path


/**
 * Package com.blackshift.verbis.rest.service
 *
 *
 * Created by Prashant on 3/16/2016.
 *
 *
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
interface DictionaryService {
    /*
    @GET("ldoce5/entries")
    Call<PearsonResults> getWordDetail(@Header("headword") String string);
    */

    @GET("{word}")
    fun getWordDetail(@Header("X-Mashape-Key") key: String, @Path("word") string: String): Call<WordsApiResult>

    @GET("{word}/synonyms")
    fun getWordSynonyms(@Header("X-Mashape-Key") key: String, @Path("word") string: String): Call<WordsApiResult>

}
