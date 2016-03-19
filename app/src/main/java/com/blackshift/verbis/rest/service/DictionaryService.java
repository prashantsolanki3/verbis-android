package com.blackshift.verbis.rest.service;

import com.blackshift.verbis.rest.model.DictionaryResults;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;

/**
 * Package com.blackshift.verbis.rest.service
 * <p>
 * Created by Prashant on 3/16/2016.
 * <p>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
public interface DictionaryService {

    @GET("ldoce5/entries")
    Call<DictionaryResults> getWordDetail(@Header("headword") String string);

}
