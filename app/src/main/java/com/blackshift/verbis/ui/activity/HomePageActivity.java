package com.blackshift.verbis.ui.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blackshift.verbis.App;
import com.blackshift.verbis.R;
import com.blackshift.verbis.adapters.HomePageBaseAdapter;
import com.blackshift.verbis.adapters.WordsOfTheWeekAdapter;
import com.blackshift.verbis.rest.model.wordlist.WordList;
import com.blackshift.verbis.ui.fragments.WordListTitlesRecyclerFragment;
import com.blackshift.verbis.utils.WordListManager;
import com.blackshift.verbis.utils.listeners.WordListListener;
import com.bumptech.glide.Glide;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;
import com.lapism.searchview.adapter.SearchAdapter;
import com.lapism.searchview.adapter.SearchItem;
import com.lapism.searchview.history.SearchHistoryTable;
import com.lapism.searchview.view.SearchCodes;
import com.lapism.searchview.view.SearchView;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.prashantsolanki3.snaplibrary.snap.adapter.AbstractSnapSelectableAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.adapter.SnapSelectableAdapter;

import static com.blackshift.verbis.App.getApp;

public class HomePageActivity extends VerbisActivity
        implements NavigationView.OnNavigationItemSelectedListener,WordListTitlesRecyclerFragment.WordListSelectionListener {

    private SearchHistoryTable mHistoryDatabase;
    private List<SearchItem> mSuggestionsList;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    @Bind(R.id.searchView)
    SearchView searchView;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.words_of_day_pager)
    ViewPager viewPagerWordOfTheDay;
    @Bind(R.id.home_page_base_pager)
    ViewPager baseViewpager;
    @Bind(R.id.viewpager_indicator)
    CirclePageIndicator pageIndicator;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    //Manually Init
    View header;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);
        init();
        manageToolbar();
        manageSearchView();
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

        viewPagerWordOfTheDay.setAdapter(new WordsOfTheWeekAdapter(getSupportFragmentManager()));
        pageIndicator.setViewPager(viewPagerWordOfTheDay);
        //Bind the title indicator to the adapter
        //TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.titles);
        //titleIndicator.setViewPager(viewPagerWordOfTheDay);

    }

    private void manageDrawer() {
        /*
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

*/
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if(wordListSnapAdapter!=null&&wordListSnapAdapter.isSelectionEnabled())
                wordListSnapAdapter.setSelectionEnabled(false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if(wordListSnapAdapter!=null){
                    if((newState==DrawerLayout.STATE_SETTLING||newState==DrawerLayout.STATE_DRAGGING)
                            &&wordListSnapAdapter.isSelectionEnabled())
                        wordListSnapAdapter.setSelectionEnabled(false);
                }
            }
        });
        Firebase ref= getApp().getFirebase();
        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    String imgurl = (String) authData.getProviderData().get("profileImageURL");
                    Glide.with(getApplicationContext()).load(imgurl).into(imgView);
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void manageSearchView() {

        mHistoryDatabase = new SearchHistoryTable(this);
        mSuggestionsList = new ArrayList<>();

        searchView.setVersion(SearchCodes.VERSION_TOOLBAR);
        searchView.setStyle(SearchCodes.STYLE_TOOLBAR_CLASSIC);
        searchView.setTheme(SearchCodes.THEME_LIGHT);

        searchView.setDivider(false);
        searchView.setHint(getString(R.string.search));
        searchView.setHintSize(getResources().getDimension(R.dimen.search_text_medium));
        searchView.setVoice(true);
        searchView.setVoiceText(getString(R.string.voice));
        searchView.setAnimationDuration(300);
        searchView.setShadowColor(ContextCompat.getColor(this, R.color.search_shadow_layout));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.hide(false);
                mHistoryDatabase.addItem(new SearchItem(query));
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomePageActivity.this, DictionaryActivity.class);
                intent.putExtra(SearchManager.QUERY, query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        List<SearchItem> mResultsList = new ArrayList<>();
        SearchAdapter mSearchAdapter = new SearchAdapter(this, mResultsList, mSuggestionsList, SearchCodes.VERSION_MENU_ITEM);
        mSearchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                searchView.hide(false);
                TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                CharSequence text = textView.getText();
                mHistoryDatabase.addItem(new SearchItem(text));
                Toast.makeText(getApplicationContext(), text + ", position: " + position, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(HomePageActivity.this, DictionaryActivity.class);
                intent.putExtra(SearchManager.QUERY, text);
                startActivity(intent);
            }
        });

        searchView.setAdapter(mSearchAdapter);

        if (mHistoryDatabase.getAllItems().size() > 0) {
            mSuggestionsList.addAll(mHistoryDatabase.getAllItems());
            searchView.show(true);
        }

        searchView.setOnSearchMenuListener(new SearchView.SearchMenuListener() {
            @Override
            public void onMenuClick() {
                drawer.openDrawer(GravityCompat.START);
            }
        });

    }

    private void init() {
        //Cannot be Init by ButterKnife.
        header =navigationView.getHeaderView(0);
        imgView = (ImageView) header.findViewById(R.id.imageView);
        fab.setImageDrawable(new IconDrawable(this, MaterialIcons.md_delete).colorRes(android.R.color.white));
        fab.hide();
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
        } else if(searchView.isSearchOpen()){
            searchView.hide(true);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home_activity) {
            // Handle the camera action
        } else if (id == R.id.nav_search_activity) {

        } else if (id == R.id.nav_wordlist_vp_activity) {
            startActivity(new Intent(this,WordListViewPagerActivity.class));
        }  else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_user_log_out) {
            App.getApp().getFirebase().unauth();

            if(Build.VERSION.SDK_INT>=21)
                this.finishAndRemoveTask();
            else
                this.finish();

        }else if(id == R.id.nav_wordy_activity){
            startActivity(new Intent(this,WordyActivity.class));
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

    SnapSelectableAdapter<WordList> wordListSnapAdapter;

    @OnClick(R.id.fab)
    void onClickFab(View view){
        WordListManager wordListManager = new WordListManager(this);
        List<WordList> selected = wordListSnapAdapter.getSelectedItems();
        if(selected!=null&&selected.size()>0){
            for(WordList wordList:selected)
                wordListManager.deleteWordList(wordList, new WordListListener() {
                    @Override
                    public void onSuccess(String firebaseReferenceString) {
                        if(wordListSnapAdapter.isSelectionEnabled())
                            wordListSnapAdapter.setSelectionEnabled(false);
                    }

                    @Override
                    public void onFailure(FirebaseError firebaseError) {

                    }
                });
        }
    }

    @Override
    public void setSnapAdapter(SnapSelectableAdapter<WordList> adapter) {
        this.wordListSnapAdapter = adapter;
    }

    @Override
    public void onSelectionModeEnabled(AbstractSnapSelectableAdapter.SelectionType selectionType) {
        fab.show();
    }

    @Override
    public void onSelectionModeDisabled(AbstractSnapSelectableAdapter.SelectionType selectionType) {
        fab.hide();
    }

    @Override
    public void onItemSelected(WordList wordList, int i) {

    }

    @Override
    public void onItemDeselected(WordList wordList, int i) {

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
}
