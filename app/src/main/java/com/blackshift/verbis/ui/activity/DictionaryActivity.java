package com.blackshift.verbis.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.blackshift.verbis.App;
import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.recyclerviewmodels.Example;
import com.blackshift.verbis.rest.model.recyclerviewmodels.Meaning;
import com.blackshift.verbis.rest.model.recyclerviewmodels.MeaningAndExample;
import com.blackshift.verbis.rest.model.wordapimodels.WordsApiResult;
import com.blackshift.verbis.rest.providers.RecentSuggestionsProvider;
import com.blackshift.verbis.ui.dialogs.WordListOptionBottomSheet;
import com.blackshift.verbis.ui.viewholders.ExampleViewHolder;
import com.blackshift.verbis.ui.viewholders.MeaningViewHolder;
import com.blackshift.verbis.ui.viewholders.PartOfSpeechViewHolder;
import com.blackshift.verbis.utils.Utils;
import com.blackshift.verbis.utils.keys.FirebaseKeys;
import com.blackshift.verbis.utils.listeners.DictionaryListener;
import com.blackshift.verbis.utils.manager.DictionaryManager;
import com.blackshift.verbis.utils.manager.RecentWordsManager;
import com.bumptech.glide.Glide;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SearchEvent;
import com.dpizarro.autolabel.library.AutoLabelUI;
import com.dpizarro.autolabel.library.Label;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.prashantsolanki3.snaplibrary.snap.adapter.SnapAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.layout.managers.SnapNestedLinearLayoutManger;
import io.github.prashantsolanki3.snaplibrary.snap.layout.wrapper.SnapLayoutWrapper;

import static com.blackshift.verbis.ui.activity.DictionaryActivity.DictionaryState.CONNECTION_ERROR;
import static com.blackshift.verbis.ui.activity.DictionaryActivity.DictionaryState.ERROR;
import static com.blackshift.verbis.ui.activity.DictionaryActivity.DictionaryState.FOUND;
import static com.blackshift.verbis.ui.activity.DictionaryActivity.DictionaryState.LOADING;
import static com.blackshift.verbis.ui.activity.DictionaryActivity.DictionaryState.NOT_FOUND;
import static com.blackshift.verbis.ui.activity.DictionaryActivity.DictionaryState.SEARCH_PROMPT;

public class DictionaryActivity extends VerbisActivity {

