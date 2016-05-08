package com.blackshift.verbis.utils.manager

import android.content.Context
import com.blackshift.verbis.App
import com.blackshift.verbis.R
import com.blackshift.verbis.rest.model.wordapimodels.WordsApiResult
import com.blackshift.verbis.utils.listeners.DictionaryListener
import retrofit.Callback
import retrofit.Response
import retrofit.Retrofit

/**
 * Package com.blackshift.verbis.utils.manager
 *
 * Created by Prashant on 5/8/2016.
 *
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
class DictionaryManager(val content:Context) {



    fun searchWord(query:String, listener: DictionaryListener){

        App.getDictionaryService().getWordDetail(content.getString(R.string.words_api_key), query).enqueue(object :Callback<WordsApiResult>{

            override fun onResponse(response: Response<WordsApiResult>?, retrofit: Retrofit?) {
                if (response!=null&&response.body() != null) {
                    //Found
                    listener.onFound(response.body())
                } else {
                    listener.onNotFound()
                    // Not Found
                }

            }

            override fun onFailure(t: Throwable?) {
                listener.onFailure(t)
            }
        })
    }

}