package com.blackshift.verbis.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blackshift.verbis.App;
import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.wordlist.Word;
import com.blackshift.verbis.rest.model.wordlist.WordList;
import com.blackshift.verbis.ui.activity.DictionaryActivity;
import com.blackshift.verbis.ui.viewholders.WordViewHolder;
import com.blackshift.verbis.utils.listeners.WordArrayListener;
import com.blackshift.verbis.utils.listeners.WordListListener;
import com.blackshift.verbis.utils.manager.WordListManager;
import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.prashantsolanki3.snaplibrary.snap.adapter.AbstractSnapSelectableAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.adapter.SnapSelectableAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapSelectableViewHolder;
import io.github.prashantsolanki3.snaplibrary.snap.layout.wrapper.SnapSelectableLayoutWrapper;
import io.github.prashantsolanki3.snaplibrary.snap.listeners.selection.SelectionListener;
import io.github.prashantsolanki3.snaplibrary.snap.listeners.touch.SnapSelectableOnItemClickListener;

import static com.blackshift.verbis.ui.fragments.WordListFragment.WordListFragmentState.CONNECTION_ERROR;
import static com.blackshift.verbis.ui.fragments.WordListFragment.WordListFragmentState.ERROR;
import static com.blackshift.verbis.ui.fragments.WordListFragment.WordListFragmentState.FOUND_WORDS;
import static com.blackshift.verbis.ui.fragments.WordListFragment.WordListFragmentState.LOADING;
import static com.blackshift.verbis.ui.fragments.WordListFragment.WordListFragmentState.NO_WORDS;

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
        @Bind(R.id.banner_ad_view)
        AdView adView;
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
            Log.d("size", rootView.getWidth() + " " + rootView.getHeight());
            manager = new WordListManager(getContext());
            initToolbar();
            addAdView();
            wordlistId = getArguments().getString(ARG_WORDLIST_ID);
            handleToolbarMode(true);
            SnapSelectableLayoutWrapper wrapper = new SnapSelectableLayoutWrapper(Word.class, WordViewHolder.class,R.layout.word_item,1,true);

            adapter = new SnapSelectableAdapter<>(getActivity(),
                    wrapper,
                    recyclerView,
                    (ViewGroup)rootView.findViewById(R.id.recyclerView_alternate),
                    AbstractSnapSelectableAdapter.SelectionType.MULTIPLE_ON_LONG_PRESS);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
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
            //To set Empty View in Adapter, Required for adapter working
            setState(NO_WORDS);
            adapter.setAutoEmptyLayoutHandling(false);
            setState(LOADING);

            adapter.setOnItemClickListener(new SnapSelectableOnItemClickListener(adapter) {
                @Override
                public void onItemClick(SnapSelectableViewHolder snapSelectableViewHolder, View view, int i) {
                    if (!adapter.isSelectionEnabled()&&adapter.getSelectedItems().size()<1) {
                        startActivity(DictionaryActivity.createIntent(WordListFragment.this.context, ((Word) snapSelectableViewHolder.getItemData()).getHeadword()));
                    }
                }

                @Override
                public void onItemLongPress(SnapSelectableViewHolder snapSelectableViewHolder, View view, int i) {

                }
            });

            manager.getWordsFromWordList(wordlistId, new WordArrayListener() {
                @Override
                public void onSuccess(@Nullable List<Word> word) {
                    if (word != null) {
                        adapter.set(word);
                        if (word.size() == 0)
                            setState(NO_WORDS);
                        else
                            setState(FOUND_WORDS);
                    }else
                        setState(NO_WORDS);
                }

                @Override
                public void onFailure(FirebaseError firebaseError) {
                    switch (firebaseError.getCode()){
                        case FirebaseError.NETWORK_ERROR:
                            setState(CONNECTION_ERROR);
                            break;
                        default:
                            setState(ERROR);
                    }
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
                   /* switch (firebaseError.getCode()){
                        case FirebaseError.NETWORK_ERROR:
                            setState(CONNECTION_ERROR);
                            break;
                        default:
                            setState(ERROR);
                    }*/
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
                                    Snackbar.make(recyclerView, "Unable to delete Wordlist.", Snackbar.LENGTH_SHORT).show();
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
                            //TODO: Share link
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey Check out My Wordlist: " +
                                    wordList.getTitle() + ", on Verbis.");

                            sendIntent.setType("text/plain");
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
        }


    void setState(@WordListFragmentState int state){

        View v = adapter.getViewFromId(R.layout.layout_image);
        ImageView img =(ImageView) v.findViewById(R.id.imageView);
        @DrawableRes
        int placeholder = R.drawable.nowordlist; // TODO: Change this to Search Prompt

        switch (state){
            case LOADING:
                // Loading View
                break;
            case CONNECTION_ERROR:
                placeholder = R.drawable.networkerror;
                break;
            case NO_WORDS:
                adapter.setEmptyView(v);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(DictionaryActivity.createIntent(getActivity(),""));
                    }
                });
                placeholder = R.drawable.noword;
                break;
            case FOUND_WORDS:
                adapter.hideAlternateLayout();
                break;
            case ERROR:
                placeholder = R.drawable.error;
                break;
        }



        if(state!=FOUND_WORDS&&state!=LOADING){
            Glide.with(App.getContext())
                    .load(placeholder)
                    .into(img);
            adapter.showAlternateLayout(v);
        }
    }

    @IntDef({CONNECTION_ERROR,
            FOUND_WORDS,
            LOADING,
            NO_WORDS,
            ERROR,})
    @Retention(RetentionPolicy.SOURCE)
    @interface WordListFragmentState {
        int LOADING = 0,CONNECTION_ERROR = 2, NO_WORDS = 3, FOUND_WORDS = 4,ERROR = 5;
    }

        void handleToolbarMode(boolean normal) {

            Menu menu = wordlistToolbar.getMenu();

            //TODO: first setup public wordlists
            menu.findItem(R.id.action_share_word_list).setVisible(false);
            menu.findItem(R.id.action_star_word_list).setVisible(normal);
            menu.findItem(R.id.action_delete_word_list).setVisible(normal);

            menu.findItem(R.id.action_delete_word).setVisible(!normal);
        }


    private void addAdView() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        if (adView != null) {
            //adView.setAdSize(new AdSize(280, 60));
            //adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
            adView.loadAd(adRequest);
        }
    }


}