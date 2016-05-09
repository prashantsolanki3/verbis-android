package com.blackshift.verbis.utils.manager

import android.content.Context
import com.blackshift.verbis.App
import com.blackshift.verbis.rest.model.verbismodels.Response
import com.blackshift.verbis.rest.model.verbismodels.WordOfTheDay
import io.realm.Realm
import org.joda.time.DateTime
import retrofit.Callback
import retrofit.Retrofit

/**
 * Package com.blackshift.verbis.utils.manager
 *
 * Created by Prashant on 5/8/2016.
 *
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
open class WordOfTheDayManager(val content: Context){

    fun getWordOfTheDay(){

        var today = DateTime.now()

        App.getVerbisService().getWordOfTheDay(today.minusDays(7).withTimeAtStartOfDay().millis.div(1000),today.withTimeAtStartOfDay().millis.div(1000)).enqueue(object :Callback<Response<List<WordOfTheDay>>> {

            override fun onResponse(response: retrofit.Response<Response<List<WordOfTheDay>>>?, retrofit: Retrofit?) {
                var realm = Realm.getDefaultInstance()
                if(response?.body()?.statusCode==200){
                    realm.beginTransaction()
                    for(data in response?.body()?.data!!.iterator())
                    realm.copyToRealmOrUpdate(data)
                    realm.commitTransaction()
                }
            }

            override fun onFailure(t: Throwable?) {
                t?.printStackTrace()
            }
        })
    }

}