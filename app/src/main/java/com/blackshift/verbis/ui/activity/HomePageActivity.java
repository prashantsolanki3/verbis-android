package com.blackshift.verbis.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blackshift.verbis.App;
import com.blackshift.verbis.R;
import com.blackshift.verbis.adapters.HomePageBaseAdapter;
import com.blackshift.verbis.adapters.WordsOfTheWeekAdapter;
import com.blackshift.verbis.utils.listeners.WordListener;
import com.blackshift.verbis.utils.manager.RecentWordsManager;
import com.firebase.client.FirebaseError;
import com.lapism.searchview.adapter.SearchAdapter;
import com.lapism.searchview.adapter.SearchItem;
import com.lapism.searchview.history.SearchHistoryTable;
import com.lapism.searchview.view.SearchCodes;
import com.lapism.searchview.view.SearchView;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String DATA_TRANSFER_TEXT = "data_transfer";
    private SearchHistoryTable mHistoryDatabase;
    private List<SearchItem> mSuggestionsList;
    private int mVersion = SearchCodes.VERSION_TOOLBAR;
    private int mStyle = SearchCodes.STYLE_TOOLBAR_CLASSIC;
    private int mTheme = SearchCodes.THEME_LIGHT;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    FloatingActionButton fab;
    ViewPager pager, baseViewpager;
    TabLayout tabLayout;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        init();
        manageToolbar();
        manageSearchView();
        manageFab();
        manageDrawer();
        manageWordOfTheDayViewPager();
        mangeBaseViewPager();

    }

    private void mangeBaseViewPager() {
        baseViewpager.setAdapter(new HomePageBaseAdapter(getSupportFragmentManager()));

        tabLayout.setupWithViewPager(baseViewpager);
        baseViewpager.addOnPageChangeListener(new TabLayout.
                TabLayoutOnPageChangeListener(tabLayout));
    }

    private void manageWordOfTheDayViewPager() {

        pager.setAdapter(new WordsOfTheWeekAdapter(getSupportFragmentManager()));
        //Bind the title indicator to the adapter
        //TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.titles);
        //titleIndicator.setViewPager(pager);

    }

    private void manageDrawer() {
        /*
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
*/
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void manageFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mHistoryDatabase.addItem(new SearchItem(query));
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                searchView.clearFocus();
                RecentWordsManager recentWordsManager = new RecentWordsManager(App.getContext());
                recentWordsManager.addRecentWord(query, new WordListener() {
                    @Override
                    public void onSuccess(@Nullable Object word) {
                        Log.d("Firebase_Recent_Words", "added");
                    }

                    @Override
                    public void onFailure(FirebaseError firebaseError) {

                        Log.d("Firebase_Recent_Words", "not added \n" + firebaseError.getMessage());
                    }
                });
                openDictionaryActivity(query);
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

        searchView.setOnSearchMenuListener(new SearchView.SearchMenuListener() {
            @Override
            public void onMenuClick() {
                drawer.openDrawer(GravityCompat.START);
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
                Toast.makeText(getApplicationContext(), text + ", position: " + position, Toast.LENGTH_SHORT).show();
                searchView.clearFocus();
                openDictionaryActivity(text.toString());
            }
        });
        searchView.setAdapter(mSearchAdapter);
    }

    private void openDictionaryActivity(String text) {
        Intent intent = new Intent(HomePageActivity.this, DictionaryActivity.class);
        intent.setAction(Intent.ACTION_SEARCH);
        intent.putExtra(DATA_TRANSFER_TEXT, text);
        startActivity(intent);
    }

    private void showSearchView() {
        mSuggestionsList.clear();
        mSuggestionsList.addAll(mHistoryDatabase.getAllItems());
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        searchView = (SearchView) findViewById(R.id.searchView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Set the pager with an adapter
        pager = (ViewPager)findViewById(R.id.words_of_week_pager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        baseViewpager = (ViewPager) findViewById(R.id.home_page_base_pager);

    }

    private void manageToolbar() {
        setSupportActionBar(toolbar);

        collapsingToolbarLayout.setTitleEnabled(false);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        switch (item.getItemId()) {
            case R.id.action_search:
                showSearchView();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                // NavUtils.navigateUpTo();
                // DatabaseUtils ... finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        */
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchView.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && results.size() > 0) {
                String searchWrd = results.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

/*
    public TabLayout getTabLayout(){
        return tabLayout;
    }
    */
}
