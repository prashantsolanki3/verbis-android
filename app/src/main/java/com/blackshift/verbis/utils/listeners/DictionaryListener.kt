package com.blackshift.verbis.utils.listeners

import com.blackshift.verbis.rest.model.wordapimodels.WordsApiResult

/**
 * Package com.blackshift.verbis.utils.listeners
 *
 * Created by Prashant on 5/8/2016.
 *
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
interface DictionaryListener {

    /**
     * Called if the execution was successful.

     */
    fun onFound(wordsApiResult: WordsApiResult)

    fun onNotFound()

    /**
     * Called if the execution was unsuccessful.

     * @param throwable object containing the information about the error.
     * *
     */
    fun onFailure(throwable: Throwable?)

}