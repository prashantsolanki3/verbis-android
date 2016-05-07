package com.blackshift.verbis.rest.model.verbismodels

import io.realm.RealmObject

open class WordOfTheDay(): RealmObject() {

    lateinit var word: String
    lateinit var date: String

    constructor(word: String,date: String):this(){
        this.word = word
        this.date = date
    }
}
