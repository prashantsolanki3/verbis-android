package com.blackshift.verbis.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.blackshift.verbis.App;
import com.blackshift.verbis.R;
import com.blackshift.verbis.adapters.HomePageBaseAdapter;
import com.blackshift.verbis.adapters.WordsOfTheWeekAdapter;
import com.blackshift.verbis.ui.fragments.BottomSheetFragment;
import com.bumptech.glide.Glide;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.lapism.searchview.adapter.SearchAdapter;
import com.lapism.searchview.adapter.SearchItem;
import com.lapism.searchview.history.SearchHistoryTable;
import com.lapism.searchview.view.SearchCodes;
import com.lapism.searchview.view.SearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.prashantsolanki3.utiloid.Utiloid;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;

import static com.blackshift.verbis.App.getApp;
import static com.blackshift.verbis.App.getContext;

public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SearchHistoryTable mHistoryDatabase;
    private List<SearchItem> mSuggestionsList;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    SearchView searchView;
    FloatingActionButton fab;
    ViewPager pager, baseViewpager;
    TabLayout tabLayout;
    @Bind(R.id.bottomSheetView) View bottomSheetView;
    BottomSheetBehavior mBottomSheetBehavior;
    NavigationView navigationView;
    View header;
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
        manageSearchView();
        manageFab();
        manageDrawer();
        manageWordOfTheDayViewPager();


        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                new BottomSheetFragment().show(getSupportFragmentManager(),HomePageActivity.class.getSimpleName());
                /*mBottomSheetBehavior.setPeekHeight((int) Utiloid.CONVERSION_UTILS.dpiToPixels(300.0f));
                if ((mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) || mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN)
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);*/
            }
        });
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

        searchView = (SearchView) findViewById(R.id.searchView);
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
        searchView.setOnSearchViewListener(new SearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                fab.hide();
            }

            @Override
            public void onSearchViewClosed() {
                fab.show();
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
        toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_home);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        searchView = (SearchView) findViewById(R.id.searchView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //BottomSheet
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
        header = navigationView.getHeaderView(0);
        imgview = (ImageView) header.findViewById(R.id.imageView);
        nameTextView = (TextView) header.findViewById(R.id.nameTextView);
        emailTextView = (TextView) header.findViewById(R.id.emailTextView);
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

        if (id == R.id.nav_home_activity) {
            // Handle the camera action
        } else if (id == R.id.nav_search_activity) {

        } else if (id == R.id.nav_wordlist_vp_activity) {
            this.finish();
            startActivity(new Intent(this,WordListViewPagerActivity.class));
        }  else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_user_log_out) {
            App.getApp().getFirebase().unauth();

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
/*
    public TabLayout getTabLayout(){
        return tabLayout;
    }
    */
}
