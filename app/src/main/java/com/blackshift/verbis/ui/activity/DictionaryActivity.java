package com.blackshift.verbis.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.providers.RecentSuggestionsProvider;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DictionaryActivity extends AppCompatActivity {

    @Bind(R.id.main_container)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.searchView)
    SearchView searchView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    String query;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        query = getIntent().getStringExtra(HomePageActivity.DATA_TRANSFER_TEXT);
        handleIntent(getIntent());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {
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
/*
            App.getDictionaryService().getWordDetail(query)
                    .enqueue(new Callback<DictionaryResults>() {
                        @Override
                        public void onResponse(Response<DictionaryResults> response, Retrofit retrofit) {
                            Log.d(DictionaryActivity.class.getName(), response.body().getResults().size() + "");
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            t.printStackTrace();
                        }
                    });
  */
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_dictionary, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
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
    /*

public class DictionaryActivity extends AppCompatActivity {

    CoordinatorLayout coordinatorLayout;
    private SearchHistoryTable mHistoryDatabase;
    private List<SearchItem> mSuggestionsList;
    private int mVersion = SearchCodes.VERSION_TOOLBAR;
    private int mStyle = SearchCodes.STYLE_TOOLBAR_CLASSIC;
    private int mTheme = SearchCodes.THEME_LIGHT;
    SearchView searchView;
    private String query = null;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_container);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        query = getIntent().getStringExtra(HomePageActivity.DATA_TRANSFER_TEXT);
        manageSearchView();
        handleIntent(getIntent());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void manageSearchView() {

        mHistoryDatabase = new SearchHistoryTable(this);
        mSuggestionsList = new ArrayList<>();

        // SearchView basic attributes  ------------------------------------------------------------
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setVersion(mVersion);
        searchView.setStyle(mStyle);
        searchView.setTheme(mTheme);
        // -----------------------------------------------------------------------------------------
        searchView.setDivider(false);
        searchView.setHint("Search");
        searchView.setHint(R.string.search);
        searchView.setHintSize(getResources().getDimension(R.dimen.search_text_medium));
        searchView.setVoice(true);
        searchView.setVoiceText("Voice");
        searchView.setAnimationDuration(300);
        searchView.setShadowColor(ContextCompat.getColor(this, R.color.search_shadow_layout));
        showSearchView();
        searchView.show(true);
        searchView.setQuery(query);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mHistoryDatabase.addItem(new SearchItem(query));
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                searchView.clearFocus();
                inflateData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchViewListener(new SearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                // mFab.hide();
            }

            @Override
            public void onSearchViewClosed() {
                // mFab.show();
            }
        });

        List<SearchItem> mResultsList = new ArrayList<>();
        SearchAdapter mSearchAdapter = new SearchAdapter(this, mResultsList, mSuggestionsList, mTheme);
        mSearchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                CharSequence text = textView.getText();
                mHistoryDatabase.addItem(new SearchItem(text));
                Toast.makeText(getApplicationContext(), text  +", position: " + position, Toast.LENGTH_SHORT).show();
                inflateData(query);
                searchView.clearFocus();
            }
        });

        searchView.setAdapter(mSearchAdapter);
    }

    private void inflateData(String query) {
        App.getDictionaryService().getWordDetail(query)
                .enqueue(new Callback<DictionaryResults>() {
                    @Override
                    public void onResponse(Response<DictionaryResults> response, Retrofit retrofit) {
                        inflateView(response.body());
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    private void inflateView(DictionaryResults dictionaryResults) {

    }

    private void showSearchView() {
        mSuggestionsList.clear();
        mSuggestionsList.addAll(mHistoryDatabase.getAllItems());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            if (query == null) {
                query = intent.getStringExtra(SearchManager.QUERY);
            }
            manageSearchView();

            Snackbar.make(coordinatorLayout, query, Snackbar.LENGTH_LONG)
                    .show();
            SearchRecentSuggestions suggestions =
                    new SearchRecentSuggestions(this, RecentSuggestionsProvider.AUTHORITY, RecentSuggestionsProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            // TODO : Add in settings the facility to clear history
            // TODO : Add accent preference
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_dictionary, menu);
        // Associate searchable configuration with the SearchView
        /*
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Dictionary Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.blackshift.verbis.ui.activity/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Dictionary Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.blackshift.verbis.ui.activity/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    }


}

 */