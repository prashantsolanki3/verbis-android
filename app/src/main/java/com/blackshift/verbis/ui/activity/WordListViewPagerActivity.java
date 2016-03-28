package com.blackshift.verbis.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import com.blackshift.verbis.utils.listeners.WordArrayListener;
import com.blackshift.verbis.utils.listeners.WordListArrayListener;
import com.blackshift.verbis.utils.manager.WordListManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
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
    static int count = 0;
    @Bind(R.id.viewpager_indicator)
    CirclePageIndicator pageIndicator;

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

        wordListManager.getAllWordLists(new WordListArrayListener() {
            @Override
            public void onSuccess(@Nullable List<WordList> wordList) {
                count++;
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
        //TODO: FAB TO CREATE NEW LIST

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