    public static final String ARG_BOTTOMSHEET_WORD = "word_transfer_text";
    public static final String ARG_WORD_URL = "word_url";

    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;
    String query=null;
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
    ProgressBar progressBar;
    AutoLabelUI synonyms,antonyms;
    AdView adView;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public static Intent createIntent(Context content,String text){
        Intent intent = new Intent(content, DictionaryActivity.class);
        intent.setAction(Intent.ACTION_SEARCH);
        Uri uri = getUriFromQuery(text);

        intent.putExtra(SearchManager.QUERY, uri.getLastPathSegment());
        intent.setData(uri);

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
        onNewIntent(getIntent());
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void manageToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initView() {
        //Bind Views
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        recyclerView = (RecyclerView) findViewById(R.id.dictionary_recycler_view);
        shareFAB = (FloatingActionButton) findViewById(R.id.fab_share);
        addToListFAB = (FloatingActionButton) findViewById(R.id.fab_add_to_list);
        pronounceFAB = (FloatingActionButton) findViewById(R.id.fab_pronounce);
        synonyms = (AutoLabelUI) findViewById(R.id.synonyms);
        antonyms = (AutoLabelUI) findViewById(R.id.antonyms);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        adView = (AdView)findViewById(R.id.banner_ad_view);

        //Recycler should only be initialized once
        recyclerView.setLayoutManager(new SnapNestedLinearLayoutManger(recyclerView,this));

        ArrayList<SnapLayoutWrapper> layoutWrappers = new ArrayList<>();
        layoutWrappers.add(new SnapLayoutWrapper(String.class, PartOfSpeechViewHolder.class, R.layout.part_of_speech_item, 1));
        layoutWrappers.add(new SnapLayoutWrapper(Meaning.class, MeaningViewHolder.class, R.layout.meaning_item, 2));
        layoutWrappers.add(new SnapLayoutWrapper(Example.class, ExampleViewHolder.class, R.layout.example, 3));

        snapMultiAdapter = new SnapAdapter(this, layoutWrappers, recyclerView,(ViewGroup)findViewById(R.id.recyclerView_alternate));
        recyclerView.setAdapter(snapMultiAdapter);
        //Set Icons
        shareFAB.setImageDrawable(new IconDrawable(this, MaterialIcons.md_share)
                .actionBarSize()
                .colorRes(android.R.color.white));
        addToListFAB.setImageDrawable(new IconDrawable(this, MaterialIcons.md_add)
                .actionBarSize()
                .colorRes(android.R.color.white));
        //Clear Views
        snapMultiAdapter.clear();
        synonyms.clear();
        antonyms.clear();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())||Intent.ACTION_VIEW.equals(intent.getAction())) {

            query = getQueryFromIntent();

            if (query!=null&&!query.isEmpty()) {
                setState(LOADING);
                // Query must not be empty

                //Add query to suggestion provider
                SearchRecentSuggestions suggestions =
                        new SearchRecentSuggestions(this, RecentSuggestionsProvider.AUTHORITY, RecentSuggestionsProvider.MODE);
                suggestions.saveRecentQuery(query, null);
                // TODO : Add in settings the facility to clear history
                // TODO : Add accent preference


                new DictionaryManager(this).searchWord(query, new DictionaryListener() {
                    @Override
                    public void onFound(@NotNull WordsApiResult wordsApiResult) {
                        setupRecyclerView(wordsApiResult);
                        recentWords.add(query);

                        // TODO : Share Url
                        manageShare(wordsApiResult.getWord());
                        manageAddWordFab(query);
                        if (wordsApiResult.getPronunciation() != null) {
                            setCollapsingTitle(wordsApiResult.getWord() + " (" +
                                    wordsApiResult.getPronunciation().getAll() + ")");
                        } else {
                            setCollapsingTitle(wordsApiResult.getWord());
                        }
                        setState(FOUND);
                    }

                    @Override
                    public void onNotFound() {
                        setState(NOT_FOUND);
                    }

                    @Override
                    public void onFailure(@Nullable Throwable throwable) {
                        setState(ERROR);
                        if (throwable != null)
                            throwable.printStackTrace();
                    }
                });
            }else{
                setState(SEARCH_PROMPT);
            }
        }
    }

    void setCollapsingTitle(String title){
        collapsingToolbarLayout.setTitle(title);
    }

    private void manageShare(final String word) {

        shareFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_message_part_1)
                                    + word + getResources().getString(R.string.share_message_part_2) + "\n"+ getUriFromQuery(word));

                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_app_sub));

                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.share_title)));
            }
        });
    }

    private void setupRecyclerView(WordsApiResult wordsApiResult) {
        //Clear Views
        snapMultiAdapter.clear();
        synonyms.clear();
        antonyms.clear();

        Set<String> partOfSpeech = Utils.getAllPartOfSpeech(wordsApiResult.getResults());
        List<List<MeaningAndExample>> meaningAndExampleList = Utils.getResultSortedByPartOfSpeech(wordsApiResult.getResults(), partOfSpeech);

        HashSet<String> synonymsList = Utils.getSynonyms(wordsApiResult.getResults());

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

        // Adds Black space on top
        snapMultiAdapter.add("");
        int i = 0;
        for (String string : partOfSpeech){
            // Adds the Part of speech
            snapMultiAdapter.add(string);
            for (MeaningAndExample meaningAndExample : meaningAndExampleList.get(i)){
                // Adds the Meaning
                snapMultiAdapter.add(new Meaning(meaningAndExample.getMeaning()));
                for (String st: meaningAndExample.getExample()){
                    // Adds Example of the given Meaning
                    snapMultiAdapter.add(new Example(st));
                }
            }
            i++;
        }
        // Adds Black space on bottom
        snapMultiAdapter.add("");

    }

    private void manageAddWordFab(final String word){
        addToListFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetDialogFragment bottomSheetDialogFragment = new WordListOptionBottomSheet();
                Bundle bundle = new Bundle();

                bundle.putString(ARG_WORD_URL, getUriFromQuery(word).toString());
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
                setState(LOADING);
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

    public String getQueryFromIntent(){
        Intent intent = getIntent();
        String query=null;

        if(intent.getAction().equals(Intent.ACTION_VIEW)||intent.getAction().equals(Intent.ACTION_SEARCH)){
            Uri uri = intent.getData();

            if(uri!=null) {
                if (!uri.getPathSegments().get(0).equals("word"))
                    return null;

                if (uri.getPathSegments().size() != 2)
                    return null;

                query = uri.getLastPathSegment();
            }

            if(query==null)
                query = intent.getStringExtra(SearchManager.QUERY);
        }

        return query;
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        String query = getQueryFromIntent();
        if(query!=null) {
            Thing object = new Thing.Builder()
                    .setName(query)
                    .setDescription("Find the meaning of"+query+"on Verbis")
                    .setUrl(getUriFromQuery(query))
                    .build();

            Action viewAction = new Action.Builder(Action.TYPE_VIEW)
                    .setObject(object)
                    .setActionStatus(Action.STATUS_TYPE_ACTIVE)
                    .build();

            Answers.getInstance().logSearch(new SearchEvent().putQuery(query));

            AppIndex.AppIndexApi.start(client, viewAction);
        }
        addAdView();

    }

    public static Uri getUriFromQuery(String q){
        return Uri.parse(App.getContext().getResources().getString(R.string.verbis_base_url)+"word/"+q);
    }

    @Override
    public void onStop() {
        super.onStop();

        String query = getQueryFromIntent();
        if(query!=null) {
            Thing object = new Thing.Builder()
                    .setName(query)
                    .setUrl(getUriFromQuery(query))
                    .setDescription("Find the meaning of " + query + " on Verbis")
                    .build();

            Action viewAction = new Action.Builder(Action.TYPE_VIEW)
                    .setObject(object)
                    .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                    .build();

            AppIndex.AppIndexApi.end(client, viewAction);
        }

        client.disconnect();
    }

    void showFab(boolean show){
        addToListFAB.setClickable(show);
        shareFAB.setClickable(show);
        addToListFAB.setEnabled(show);
        shareFAB.setEnabled(show);

        if(show){
            addToListFAB.show();

            shareFAB.show();
        }else {
            addToListFAB.hide();

            shareFAB.hide();
        }
    }

    void showFoundLayout(boolean show){
        showFab(show);
        if(show){
            findViewById(R.id.main_meaning_layout).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.main_meaning_layout).setVisibility(View.GONE);
        }

    }

    void setState(@DictionaryState int state){

        progressBar.setVisibility(state==LOADING?View.VISIBLE:View.GONE);
        showFab(state==FOUND);
        if (findViewById(R.id.dictionary_scroll_view) != null)
            ((NestedScrollView) findViewById(R.id.dictionary_scroll_view)).smoothScrollTo(0, 0);

        View v = snapMultiAdapter.getViewFromId(R.layout.layout_image);
        ImageView img =(ImageView) v.findViewById(R.id.imageView);
        @DrawableRes
        int placeholder = R.drawable.search;
        ((AppBarLayout)findViewById(R.id.appbar)).setExpanded(true,true);
        switch (state){
            case ERROR:// Show connection error alternate view, hide fab,and a try again btn
                    placeholder = R.drawable.error;
                setCollapsingTitle("Error");
                //((AppBarLayout)findViewById(R.id.appbar)).setExpanded(false,true);
                break;
            case FOUND:// Show recycler and FAB, hide alternate views and progress bar
                   // ((AppBarLayout)findViewById(R.id.appbar)).setExpanded(true,true);
                //setCollapsingTitle(query); Handled in on Found
                break;
            case NOT_FOUND:// Show Not found alternate view, hide fab and progress bar
                placeholder = R.drawable.notfound;
                setCollapsingTitle("Not Found");
                //((AppBarLayout)findViewById(R.id.appbar)).setExpanded(false,true);
                break;
            case LOADING:// Show Loading alternate view or just hide recycler, hide fab and show progress bar
                //((AppBarLayout)findViewById(R.id.appbar)).setExpanded(true,true);
                setCollapsingTitle(query);
                break;
            case CONNECTION_ERROR:// Show connection error alternate view, hide fab,and a try again btn
                placeholder = R.drawable.networkerror;
                setCollapsingTitle("Network Error");
                //((AppBarLayout)findViewById(R.id.appbar)).setExpanded(false,true);
                break;
            case SEARCH_PROMPT:// expand the search bar, show search prompt alternate view and hide recycler, fab
            default://Search Prompt
                setCollapsingTitle("Search something");
                //((AppBarLayout)findViewById(R.id.appbar)).setExpanded(true,true);
        }


        showFoundLayout(state==FOUND);

        if(state==LOADING){
            img.setVisibility(View.GONE);
            v.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        }else {
            img.setVisibility(View.VISIBLE);
            v.findViewById(R.id.progressBar).setVisibility(View.GONE);
        }

        if(state==FOUND||state==LOADING){
            snapMultiAdapter.hideAlternateLayout();
        }else {
            Glide.with(App.getContext())
                    .load(placeholder)
                    .into(img);
            snapMultiAdapter.showAlternateLayout(v);
        }
    }

    @IntDef({CONNECTION_ERROR,
            FOUND,
            LOADING,
            NOT_FOUND,
            ERROR,
            SEARCH_PROMPT})
    @Retention(RetentionPolicy.SOURCE)
    @interface DictionaryState {
       int LOADING = 0,CONNECTION_ERROR = 2, NOT_FOUND = 3, FOUND = 4,ERROR = 5,SEARCH_PROMPT=6;
    }

    private void addAdView() {
        AdRequest adRequest = new AdRequest.Builder()
                                    .build();
        adView.loadAd(adRequest);
    }

}
