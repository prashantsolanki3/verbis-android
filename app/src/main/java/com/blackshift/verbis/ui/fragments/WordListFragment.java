package com.blackshift.verbis.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.wordlist.Word;
import com.blackshift.verbis.rest.model.wordlist.WordList;
import com.blackshift.verbis.ui.viewholders.WordViewHolder;
import com.blackshift.verbis.utils.DateUtils;
import com.blackshift.verbis.utils.manager.WordListManager;
import com.blackshift.verbis.utils.listeners.WordArrayListener;
import com.blackshift.verbis.utils.listeners.WordListListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

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
    public class WordListFragment extends VerbisFragment{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_WORDLIST_ID = "wordlist_id";

        public WordListFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static WordListFragment newInstance(String wordlistId) {
            WordListFragment fragment = new WordListFragment();
            Bundle args = new Bundle();
            args.putString(ARG_WORDLIST_ID, wordlistId);
            fragment.setArguments(args);
            return fragment;
        }

        String wordlistId;
        WordListManager manager;

        @Bind(R.id.wordlist_toolbar)
        Toolbar wordlistToolbar;
        @Bind(R.id.list_words)
        RecyclerView recyclerView;
        WordList wordList=null;
        SnapSelectableAdapter<Word> adapter;
        Context context;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            context = getContext();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_word_list_view_pager, container, false);
            ButterKnife.bind(this,rootView);
            manager = new WordListManager(getContext());
            initToolbar();
            wordlistId = getArguments().getString(ARG_WORDLIST_ID);
            handleToolbarMode(true);
            SnapSelectableLayoutWrapper wrapper = new SnapSelectableLayoutWrapper(Word.class, WordViewHolder.class,R.layout.word_item,1,true);

            adapter = new SnapSelectableAdapter<>(getActivity(),
                    wrapper,
                    recyclerView,
                    AbstractSnapSelectableAdapter.SelectionType.MULTIPLE_ON_LONG_PRESS);

            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            adapter.setOnSelectionListener(new SelectionListener<Word>() {
                @Override
                public void onSelectionModeEnabled(AbstractSnapSelectableAdapter.SelectionType selectionType) {
                    handleToolbarMode(false);
                }

                @Override
                public void onSelectionModeDisabled(AbstractSnapSelectableAdapter.SelectionType selectionType) {
                    handleToolbarMode(true);
                }

                @Override
                public void onItemSelected(Word word, int i) {

                }

                @Override
                public void onItemDeselected(Word word, int i) {

                }

                @Override
                public void onSelectionLimitReached() {

                }

                @Override
                public void onSelectionLimitExceeding() {

                }

                @Override
                public void onNoneSelected() {

                }
            });

            manager.getWordsFromWordList(wordlistId, new WordArrayListener() {
                @Override
                public void onSuccess(@Nullable List<Word> word) {
                    if (word != null)
                        adapter.set(word);
                }

                @Override
                public void onFailure(FirebaseError firebaseError) {

                }
            });

            manager.getListFirebaseRef().child(wordlistId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    wordList = dataSnapshot.getValue(WordList.class);
                    if (wordList != null) {
                        wordlistToolbar.setTitle(wordList.getTitle());
                        MaterialIcons star;
                        if (wordList.isStarred())
                            star = MaterialIcons.md_star;
                        else
                            star = MaterialIcons.md_star_border;

                        wordlistToolbar.getMenu()
                                .findItem(R.id.action_star_word_list)
                                .setIcon(new IconDrawable(context, star)
                                        .colorRes(android.R.color.darker_gray)
                                        .actionBarSize());
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            return rootView;
        }

        void initToolbar(){
            wordlistToolbar.inflateMenu(R.menu.menu_word_list);
            wordlistToolbar.getMenu()
                    .findItem(R.id.action_share_word_list)
                    .setIcon(new IconDrawable(context, MaterialIcons.md_share)
                            .actionBarSize()
                            .colorRes(android.R.color.darker_gray));

            wordlistToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_delete_word_list:
                            manager.deleteWordList(wordlistId, new WordListListener() {
                                @Override
                                public void onSuccess(String firebaseReferenceString) {
                                    //Snackbar.make(mViewPager, "Wordlist deleted.", Snackbar.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(FirebaseError firebaseError) {
                                    //Snackbar.make(mViewPager, "Unable to delete Wordlist.", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                            return true;
                        case R.id.action_star_word_list:
                            if (wordList != null)
                                if (wordList.isStarred())
                                    manager.unstarWordlist(wordlistId, null);
                                else
                                    manager.starWordlist(wordlistId, null);
                            return true;
                        case R.id.action_share_word_list:
/*                            ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);*/
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey Check out My Wordlist: " +
                                    wordList.getTitle() + ", on Verbis.\n" + wordList.getId());
                            sendIntent.setType("text/plain");
 /*                           mShareActionProvider.setShareIntent(sendIntent);*/
                            startActivity(Intent.createChooser(sendIntent, "Share via"));
                            return true;
                        case R.id.action_delete_word:
                            List<Word> wordsToBeDeleted = adapter.getSelectedItems();
                            if(wordsToBeDeleted!=null&&wordsToBeDeleted.size()>0){
                                for(Word word:wordsToBeDeleted){
                                    manager.deleteWord(word,wordList,null);
                                }
                            }
                            adapter.setSelectionEnabled(false);
                            return true;
                    }
                    return false;
                }
            });

            wordlistToolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.addWord(DateUtils.getTimestampUTC() + "Timsestmp", DateUtils.getTimestampUTC() + "id", wordlistId, null);
                }
            });
        }

        void handleToolbarMode(boolean normal){

            Menu menu = wordlistToolbar.getMenu();

            menu.findItem(R.id.action_share_word_list).setVisible(normal);
            menu.findItem(R.id.action_star_word_list).setVisible(normal);
            menu.findItem(R.id.action_delete_word_list).setVisible(normal);

            menu.findItem(R.id.action_delete_word).setVisible(!normal);
        }

    /*public interface WordListSelectionListener extends SelectionListener<WordList> {
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
    }*/

    }