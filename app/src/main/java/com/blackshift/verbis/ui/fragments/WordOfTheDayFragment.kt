package com.blackshift.verbis.ui.fragments


import android.os.Bundle
import android.support.annotation.IdRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.blackshift.verbis.R
import com.blackshift.verbis.rest.model.wordapimodels.WordsApiResult
import com.blackshift.verbis.utils.listeners.DictionaryListener
import com.blackshift.verbis.utils.manager.DictionaryManager


/**
 * A simple [Fragment] subclass.
 * Use the [WordOfTheDayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WordOfTheDayFragment : VerbisFragment() {

    private var mWord: String? = null

    var wordOfTheDayText: TextView? = null
    var date: TextView? = null
    var word: TextView? = null
    var meaning: TextView? = null
    var pronunciation: TextView? = null
    var speaker: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mWord = arguments.getString(ARG_PARAM1)
        }
    }

    lateinit var rootview:View

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootview = inflater!!.inflate(R.layout.fragment_word_of_the_day, container, false)
        //To initialize the views
        //ButterKnife.bind(this, view)
        init()

        populatingTextView()

        return view
    }

    fun init(){
        wordOfTheDayText = find(R.id.word_of_the_day_text)
        date = find(R.id.date)
        word = find(R.id.word)
        meaning= find(R.id.meaning)
        pronunciation= find(R.id.pronunciation)
        speaker = find(R.id.speaker)
    }

    fun <T: View> WordOfTheDayFragment.find(@IdRes id:Int):T{
         return this.rootview.findViewById(id) as T
    }

    private fun populatingTextView() {

        //TODO: Didi don't do this. :p Har jaga date format same nai hota. Use the method. isme kabi b error aa skta h.
        word?.text = mWord
        date?.text
        DictionaryManager(activity).searchWord(mWord!!, object : DictionaryListener{
            override fun onFound(wordsApiResult: WordsApiResult) {
                meaning?.text = wordsApiResult.results[0].definition
                pronunciation?.text = wordsApiResult.pronunciation.all
            }

            override fun onNotFound() {
            }

            override fun onFailure(throwable: Throwable?) {

            }
        })
    }

    companion object {

        private val ARG_PARAM1 = "word"

        fun newInstance(word: String): WordOfTheDayFragment {
            val fragment = WordOfTheDayFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, word)
            fragment.arguments = args
            return fragment
        }
    }


}// Required empty public constructor
