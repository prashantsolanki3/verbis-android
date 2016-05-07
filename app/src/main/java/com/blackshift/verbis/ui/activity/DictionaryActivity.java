package com.blackshift.verbis.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.blackshift.verbis.App;
import com.blackshift.verbis.R;
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
import com.blackshift.verbis.utils.manager.RecentWordsManager;
import com.dpizarro.autolabel.library.AutoLabelUI;
import com.dpizarro.autolabel.library.Label;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.prashantsolanki3.snaplibrary.snap.adapter.SnapAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.layout.managers.SnapNestedLinearLayoutManger;
import io.github.prashantsolanki3.snaplibrary.snap.layout.wrapper.SnapLayoutWrapper;
import retrofit.BaseUrl;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DictionaryActivity extends VerbisActivity {

    public static final String ARG_BOTTOMSHEET_WORD = "word_transfer_text";
    public static final String ARG_WORD_URL = "word_url";

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

    public static Intent createIntent(Context content,String text){
        Intent intent = new Intent(content, DictionaryActivity.class);
        intent.setAction(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, text);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        initView();
        manageToolbar();
        recentWordsManager = new RecentWordsManager(App.getContext(), FirebaseKeys.RECENT_WORDS);
        wordNotFoundManager = new RecentWordsManager(App.getContext(), FirebaseKeys.WORDS_NOT_FOUND);
        handleIntent(getIntent());

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void manageToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    AutoLabelUI synonyms,antonyms;

    private void initView() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        recyclerView = (RecyclerView) findViewById(R.id.dictionary_recycler_view);
        shareFAB = (FloatingActionButton) findViewById(R.id.fab_share);
        addToListFAB = (FloatingActionButton) findViewById(R.id.fab_add_to_list);
        shareFAB.setImageDrawable(new IconDrawable(this, MaterialIcons.md_share)
                .actionBarSize()
                .colorRes(android.R.color.white));
        addToListFAB.setImageDrawable(new IconDrawable(this, MaterialIcons.md_add)
                .actionBarSize()
                .colorRes(android.R.color.white));
        pronounceFAB = (FloatingActionButton) findViewById(R.id.fab_pronounce);
        synonyms = (AutoLabelUI) findViewById(R.id.synonyms);
        synonyms.clear();
        antonyms = (AutoLabelUI) findViewById(R.id.antonyms);
        antonyms.clear();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        ((AppBarLayout)findViewById(R.id.appbar)).setExpanded(true,true);


        if (Intent.ACTION_SEARCH.equals(intent.getAction())){

            collapsingToolbarLayout.setTitle("Loading...");

            query = intent.getStringExtra(SearchManager.QUERY);

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

                                manageShare(response.body().getWord());
                                // TODO : Add pronunciation
                                manageAddWordFab(retrofit.baseUrl(), query);

                                if (response.body().getPronunciation() != null) {
                                    collapsingToolbarLayout.setTitle(response.body().getWord() + " (" +
                                            response.body().getPronunciation().getAll() + ")");
                                }else{
                                    collapsingToolbarLayout.setTitle(response.body().getWord());
                                }

                                setupRecyclerView(response.body());
                                recentWords.add(query);
                            }else{
                                //TODO: Not Found
                                collapsingToolbarLayout.setTitle("Not Found.");
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
        recyclerView.setLayoutManager(new SnapNestedLinearLayoutManger(recyclerView,this));

        ArrayList<SnapLayoutWrapper> layoutWrappers = new ArrayList<>();
        layoutWrappers.add(new SnapLayoutWrapper(String.class, PartOfSpeechViewHolder.class, R.layout.part_of_speech_item, 1));
        layoutWrappers.add(new SnapLayoutWrapper(Meaning.class, MeaningViewHolder.class, R.layout.meaning_item, 2));
        layoutWrappers.add(new SnapLayoutWrapper(Example.class, ExampleViewHolder.class, R.layout.example, 3));

        snapMultiAdapter = new SnapAdapter(this, layoutWrappers, recyclerView
                //, (ViewGroup)findViewById(R.id.recyclerView_alternate)
                );

        Set<String> partOfSpeech = Utils.getAllPartOfSpeech(wordsApiResult.getResults());
        List<List<MeaningAndExample>> meaningAndExampleList = Utils.getResultSortedByPartOfSpeech(wordsApiResult.getResults(), partOfSpeech);

        HashSet<String> synonymsList = Utils.getSynonyms(wordsApiResult.getResults());

        synonyms.clear();
        antonyms.clear();

        if(!synonymsList.isEmpty()) {
            findViewById(R.id.synonyms_header).setVisibility(View.VISIBLE);
            synonyms.setVisibility(View.VISIBLE);

            for (String s : synonymsList)
                synonyms.addLabel(s);
            synonyms.setOnLabelClickListener(new AutoLabelUI.OnLabelClickListener() {
                @Override
                public void onClickLabel(View v) {
                    startActivity(createIntent(DictionaryActivity.this,((Label)v).getText()));
                }
            });
        }else {
            synonyms.setVisibility(View.GONE);
            findViewById(R.id.synonyms_header).setVisibility(View.GONE);
        }

        HashSet<String> antonymsList = Utils.getAntonyms(wordsApiResult.getResults());
        if(!antonymsList.isEmpty()) {
            findViewById(R.id.antonyms_header).setVisibility(View.VISIBLE);
            antonyms.setVisibility(View.VISIBLE);
            for (String s : antonymsList)
                antonyms.addLabel(s);
            antonyms.setOnLabelClickListener(new AutoLabelUI.OnLabelClickListener() {
                @Override
                public void onClickLabel(View v) {
                    startActivity(createIntent(DictionaryActivity.this,((Label)v).getText()));
                }
            });
        }else {
            antonyms.setVisibility(View.GONE);
            findViewById(R.id.antonyms_header).setVisibility(View.GONE);
        }


        snapMultiAdapter.add("");
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
        snapMultiAdapter.add("");
        recyclerView.setAdapter(snapMultiAdapter);

    }

    private void manageAddWordFab(final BaseUrl baseUrl, final String word){

        addToListFAB.setVisibility(View.VISIBLE);

        addToListFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                collapsingToolbarLayout.setMinimumHeight(500);
                recyclerView.setVisibility(View.VISIBLE);

                BottomSheetDialogFragment bottomSheetDialogFragment = new WordListOptionBottomSheet();
                Bundle bundle = new Bundle();
                bundle.putString(ARG_WORD_URL, baseUrl.url() + word);
                bundle.putString(ARG_BOTTOMSHEET_WORD, word);
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
                try {
                    if (findViewById(R.id.dictionary_scroll_view) != null)
                        ((NestedScrollView) findViewById(R.id.dictionary_scroll_view)).smoothScrollTo(0, 0);
                }catch (Exception e){
                    e.printStackTrace();
                }
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

    @Override
    public void onStart() {
        super.onStart();

        client.connect();

        Thing object = new Thing.Builder()
                .setName(getIntent().getStringExtra(SearchManager.QUERY))
                .setDescription("description")
                .setUrl(Uri.parse("https://www.verbis.io"))
                .build();

        Action viewAction = new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();

        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        Thing object = new Thing.Builder()
                .setName(getIntent().getStringExtra(SearchManager.QUERY))
                .setDescription("description")
                .setUrl(Uri.parse("https://www.verbis.io"))
                .build();

        Action viewAction = new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();

        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
