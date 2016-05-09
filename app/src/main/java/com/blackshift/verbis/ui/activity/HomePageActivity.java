package com.blackshift.verbis.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackshift.verbis.App;
import com.blackshift.verbis.R;
import com.blackshift.verbis.adapters.HomePageBaseAdapter;
import com.blackshift.verbis.adapters.WordsOfTheWeekAdapter;
import com.blackshift.verbis.rest.model.RecentWord;
import com.blackshift.verbis.rest.model.verbismodels.WordOfTheDay;
import com.blackshift.verbis.utils.AnswersKeys;
import com.blackshift.verbis.utils.FirebaseKeys;
import com.blackshift.verbis.utils.listeners.RecentWordListListener;
import com.blackshift.verbis.utils.manager.RecentWordsManager;
import com.bumptech.glide.Glide;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.SearchEvent;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
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
import io.github.prashantsolanki3.utiloid.Utiloid;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.blackshift.verbis.App.getApp;
import static com.blackshift.verbis.App.getContext;
import com.crashlytics.android.answers.Answers;

public class HomePageActivity extends VerbisActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private SearchHistoryTable mHistoryDatabase;
    private List<SearchItem> mSuggestionsList;
    private int mVersion = SearchCodes.VERSION_TOOLBAR;
    private int mStyle = SearchCodes.STYLE_TOOLBAR_CLASSIC;
    private int mTheme = SearchCodes.THEME_LIGHT;
    @Bind(R.id.toolbar_home)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar_home)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.searchView)
    SearchView searchView;
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
    ImageView imgview;
    TextView nameTextView;
    TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);
        initHeader();
        manageToolbar();
        manageDrawer();
        manageWordOfTheDayViewPager();
        mangeBaseViewPager();
        try {
            findViewById(R.id.appbar).getLayoutParams().height = (int) Utiloid.DISPLAY_UTILS.getScreenWidthPixels();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        populateRecentWords();
    }

    private void initHeader() {
        //BottomSheet
        header = navigationView.getHeaderView(0);
        imgview = (ImageView) header.findViewById(R.id.imageView);
        nameTextView = (TextView) header.findViewById(R.id.nameTextView);
        emailTextView = (TextView) header.findViewById(R.id.emailTextView);
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
    WordsOfTheWeekAdapter wordsOfTheWeekAdapter;
    private void manageWordOfTheDayViewPager() {

        wordsOfTheWeekAdapter = new WordsOfTheWeekAdapter(getSupportFragmentManager());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<WordOfTheDay> results = realm.where(WordOfTheDay.class).findAll();
        wordsOfTheWeekAdapter.setWords(results);

        results.addChangeListener(new RealmChangeListener<RealmResults<WordOfTheDay>>() {
            @Override
            public void onChange(RealmResults<WordOfTheDay> element) {
                    wordsOfTheWeekAdapter.setWords(element);
            }
        });
        viewPagerWordOfTheDay.setOffscreenPageLimit(4);
        viewPagerWordOfTheDay.setAdapter(wordsOfTheWeekAdapter);
        pageIndicator.setViewPager(viewPagerWordOfTheDay);
    }

    private void manageDrawer() {
        Firebase ref= getApp().getFirebase();
        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    String imgurl = (String) authData.getProviderData().get("profileImageURL");
                    String provider = authData.getProvider();
                    String name,email;
                    Glide.with(getApplicationContext()).load(imgurl).bitmapTransform(new CropCircleTransformation(getContext())).into(imgview);
                    name = (String) authData.getProviderData().get("displayName");
                    Log.d("Name:",name);
                    nameTextView.setText(name);
                    email = (String) authData.getProviderData().get("email");
                    emailTextView.setText(email);

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
                Answers.getInstance().logSearch(new SearchEvent()
                        .putQuery(text.toString())
                        .putCustomAttribute(AnswersKeys.KEY_LOCATION,"Homepage->Searchview"));
                openDictionaryActivity(text.toString());


            }
        });
        searchView.setAdapter(mSearchAdapter);
    }

    private void openDictionaryActivity(String text) {
        startActivity(DictionaryActivity.createIntent(this,text));
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
            Answers.getInstance().logCustom(new CustomEvent("NavDrawer item select")
                    .putCustomAttribute(AnswersKeys.KEY_EVENT,"Navdrawer SearchActivity")
                    .putCustomAttribute(AnswersKeys.KEY_LOCATION,"Navdrawer")
            );

            startActivity(DictionaryActivity.createIntent(this,""));
        } else if (id == R.id.nav_wordlist_vp_activity) {
            Answers.getInstance().logCustom(new CustomEvent("NavDrawer item select")
                    .putCustomAttribute(AnswersKeys.KEY_EVENT,"Navdrawer WordlistActivity")
                    .putCustomAttribute(AnswersKeys.KEY_LOCATION,"Navdrawer")
            );
            startActivity(new Intent(this,WordListViewPagerActivity.class));
        }  else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_user_log_out) {

            Answers.getInstance().logCustom(new CustomEvent("NavDrawer item select")
                    .putCustomAttribute(AnswersKeys.KEY_EVENT,"Navdrawer Logout")
                    .putCustomAttribute(AnswersKeys.KEY_LOCATION,"Navdrawer")
            );
            App.getApp().getFirebase().unauth();
            Firebase ref = getApp().getFirebase();
            ref.addAuthStateListener(new Firebase.AuthStateListener() {
                @Override
                public void onAuthStateChanged(AuthData authData) {
                    if (authData !=null) {
                        String provider = authData.getProvider();
                        if(provider.equals("facebook"))
                            LoginManager.getInstance().logOut();

                    }
                }
            });


            if(Build.VERSION.SDK_INT>=21)
                this.finishAndRemoveTask();
            else
                this.finish();
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
}
