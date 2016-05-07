package com.blackshift.verbis.rest.service;

import com.blackshift.verbis.rest.model.wordapimodels.WordsApiResult;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

/**
 * Package com.blackshift.verbis.rest.service
 * <p>
 * Created by Prashant on 3/16/2016.
 * <p>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
public interface DictionaryService {
    /*
    @GET("ldoce5/entries")
    Call<PearsonResults> getWordDetail(@Header("headword") String string);
    */

    @GET("{word}")
    Call<WordsApiResult> getWordDetail(@Header("X-Mashape-Key")String key, @Path("word") String string);

    @GET("{word}/synonyms")
    Call<WordsApiResult> getWordSynonyms(@Header("X-Mashape-Key")String key, @Path("word") String string);

}
