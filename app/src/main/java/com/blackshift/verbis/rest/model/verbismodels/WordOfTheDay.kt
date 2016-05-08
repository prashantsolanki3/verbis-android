package com.blackshift.verbis.rest.model.verbismodels

import io.realm.RealmObject
import io.realm.WordOfTheDayRealmProxy
import io.realm.annotations.PrimaryKey
import org.parceler.Parcel
import org.parceler.ParcelConstructor
import org.parceler.ParcelProperty

@Parcel(implementations = arrayOf(WordOfTheDayRealmProxy::class),
        value = Parcel.Serialization.BEAN,
        analyze = arrayOf(WordOfTheDay::class))
open class WordOfTheDay @ParcelConstructor constructor():RealmObject(){

    @ParcelProperty("word")
    lateinit var word: String
    @PrimaryKey
    @ParcelProperty("date")
    var date: Long = 0

    constructor(@ParcelProperty("word")word: String,@ParcelProperty("date")date: Long):this(){
        this.word = word
        this.date = date
    }
}
