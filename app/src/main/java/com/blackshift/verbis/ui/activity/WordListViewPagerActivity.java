package com.blackshift.verbis.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;

import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.wordlist.Word;
import com.blackshift.verbis.rest.model.wordlist.WordList;
import com.blackshift.verbis.ui.viewholders.WordViewHolder;
import com.blackshift.verbis.utils.DateUtils;
import com.blackshift.verbis.utils.WordListManager;
import com.blackshift.verbis.utils.listeners.WordArrayListener;
import com.blackshift.verbis.utils.listeners.WordListArrayListener;
import com.blackshift.verbis.utils.listeners.WordListListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;
import com.viewpagerindicator.CirclePageIndicator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import io.github.prashantsolanki3.snaplibrary.snap.adapter.SnapAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.layout.wrapper.SnapLayoutWrapper;

public class WordListViewPagerActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private WordListViewPagerAdapter mWordListViewPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    @Bind(R.id.container)
    ViewPager mViewPager;
    private WordListManager wordListManager;
    @Bind(R.id.viewpager_indicator)
    CirclePageIndicator pageIndicator;

    @Bind(R.id.add_list_layout)
    View overlayToolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.new_wordlist_title)
    EditText newWordlistTitle;

    SupportAnimator overLayoutToolbarAnimator,reverseOverlayToolbarAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list_view_pager);
        wordListManager = new WordListManager(this);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mWordListViewPagerAdapter = new WordListViewPagerAdapter(getSupportFragmentManager(),this);
        overlayToolbar.setVisibility(View.INVISIBLE);
        ((CollapsingToolbarLayout)findViewById(R.id.toolbar_layout)).setTitleEnabled(false);

        wordListManager.getAllWordLists(new WordListArrayListener() {
            @Override
            public void onSuccess(@Nullable List<WordList> wordList) {
                mWordListViewPagerAdapter.set(wordList);
            }

            @Override
            public void onFailure(FirebaseError firebaseError) {

            }
        });

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mWordListViewPagerAdapter);
        mViewPager.setClipToPadding(false);
        pageIndicator.setViewPager(mViewPager);
        mViewPager.setPageMargin(56);

        newWordlistTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    handleFabStatus(FabStatus.ADD);
                    newWordlistTitle.setText(null);
                }
            }
        });

        newWordlistTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO: Better validation so that user can't enter special chars.
                if(newWordlistTitle.isEnabled())
                if(s.toString().isEmpty())
                    handleFabStatus(FabStatus.CANCEL);
                else
                    handleFabStatus(FabStatus.ACCEPT);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_word_list_view_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    void fabclick(){
        WordListManager wordListManager = new WordListManager(this);
        switch ((String)fab.getTag()){
            case FabStatus.ADD:
                //Because Next state should be Cancel
                handleFabStatus(FabStatus.CANCEL);

                //Show Edit Text
                newWordlistTitle.requestFocus();
                showAddWordlistLayout(true);
                break;
            case FabStatus.ACCEPT:
                fab.setEnabled(false);
                newWordlistTitle.setEnabled(false);
                wordListManager.createWordList(newWordlistTitle.getText().toString(), new WordListListener() {
                    @Override
                    public void onSuccess(String firebaseReferenceString) {
                        //Because Next state should be ADD
                        handleFabStatus(FabStatus.ADD);
                        showAddWordlistLayout(false);
                        fab.setEnabled(true);
                    }

                    @Override
                    public void onFailure(FirebaseError firebaseError) {
                        fab.setEnabled(true);
                    }
                });
                break;
            //Currently not in use.
            case FabStatus.CANCEL:
                //Because Next state should be ADD
                handleFabStatus(FabStatus.ADD);
                newWordlistTitle.setText(null);
                showAddWordlistLayout(false);
                break;
            default:
                handleFabStatus(FabStatus.ADD);
        }
    }

    void handleFabStatus(@FabStatus String status){
        MaterialIcons icons = MaterialIcons.md_add;
        switch (status){
            case FabStatus.ADD:
                newWordlistTitle.setEnabled(false);
                break;
            case FabStatus.ACCEPT:
                newWordlistTitle.setEnabled(true);
                icons = MaterialIcons.md_check;
                break;
            //Currently not in use.
            case FabStatus.CANCEL:
                newWordlistTitle.setEnabled(true);
                icons = MaterialIcons.md_clear;
                break;
        }

        fab.setImageDrawable(new IconDrawable(this,icons).colorRes(android.R.color.white));
        fab.setTag(status);
    }

    void showAddWordlistLayout(boolean visibility){
        View fab = findViewById(R.id.fab);
        // get the center for the clipping circle
        int cx = (fab.getLeft() + fab.getRight()) / 2;
        int cy = (fab.getTop() + fab.getBottom()) / 2;

        // get the final radius for the clipping circle
        int dx = Math.max(cx, overlayToolbar.getWidth() - cx);
        int dy = Math.max(cy, overlayToolbar.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        overLayoutToolbarAnimator =
                ViewAnimationUtils.createCircularReveal(overlayToolbar, cx, cy, 0, finalRadius);
        overlayToolbar.setVisibility(View.INVISIBLE);
        overLayoutToolbarAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        overLayoutToolbarAnimator.setDuration(500);
        reverseOverlayToolbarAnimator = overLayoutToolbarAnimator.reverse();

        if(visibility) {
            overlayToolbar.setVisibility(View.VISIBLE);
            overLayoutToolbarAnimator.start();
        }else {
            //TODO: Reverse Circular Reveal
            reverseOverlayToolbarAnimator.start();
            overlayToolbar.setVisibility(View.INVISIBLE);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({FabStatus.ADD,FabStatus.ACCEPT,FabStatus.CANCEL})
    @interface FabStatus{
        String ADD="add",ACCEPT = "accept",CANCEL = "cancel";
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_WORDLIST_ID = "wordlist_id";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String wordlistId) {
            PlaceholderFragment fragment = new PlaceholderFragment();
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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_word_list_view_pager, container, false);
            ButterKnife.bind(this,rootView);
            manager = new WordListManager(getContext());
            wordlistId = getArguments().getString(ARG_WORDLIST_ID);
            SnapLayoutWrapper wrapper = new SnapLayoutWrapper(Word.class, WordViewHolder.class,R.layout.word_item,1);
            final SnapAdapter<Word> adapter = new SnapAdapter<>(getActivity(),wrapper,recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            //TODO: Create Menu for Deleting, starring, etc.

            wordlistToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return false;
                }
            });

            wordlistToolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.addWord(DateUtils.getTimestampUTC()+"Timsestmp",DateUtils.getTimestampUTC()+"id", wordlistId,null);
                }
            });

            manager.getWordsFromWordList(wordlistId, new WordArrayListener() {
                @Override
                public void onSuccess(@Nullable List<Word> word) {
                    if(word!=null)
                        adapter.set(word);
                }

                @Override
                public void onFailure(FirebaseError firebaseError) {

                }
            });

            manager.getListFirebaseRef().child(wordlistId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    WordList wordList = dataSnapshot.getValue(WordList.class);
                    wordlistToolbar.setTitle(wordList.getTitle());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class WordListViewPagerAdapter extends FragmentStatePagerAdapter {

        List<WordList> wordLists;
        Context context;

        public WordListViewPagerAdapter(FragmentManager fm, Context context) {
            this(fm,new ArrayList<WordList>(),context);
        }

        public WordListViewPagerAdapter(FragmentManager fm, List<WordList> wordLists, Context context) {
            super(fm);
            this.wordLists = wordLists;
            this.context = context;
        }

        public void set(List<WordList> wordLists){
            this.wordLists.clear();
            this.wordLists = wordLists;
            this.notifyDataSetChanged();
        }

        public void add(WordList wordList){
            this.wordLists.add(wordList);
            this.notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(wordLists.get(position).getId());
        }

        @Override
        public int getCount() {
            if(wordLists!=null)
                return wordLists.size();

            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(getCount()>position&&wordLists!=null)
                return wordLists.get(position).getTitle();

            return "";
        }


        @Override
        public int getItemPosition(Object object) {
            //To update fragment every time user navigates to it.
            return POSITION_NONE;
        }

        @Override
        public float getPageWidth(int position) {
            return 0.90f;
        }
    }
}
