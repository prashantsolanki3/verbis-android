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


    fun getWordOfTheDayRange(from: Long, to: Long){

        App.getVerbisService().getWordOfTheDay(from,to).
                enqueue(object :Callback<Response<List<WordOfTheDay>>> {

                    override fun onResponse(response: retrofit.Response<Response<List<WordOfTheDay>>>?, retrofit: Retrofit?) {
                        var realm = Realm.getDefaultInstance()
                        if(response?.body()?.statusCode==200){
                            realm.beginTransaction()
                            for(data in response?.body()?.data!!.iterator())
                                realm.copyToRealmOrUpdate(data)
                            realm.commitTransaction()
                            realm.isAutoRefresh = true
                        }
                    }

                    override fun onFailure(t: Throwable?) {
                        t?.printStackTrace()
                    }
                })

    }

    fun getWordsOfTheWeek(){

        var today = DateTime.now()
        var todayTimestamp = today.millis.div(1000)
        var sevenDayAgoTimestamp = today.minusDays(7).millis.div(1000)
        getWordOfTheDayRange(sevenDayAgoTimestamp,todayTimestamp)

    }

}