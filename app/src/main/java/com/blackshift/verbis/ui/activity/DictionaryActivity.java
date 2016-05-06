package com.blackshift.verbis.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blackshift.verbis.App;
import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.RecentWord;
import com.blackshift.verbis.rest.model.recyclerviewmodels.Example;
import com.blackshift.verbis.rest.model.recyclerviewmodels.Meaning;
import com.blackshift.verbis.rest.model.recyclerviewmodels.MeaningAndExample;
import com.blackshift.verbis.rest.model.wordapimodels.WordsApiResult;
import com.blackshift.verbis.rest.providers.RecentSuggestionsProvider;
import com.blackshift.verbis.ui.fragments.WordListOptionBottomSheet;
import com.blackshift.verbis.ui.viewholders.ExampleViewHolder;
import com.blackshift.verbis.ui.viewholders.MeaningViewHolder;
import com.blackshift.verbis.ui.viewholders.PartOfSpeechViewHolder;
import com.blackshift.verbis.utils.FirebaseKeys;
import com.blackshift.verbis.utils.Utils;
import com.blackshift.verbis.utils.listeners.RecentWordListListener;
import com.blackshift.verbis.utils.listeners.RecentWordListener;
import com.blackshift.verbis.utils.manager.RecentWordsManager;
import com.firebase.client.FirebaseError;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialIcons;

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
    ArrayList<String> recentWords = new ArrayList<>();
    RecentWordsManager recentWordsManager;
    RecentWordsManager wordNotFoundManager;

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
        recentWordsManager = new RecentWordsManager(App.getContext(), FirebaseKeys.RECENT_WORDS);
        wordNotFoundManager = new RecentWordsManager(App.getContext(), FirebaseKeys.WORDS_NOT_FOUND);
        Log.d("Dictionary", getIntent().getStringExtra(HomePageActivity.DATA_TRANSFER_TEXT));
        Log.d("Dictionary", getIntent().getStringArrayListExtra(HomePageActivity.DATA_TRANSFER_TEXT_1).size() + "");
        recentWords = getIntent().getStringArrayListExtra(HomePageActivity.DATA_TRANSFER_TEXT_1);
        //getAllRecentWords();
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

        if (Intent.ACTION_SEARCH.equals(intent.getAction())){

            if (query == null) {
                query = intent.getStringExtra(SearchManager.QUERY);
            }
            if (Intent.ACTION_VIEW.equals(intent.getAction())){
                query = intent.getData().getPath().substring(intent.getData().getPath().lastIndexOf("=") + 1);
            }
            Log.d("word in dictionary", query);
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

                                manageShare(response.body().getWord());
                                // TODO : Add pronunciation
                                //pronounceFAB.setVisibility(View.VISIBLE);
/*
                                if (((TextView)findViewById(R.id.word)) != null) {
                                    ((TextView)findViewById(R.id.word)).setText(response.body().getWord());
                                }

                                if (((TextView)findViewById(R.id.cv_part_of_speech)) != null) {
                                    ((TextView)findViewById(R.id.cv_part_of_speech)).setText(response.body().getResults()
                                                                                    .get(0).getPartOfSpeech());
                                }

                                if (((TextView)findViewById(R.id.cv_meaning)) != null) {
                                    ((TextView)findViewById(R.id.cv_meaning)).setText(response.body().getResults()
                                            .get(0).getDefinition());
                                }
*/
                                manageAddWordFab(retrofit.baseUrl(), query);
                                if (response.body().getPronunciation() != null) {
                                    collapsingToolbarLayout.setTitle(response.body().getWord() + "(" +
                                            response.body().getPronunciation().getAll() + ")");
                                }else{
                                    collapsingToolbarLayout.setTitle(response.body().getWord());
                                }
                                saveRecentWord(recentWordsManager, query);
                                setupRecyclerView(response.body());
                                recentWords.add(query);
                                Log.d("HandleIntent", "word saved" + recentWords.size());

                            }else{
                                collapsingToolbarLayout.setTitle("Not Found.");
                                saveRecentWord(wordNotFoundManager, query);
                            }

                        }
                        @Override
                        public void onFailure(Throwable t) {
                            t.printStackTrace();
                        }
                    });

        }

    }

    private void manageShare(final String word) {
        shareFAB.setVisibility(View.VISIBLE);
        shareFAB.setBackgroundDrawable(new IconDrawable(this, MaterialIcons.md_add));
        shareFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_message_part_1)
                                    + word + getResources().getString(R.string.share_message_part_2));
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_app_sub));
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.share_title)));
            }
        });
    }

    private void setupRecyclerView(WordsApiResult wordsApiResult) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<SnapLayoutWrapper> layoutWrappers = new ArrayList<>();
        layoutWrappers.add(new SnapLayoutWrapper(String.class, PartOfSpeechViewHolder.class, R.layout.part_of_speech_item, 1));
        layoutWrappers.add(new SnapLayoutWrapper(Meaning.class, MeaningViewHolder.class, R.layout.meaning_item, 2));
        layoutWrappers.add(new SnapLayoutWrapper(Example.class, ExampleViewHolder.class, R.layout.example, 3));

        snapMultiAdapter = new SnapAdapter(this, layoutWrappers, recyclerView
                //, (ViewGroup)findViewById(R.id.recyclerView_alternate)
        );

        Set<String> partOfSpeech = Utils.getAllPartOfSpeech(wordsApiResult.getResults());
        ArrayList<ArrayList<MeaningAndExample>> meaningAndExampleList =
                Utils.getResultSortedByPartOfSpeech(wordsApiResult.getResults(), partOfSpeech);

        int i = 0;
        for (String string : partOfSpeech){
            snapMultiAdapter.add(string);
            for (MeaningAndExample meaningAndExample : meaningAndExampleList.get(i)){
                snapMultiAdapter.add(new Meaning(meaningAndExample.getMeaning()));
                Log.d("meaning", meaningAndExample.getMeaning());
                for (String st: meaningAndExample.getExample()){
                    Log.d("example", st);
                    snapMultiAdapter.add(new Example(st));
                }
            }
            i++;
        }
        recyclerView.setAdapter(snapMultiAdapter);

    }

    private void saveRecentWord(RecentWordsManager manager, String query) {

        Log.d("test", !recentWords.contains(query) + "");
        if (!recentWords.contains(query)) {

            manager.addRecentWord(query, new RecentWordListener() {
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

                collapsingToolbarLayout.setMinimumHeight(500);
                /*
                assert ((LinearLayout)findViewById(R.id.collapsed_view)) != null;
                ((LinearLayout)findViewById(R.id.collapsed_view)).setVisibility(View.GONE);
*/
                recyclerView.setVisibility(View.VISIBLE);


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
                //recentWords = words;
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
        client.connect();
        Thing object = new Thing.Builder()
                .setName("Verbis Dictionary")
                .setDescription("description")
                .setUrl(Uri.parse("https://www.verbis.io"))
                .build();

        Action viewAction = new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
        /*
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
        */
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Thing object = new Thing.Builder()
                .setName("Verbis Dictionary")
                .setDescription("description")
                .setUrl(Uri.parse("https://www.verbis.io"))
                .build();

        Action viewAction = new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
       /*
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
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
