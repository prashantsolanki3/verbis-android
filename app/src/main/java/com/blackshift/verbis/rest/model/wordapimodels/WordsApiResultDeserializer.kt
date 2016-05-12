package com.blackshift.verbis.rest.model.wordapimodels

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.util.*

/**
 * Package com.blackshift.verbis.rest.model.wordapimodels
 *
 * Created by Prashant on 5/12/2016.
 *
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
open class WordsApiResultDeserializer :JsonDeserializer<WordsApiResult>{
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): WordsApiResult? {
        var result = WordsApiResult()
        var fullObj = json?.asJsonObject
        if(fullObj!=null){

            var word = fullObj.get("word").asString
            result.word = word

            var results: MutableList<Result> = arrayListOf()
            if(fullObj.has("results")) {
                var jsonResults = fullObj.getAsJsonArray("results")
                for (a in jsonResults)
                    results.add(context!!.deserialize(a, Result::class.java))
            }
            result.results = results as ArrayList<Result>

            if(fullObj.has("pronunciation")) {
                var isPronunciationObject = fullObj.get("pronunciation").isJsonObject
                var pronunciation: Pronunciation?
                if (isPronunciationObject) {
//                    pronunciation = Gson().fromJson(fullObj.getAsJsonObject("pronunciation"), Pronunciation::class.java)
                    pronunciation = context!!.deserialize(fullObj.get("pronunciation"),Pronunciation::class.java)
                } else {
                    pronunciation = Pronunciation()
                    pronunciation.all = fullObj.get("pronunciation").asString
                }
                result.pronunciation = pronunciation
            }

            if(fullObj.has("rhymes")){
                result.rhymes = context!!.deserialize(fullObj.get("rhymes"),Rhymes::class.java)
            }

            if(fullObj.has("syllables")){
                result.syllables = context!!.deserialize(fullObj.get("syllables"),Syllables::class.java)
            }


        }
        return result
    }
}