package com.blackshift.verbis.utils

import com.blackshift.verbis.rest.model.recyclerviewmodels.MeaningAndExample
import com.blackshift.verbis.rest.model.wordapimodels.Result
import java.util.*

/**
 * Package com.blackshift.verbis.utils
 *
 *
 * Created by Prashant on 3/16/2016.
 *
 *
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
class  Utils {
    companion object {

        @JvmStatic
        fun getAllPartOfSpeech(results: List<Result>): Set<String> {
            val partOfSpeech = HashSet<String>()

            for (result in results) {
                partOfSpeech.add(result.partOfSpeech)
            }

            return partOfSpeech
        }
        @JvmStatic
        fun getResultSortedByPartOfSpeech(results: List<Result>, partOfSpeech: Set<String>): List<List<MeaningAndExample>> {
            val meaningAndExampleLists = ArrayList<ArrayList<MeaningAndExample>>()
            for (string in partOfSpeech) {
                val meaningAndExamples = ArrayList<MeaningAndExample>()
                for (result in results) {
                    if (result.partOfSpeech == string) {
                        meaningAndExamples.add(MeaningAndExample(result.definition, result.examples))
                    }
                }
                meaningAndExampleLists.add(meaningAndExamples)
            }
            return meaningAndExampleLists
        }

        @JvmStatic
        fun getSynonyms(results: List<Result>):List<String>{
            var syn= ArrayList<String>()

            for(res in results){
                for (s in res.synonyms)
                    syn.add(s)
            }

            return syn
        }

        @JvmStatic
        fun getAntonyms(results: List<Result>):List<String>{
            var an= ArrayList<String>()

            for(res in results){
                for (s in res.antonyms)
                    an.add(s)
            }

            return an
        }
    }
}
