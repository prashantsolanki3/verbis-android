package com.blackshift.verbis.ui.fragments

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.blackshift.verbis.App
import com.blackshift.verbis.R
import com.blackshift.verbis.rest.model.wordlist.WordList
import com.blackshift.verbis.ui.activity.WordListViewPagerActivity
import com.blackshift.verbis.ui.viewholders.WordListTitleViewHolder
import com.blackshift.verbis.utils.listeners.WordListArrayListener
import com.blackshift.verbis.utils.manager.WordListManager
import com.bumptech.glide.Glide
import com.firebase.client.FirebaseError
import io.github.prashantsolanki3.snaplibrary.snap.adapter.SnapAdapter
import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapViewHolder
import io.github.prashantsolanki3.snaplibrary.snap.layout.wrapper.SnapSelectableLayoutWrapper
import io.github.prashantsolanki3.snaplibrary.snap.listeners.touch.SnapOnItemClickListener

/**
 * A placeholder fragment containing a simple view.
 */
class WordListTitlesRecyclerFragment : VerbisFragment() {

    lateinit internal var view: View
    lateinit internal var wordlistTitlesRecycler: RecyclerView
    lateinit  internal var wordListSnapAdapter: SnapAdapter<WordList>
    lateinit internal var context: Context

    @LayoutRes
    val progressBar:Int = R.layout.progressbar_circular_full

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater!!.inflate(R.layout.fragment_word_list, container, false)

        wordlistTitlesRecycler = view.findViewById(R.id.wordlist_title_recycler) as RecyclerView

        context = getContext()
        wordListManager = WordListManager(getContext())
        populateViews()

        return view
    }


    lateinit internal var wordListManager: WordListManager

    internal fun populateViews() {
        wordlistTitlesRecycler.setHasFixedSize(true)

        val snapLayoutWrapper = SnapSelectableLayoutWrapper(WordList::class.java, WordListTitleViewHolder::class.java, R.layout.wordlist_title_item, 1, true)

        wordListSnapAdapter = SnapAdapter<WordList>(context, snapLayoutWrapper, wordlistTitlesRecycler, view.findViewById(R.id.recyclerView_alternate) as ViewGroup)

        wordListSnapAdapter.showAlternateLayout(progressBar)

        wordListSnapAdapter.setOnItemClickListener(object :SnapOnItemClickListener{
            override fun onItemLongPress(p0: SnapViewHolder<*>?, p1: View?, p2: Int) {
            }

            override fun onItemClick(p0: SnapViewHolder<*>?, p1: View?, p2: Int) {

                var wl :WordList? = null
                if(p0!=null && p0.itemData!=null)
                    wl = p0.itemData as WordList

                startActivity(WordListViewPagerActivity.createIntent(context,wl!!.id))

            }
        })

        val layoutManager = GridLayoutManager(context, 2)
        wordlistTitlesRecycler.layoutManager = layoutManager
        wordlistTitlesRecycler.adapter = wordListSnapAdapter
        wordListManager.getAllWordLists(object : WordListArrayListener() {
            override fun onSuccess(wordList: List<WordList>?) {
                wordListSnapAdapter.set(wordList)
                if(wordList==null||wordList.isEmpty()){
                    wordListSnapAdapter.hideAlternateLayout()

                    var v:View? =  wordListSnapAdapter.getViewFromId(R.layout.layout_image)
                    if(v!=null) {
                        val img  = v.findViewById(R.id.imageView) as ImageView
                        Glide.with(App.getContext())
                                .load(R.drawable.nowordlist)
                                .into(img)
                        v.setOnClickListener {
                            startActivity(WordListViewPagerActivity.createIntent(context))
                        }
                        wordListSnapAdapter.showAlternateLayout(v)
                    }
                }else{
                    wordListSnapAdapter.hideAlternateLayout()
                }
            }

            override fun onFailure(firebaseError: FirebaseError) {
                var v:View? =  wordListSnapAdapter.getViewFromId(R.layout.layout_image)
                var image:Int?

                when(firebaseError.code){
                    FirebaseError.NETWORK_ERROR -> image = R.drawable.networkerror
                    else -> image = R.drawable.error
                }
                if(v!=null) {
                    Glide.with(App.getContext())
                            .load(image)
                            .into(v.findViewById(R.id.imageView) as ImageView)
                    wordListSnapAdapter.showAlternateLayout(v)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        callTracker(getString(R.string.title_fragment_word_list_titles))
    }

}
