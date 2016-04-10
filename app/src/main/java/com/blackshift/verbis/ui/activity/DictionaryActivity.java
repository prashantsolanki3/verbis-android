package com.blackshift.verbis.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackshift.verbis.App;
import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.RecentWord;
import com.blackshift.verbis.rest.model.recyclerviewmodels.MeaningAndExample;
import com.blackshift.verbis.rest.model.wordapimodels.WordsApiResult;
import com.blackshift.verbis.rest.providers.RecentSuggestionsProvider;
import com.blackshift.verbis.ui.fragments.WordListOptionBottomSheet;
import com.blackshift.verbis.ui.viewholders.MeaningExampleViewHolder;
import com.blackshift.verbis.ui.viewholders.PartOfSpeechViewHolder;
import com.blackshift.verbis.utils.Utils;
import com.blackshift.verbis.utils.listeners.RecentWordListListener;
import com.blackshift.verbis.utils.listeners.RecentWordListener;
import com.blackshift.verbis.utils.manager.RecentWordsManager;
import com.firebase.client.FirebaseError;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.github.prashantsolanki3.snaplibrary.snap.adapter.SnapAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.layout.wrapper.SnapLayoutWrapper;
import retrofit.BaseUrl;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DictionaryActivity extends VerbisActivity {

    public static final String WORD_TRANSFER_TEXT = "word_transfer_text";

    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;
    String query;
    CollapsingToolbarLayout collapsingToolbarLayout;
    RecyclerView recyclerView;
    FloatingActionButton shareFAB;
    FloatingActionButton addToListFAB;
    FloatingActionButton pronounceFAB;
    SnapAdapter snapMultiAdapter;
    SearchView searchView;
    List<RecentWord> recentWords = new ArrayList<>();
    RecentWordsManager recentWordsManager;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        initView();
        manageToolbar();
        recentWordsManager = new RecentWordsManager(App.getContext());
        getAllRecentWords();
        handleIntent(getIntent());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void manageToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle("Loading...");
    }

    private void initView() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        recyclerView = (RecyclerView) findViewById(R.id.dictionary_recycler_view);
        shareFAB = (FloatingActionButton) findViewById(R.id.fab_share);
        addToListFAB = (FloatingActionButton) findViewById(R.id.fab_add_to_list);
        pronounceFAB = (FloatingActionButton) findViewById(R.id.fab_pronounce);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        query = getIntent().getStringExtra(HomePageActivity.DATA_TRANSFER_TEXT);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            if (query == null) {
                query = intent.getStringExtra(SearchManager.QUERY);
            }
            Snackbar.make(coordinatorLayout, query, Snackbar.LENGTH_LONG)
                    .show();
            SearchRecentSuggestions suggestions =
                    new SearchRecentSuggestions(this, RecentSuggestionsProvider.AUTHORITY, RecentSuggestionsProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            // TODO : Add in settings the facility to clear history
            // TODO : Add accent preference

            App.getDictionaryService().getWordDetail(getString(R.string.words_api_key), query)
                    .enqueue(new Callback<WordsApiResult>() {

                        @Override
                        public void onResponse(Response<WordsApiResult> response, Retrofit retrofit) {

                            if (response.body() != null) {

                                Log.d(DictionaryActivity.class.getName(), response.body() + "  " + response.isSuccess() + " " + response.message());

                                shareFAB.setVisibility(View.VISIBLE);
                                // TODO : Add pronunciation
                                pronounceFAB.setVisibility(View.VISIBLE);
                                manageAddWordFab(retrofit.baseUrl(), query);
                                collapsingToolbarLayout.setTitle(response.body().getWord() + "(" +
                                        response.body().getPronunciation().getAll() + ")");
                                setupRecyclerView(response.body());
                            }

                        }
                        @Override
                        public void onFailure(Throwable t) {
                            t.printStackTrace();
                        }
                    });
        }
    }

    private void setupRecyclerView(WordsApiResult wordsApiResult) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<SnapLayoutWrapper> layoutWrappers = new ArrayList<>();
        layoutWrappers.add(new SnapLayoutWrapper(String.class, PartOfSpeechViewHolder.class, R.layout.part_of_speech_item, 1));
        layoutWrappers.add(new SnapLayoutWrapper(MeaningAndExample.class, MeaningExampleViewHolder.class, R.layout.meaning_example_item, 2));

        snapMultiAdapter = new SnapAdapter(this, layoutWrappers, recyclerView, (ViewGroup)findViewById(R.id.recyclerView_alternate));

        Set<String> partOfSpeech = Utils.getAllPartOfSpeech(wordsApiResult.getResults());
        ArrayList<ArrayList<MeaningAndExample>> meaningAndExampleList =
                Utils.getResultSortedByPartOfSpeech(wordsApiResult.getResults(), partOfSpeech);

        Log.d("size", meaningAndExampleList.size() + "");
        for (ArrayList<MeaningAndExample> meaningAndExamples : meaningAndExampleList){
            for (MeaningAndExample meaningAndExample : meaningAndExamples){
                Log.d("Meaning", meaningAndExample.getMeaning());
                for (String s : meaningAndExample.getExample()){
                    Log.d("Example", s);
                }
            }
        }

        int i = 0;
        for (String string : partOfSpeech){
            snapMultiAdapter.add(string);
            for (MeaningAndExample meaningAndExample : meaningAndExampleList.get(i)){
                Log.d("meaning", meaningAndExample.getMeaning());
                for (String st: meaningAndExample.getExample()){
                    Log.d("example", st);
                }
                snapMultiAdapter.add(meaningAndExample);
            }
            i++;
        }
        recyclerView.setAdapter(snapMultiAdapter);

    }

    private void saveRecentWord(String query) {

        if (!recentWords.contains(query)) {

            recentWordsManager.addRecentWord(query, new RecentWordListener() {
                @Override
                public void onSuccess(RecentWord word) {
                    Log.d("Firebase_Recent_Words", "added");
                }

                @Override
                public void onFailure(FirebaseError firebaseError) {
                    Log.d("Firebase_Recent_Words", "not added \n" + firebaseError.getMessage());
                }
            });
        }

    }

    private void manageAddWordFab(final BaseUrl baseUrl, final String word){

        addToListFAB.setVisibility(View.VISIBLE);
        // TODO : Add Bottom Sheet
        addToListFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialogFragment bottomSheetDialogFragment = new WordListOptionBottomSheet();
                Bundle bundle = new Bundle();
                bundle.putString("url", baseUrl.url() + word);
                bundle.putString(WORD_TRANSFER_TEXT, word);
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_dictionary, menu);
        // Associate searchable configuration with the SearchView

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.onActionViewCollapsed();
                saveRecentWord(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    public void getAllRecentWords() {
        recentWordsManager.getRecentWords(new RecentWordListListener() {
            @Override
            public void onSuccess(List<RecentWord> words) {
                recentWords = words;
            }

            @Override
            public void onFailure(FirebaseError firebaseError) {
                Log.e("Error", firebaseError.toString());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    /*
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Dictionary Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                null,
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.blackshift.verbis.ui.activity/http/blackshift.verbis.io/")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
*/
    }

    @Override
    public void onStop() {
        super.onStop();
/*
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Dictionary Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                null,
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.blackshift.verbis.ui.activity/http/blackshift.verbis.io/")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
 */
    }

}
