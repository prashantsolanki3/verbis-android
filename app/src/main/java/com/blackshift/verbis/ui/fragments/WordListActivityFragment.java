package com.blackshift.verbis.ui.fragments;

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
import com.blackshift.verbis.utils.listeners.WordListListener;
import com.firebase.client.FirebaseError;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.prashantsolanki3.snaplibrary.snap.adapter.SnapAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapViewHolder;
import io.github.prashantsolanki3.snaplibrary.snap.layout.wrapper.SnapLayoutWrapper;
import io.github.prashantsolanki3.snaplibrary.snap.listeners.touch.SnapOnItemClickListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class WordListActivityFragment extends Fragment {

    public WordListActivityFragment() {
    }

    View view;

    @Bind(R.id.wordlist_title_recycler)
    RecyclerView wordlistTitlesRecycler;
    SnapAdapter<WordList> wordListSnapAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_word_list, container, false);
        ButterKnife.bind(this, view);
        populateViews();
        wordListManager = new WordListManager(getContext());
        return view;
    }

    WordListManager wordListManager;

    void populateViews(){
        wordlistTitlesRecycler.setHasFixedSize(true);
        final SnapLayoutWrapper snapLayoutWrapper = new SnapLayoutWrapper(WordList.class, WordListTitleViewHolder.class,R.layout.wordlist_title_item,1);
        wordListSnapAdapter = new SnapAdapter<>(getContext(),snapLayoutWrapper, wordlistTitlesRecycler, (ViewGroup) view.findViewById(R.id.recyclerView_alternate));

        wordListSnapAdapter.setOnItemClickListener(new SnapOnItemClickListener() {
            @Override
            public void onItemClick(SnapViewHolder snapViewHolder, View view, int i) {
                wordListManager.renameWordList("Renamed " + i, (WordList) snapViewHolder.getItemData(), new WordListListener() {

                        @Override
                    public void onSuccess(String firebaseReferenceString) {

                    }

                    @Override
                    public void onFailure(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onItemLongPress(SnapViewHolder snapViewHolder, View view, int i) {
                wordListManager.deleteWordList((WordList) snapViewHolder.getItemData(), new WordListListener() {
                    @Override
                    public void onSuccess(String firebaseReferenceString) {

                    }

                    @Override
                    public void onFailure(FirebaseError firebaseError) {

                    }
                });
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        wordlistTitlesRecycler.setLayoutManager(layoutManager);
        wordlistTitlesRecycler.setAdapter(wordListSnapAdapter);
        WordListManager wordListManager = new WordListManager(getContext());
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
}
