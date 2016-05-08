package com.blackshift.verbis.rest.service;

import com.blackshift.verbis.rest.model.wordapimodels.WordsApiResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;


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
