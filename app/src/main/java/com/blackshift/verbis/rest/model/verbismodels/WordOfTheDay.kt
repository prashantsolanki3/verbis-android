package com.blackshift.verbis.rest.model.verbismodels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class WordOfTheDay():RealmObject(){
    lateinit var word: String
    @PrimaryKey
    var date: Long = 0

    constructor(word: String,date: Long):this(){
        this.word = word
        this.date = date
    }


    /*var day: Int = 0
    var month:Int = 0
    var year: Int = 0*/

/*    constructor(word: String,day: Int, month:Int,year:Int):this(){
        this.word = word
        this.day = day
        this.month = month
        this.year = year
    }*/
}
