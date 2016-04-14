package com.blackshift.verbis.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.BottomSheetBehavior;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.blackshift.verbis.App;
import com.blackshift.verbis.R;
import com.blackshift.verbis.adapters.HomePageBaseAdapter;
import com.blackshift.verbis.adapters.WordsOfTheWeekAdapter;
import com.blackshift.verbis.rest.model.RecentWord;
import com.blackshift.verbis.rest.model.wordlist.WordList;
import com.blackshift.verbis.ui.fragments.BottomSheetFragment;
import com.blackshift.verbis.ui.fragments.WordListTitlesRecyclerFragment;
import com.blackshift.verbis.utils.FirebaseKeys;
import com.blackshift.verbis.utils.listeners.RecentWordListListener;
import com.blackshift.verbis.utils.listeners.WordListListener;
import com.blackshift.verbis.utils.manager.RecentWordsManager;
import com.blackshift.verbis.utils.manager.WordListManager;
import com.bumptech.glide.Glide;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
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
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.blackshift.verbis.App.getApp;
import static com.blackshift.verbis.App.getContext;

public class HomePageActivity extends VerbisActivity
        implements NavigationView.OnNavigationItemSelectedListener,WordListTitlesRecyclerFragment.WordListSelectionListener {

    public static final String DATA_TRANSFER_TEXT = "data_transfer";
    public static final String DATA_TRANSFER_TEXT_1 = "data_transfer_1";
    private SearchHistoryTable mHistoryDatabase;
    private List<SearchItem> mSuggestionsList;
    private int mVersion = SearchCodes.VERSION_TOOLBAR;
    private int mStyle = SearchCodes.STYLE_TOOLBAR_CLASSIC;
    private int mTheme = SearchCodes.THEME_LIGHT;
    Toolbar toolbar;
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
    RecentWordsManager recentWordsManager;
    ArrayList<RecentWord> recentWords = new ArrayList<>();
    //Manually Init
    View header;
    ImageView imgView;
    @Bind(R.id.bottomSheetView) View bottomSheetView;
    BottomSheetBehavior mBottomSheetBehavior;
    ImageView imgview;
    TextView nameTextView;
    TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);
        init();
        manageToolbar();
        manageDrawer();
        manageWordOfTheDayViewPager();
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                new BottomSheetFragment().show(getSupportFragmentManager(), HomePageActivity.class.getSimpleName());
                /*mBottomSheetBehavior.setPeekHeight((int) Utiloid.CONVERSION_UTILS.dpiToPixels(300.0f));
                if ((mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) || mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN)
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);*/
            }
        });
        mangeBaseViewPager();

    }

    @Override
    protected void onStart() {
        super.onStart();
        populateRecentWords();
    }

    private void populateRecentWords() {

        recentWordsManager = new RecentWordsManager(App.getContext(), FirebaseKeys.RECENT_WORDS);
        recentWordsManager.getRecentWords(new RecentWordListListener() {
            @Override
            public void onSuccess(List<RecentWord> words) {
                recentWords.addAll(words);
                manageSearchView();
            }

            @Override
            public void onFailure(FirebaseError firebaseError) {
                Log.d("error", firebaseError.toString());
            }
        });

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
                    String provider = authData.getProvider();
                    String name,email;
                    if(provider.equals("google"))  {
                        Glide.with(getApplicationContext()).load(imgurl).bitmapTransform(new CropCircleTransformation(getContext())).into(imgview);
                        name = (String) authData.getProviderData().get("displayName");
                        Log.d("Name:",name);
                        nameTextView.setText(name);
                        email = (String) authData.getProviderData().get("email");
                        emailTextView.setText(email);
                    }
                    else if(provider.equals("password")){
                        ColorGenerator generator = ColorGenerator.MATERIAL;
                        email = (String) authData.getProviderData().get("email");
                        emailTextView.setText(email);
                        String letter = String.valueOf(email.charAt(0));
                        TextDrawable drawable = TextDrawable.builder().beginConfig().toUpperCase().endConfig().buildRound(letter,generator.getRandomColor());
                        imgview.setImageDrawable(drawable);

                    }
                    //set textview values after updating firebase rules
                }
            }
        });


        navigationView.setNavigationItemSelectedListener(this);
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
                Log.d("word in home", query);
                searchView.clearFocus();
                openDictionaryActivity(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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
                Log.d("word in home", text + " " + position);
                searchView.clearFocus();
                openDictionaryActivity(text.toString());
            }
        });
        searchView.setAdapter(mSearchAdapter);
    }

    private void openDictionaryActivity(String text) {
        Intent intent = new Intent(HomePageActivity.this, DictionaryActivity.class);
        intent.setAction(Intent.ACTION_SEARCH);
        ArrayList<String> strings = new ArrayList<>();
        intent.putExtra(DATA_TRANSFER_TEXT, text);
        Log.d("Home", recentWords.size() + "");
        for (RecentWord recentWord : recentWords){
            strings.add(recentWord.getWord());
        }
        intent.putStringArrayListExtra(DATA_TRANSFER_TEXT_1, strings);
        startActivity(intent);
    }

    private void showSearchView() {
        mSuggestionsList.clear();
        for (RecentWord recentWord : recentWords) {
            Log.d("word", recentWord.getWord());
            mHistoryDatabase.addItem(new SearchItem(recentWord.getWord()));
        }
        mSuggestionsList.addAll(mHistoryDatabase.getAllItems());
        searchView.setOnSearchMenuListener(new SearchView.SearchMenuListener() {
            @Override
            public void onMenuClick() {
                drawer.openDrawer(GravityCompat.START);
            }
        });
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_home);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        searchView = (SearchView) findViewById(R.id.searchView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //BottomSheet
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
        header = navigationView.getHeaderView(0);
        imgview = (ImageView) header.findViewById(R.id.imageView);
        nameTextView = (TextView) header.findViewById(R.id.nameTextView);
        emailTextView = (TextView) header.findViewById(R.id.emailTextView);
        //Set the pager with an adapter
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
