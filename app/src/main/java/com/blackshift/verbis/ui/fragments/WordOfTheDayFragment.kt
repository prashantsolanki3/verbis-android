package com.blackshift.verbis.ui.fragments


import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.NonNull
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blackshift.verbis.R
import com.blackshift.verbis.rest.model.verbismodels.WordOfTheDay
import com.blackshift.verbis.rest.model.wordapimodels.WordsApiResult
import com.blackshift.verbis.ui.activity.DictionaryActivity
import com.blackshift.verbis.ui.widgets.FontTextView
import com.blackshift.verbis.utils.listeners.DictionaryListener
import com.blackshift.verbis.utils.manager.DictionaryManager
import io.realm.Realm
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter


/**
 * A simple [Fragment] subclass.
 * Use the [WordOfTheDayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WordOfTheDayFragment : VerbisFragment() {

    private var mWord: WordOfTheDay? = null

    private var dateStamp:Long = -1
    lateinit var rootview:View
    lateinit var word: FontTextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            dateStamp = arguments.getLong(ARG_PARAM1,-1)
            if(!dateStamp.equals(-1))
            mWord = Realm.getDefaultInstance().where(WordOfTheDay::class.java).equalTo("date",dateStamp).findFirst()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootview = inflater!!.inflate(R.layout.fragment_word_of_the_day, container, false)
        //To initialize the views
        //ButterKnife.bind(this, view)
        init()

        rootview.setOnClickListener {
            startActivity(DictionaryActivity.createIntent(activity,mWord!!.word))
        }

        populatingTextView()

        return rootview
    }

    lateinit var meaning:FontTextView
    lateinit var partOfSpeech:FontTextView
    lateinit var date:FontTextView

    fun init(){
        word = find(R.id.wotd_word)
        meaning = find(R.id.wotd_meaning)
        partOfSpeech = find(R.id.wotd_part_of_speech)
        date = find(R.id.wotd_date)
    }

    fun <T: View> WordOfTheDayFragment.find(@IdRes id:Int):T{
         return this.rootview.findViewById(id) as T
    }

    private fun populatingTextView() {
        if(mWord!=null){
        word.text = mWord?.word
            var dtf:DateTimeFormatter = DateTimeFormat.forPattern("MMM dd YYYY")

        date.text = DateTime(mWord?.date?.times(1000)).toString(dtf)

       DictionaryManager(activity).searchWord(mWord!!.word, object : DictionaryListener{
            override fun onFound(wordsApiResult: WordsApiResult) {
                meaning.text = wordsApiResult.results[0].definition
                partOfSpeech.text = wordsApiResult.results[0].partOfSpeech
             }

            override fun onNotFound() {
            }

            override fun onFailure(throwable: Throwable?) {

            }
        })
        }
    }

    companion object {

        private val ARG_PARAM1 = "word"

        fun newInstance(@NonNull word: WordOfTheDay): WordOfTheDayFragment {
            val fragment = WordOfTheDayFragment()
            val args = Bundle()
            args.putLong(ARG_PARAM1, word.date)
            fragment.arguments = args
            return fragment
        }
    }


}// Required empty public constructor
