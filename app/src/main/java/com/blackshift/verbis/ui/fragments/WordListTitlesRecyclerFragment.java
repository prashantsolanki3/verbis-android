package com.blackshift.verbis.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.wordlist.WordList;
import com.blackshift.verbis.ui.viewholders.WordListTitleViewHolder;
import com.blackshift.verbis.utils.manager.WordListManager;
import com.blackshift.verbis.utils.listeners.WordListArrayListener;
import com.firebase.client.FirebaseError;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.prashantsolanki3.snaplibrary.snap.adapter.AbstractSnapSelectableAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.adapter.SnapSelectableAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.layout.wrapper.SnapSelectableLayoutWrapper;
import io.github.prashantsolanki3.snaplibrary.snap.listeners.selection.SelectionListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class WordListTitlesRecyclerFragment extends Fragment {

    public WordListTitlesRecyclerFragment() {}

    View view;

    @Bind(R.id.wordlist_title_recycler)
    RecyclerView wordlistTitlesRecycler;
    SnapSelectableAdapter<WordList> wordListSnapAdapter;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_word_list, container, false);
        context = getContext();
        ButterKnife.bind(this, view);
        wordListManager = new WordListManager(getContext());
        populateViews();

        return view;
    }


    WordListManager wordListManager;

    void populateViews(){
        wordlistTitlesRecycler.setHasFixedSize(true);
        final SnapSelectableLayoutWrapper snapLayoutWrapper = new SnapSelectableLayoutWrapper(WordList.class, WordListTitleViewHolder.class,R.layout.wordlist_title_item,1,true);
        wordListSnapAdapter = new SnapSelectableAdapter<>(context,snapLayoutWrapper, wordlistTitlesRecycler, AbstractSnapSelectableAdapter.SelectionType.MULTIPLE_ON_LONG_PRESS);
        wordListSnapAdapter.setOnSelectionListener(mCallback);
        mCallback.setSnapAdapter(wordListSnapAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(context,2);
        wordlistTitlesRecycler.setLayoutManager(layoutManager);
        wordlistTitlesRecycler.setAdapter(wordListSnapAdapter);
        wordListManager.getAllWordLists(new WordListArrayListener() {
            @Override
            public void onSuccess(@Nullable List<WordList> wordList) {
                wordListSnapAdapter.set(wordList);
            }

            @Override
            public void onFailure(FirebaseError firebaseError) {

            }
        });
    }
    public interface WordListSelectionListener extends SelectionListener<WordList> {
        void setSnapAdapter(SnapSelectableAdapter<WordList> adapter);
    }

    WordListSelectionListener mCallback;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (WordListSelectionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
}
