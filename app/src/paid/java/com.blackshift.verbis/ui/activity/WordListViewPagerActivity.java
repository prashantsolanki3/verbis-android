package com.blackshift.verbis.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;

import com.blackshift.verbis.App;
import com.blackshift.verbis.R;
import com.blackshift.verbis.adapters.WordListViewPagerAdapter;
import com.blackshift.verbis.rest.model.wordlist.WordList;
import com.blackshift.verbis.ui.widgets.WordListViewPager;
import com.blackshift.verbis.utils.listeners.WordListArrayListener;
import com.blackshift.verbis.utils.manager.WordListManager;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseError;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;
import com.viewpagerindicator.CirclePageIndicator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

import static com.blackshift.verbis.ui.activity.WordListViewPagerActivity.WordListViewPagerState.CONNECTION_ERROR;
import static com.blackshift.verbis.ui.activity.WordListViewPagerActivity.WordListViewPagerState.ERROR;
import static com.blackshift.verbis.ui.activity.WordListViewPagerActivity.WordListViewPagerState.FOUND_WORD_LIST;
import static com.blackshift.verbis.ui.activity.WordListViewPagerActivity.WordListViewPagerState.LOADING;
import static com.blackshift.verbis.ui.activity.WordListViewPagerActivity.WordListViewPagerState.NO_WORD_LIST;

public class WordListViewPagerActivity extends VerbisActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private WordListViewPagerAdapter mWordListViewPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    @Bind(R.id.container)
    WordListViewPager mViewPager = null;

    WordListManager wordListManager;

    @Bind(R.id.viewpager_indicator)
    CirclePageIndicator pageIndicator;

    @Bind(R.id.add_list_layout)
    View overlayToolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.new_wordlist_title)
    EditText newWordlistTitle;
    @Bind(R.id.appbar)
    AppBarLayout appBarLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView_alternate)
    ViewGroup alternateLayout;

    SupportAnimator overLayoutToolbarAnimator,reverseOverlayToolbarAnimator;

    boolean moveToPos =true;
    static final String ARG_WORDLIST_ID = "wordlist_id";
    String wordlistId = null;

    public static Intent createIntent(Context context, String wordlistId){
        Intent intent = createIntent(context);
        intent.putExtra(ARG_WORDLIST_ID,wordlistId);
        return intent;
    }

    public static Intent createIntent(Context context){
        Intent intent =new Intent(context,WordListViewPagerActivity.class);
        return intent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list_view_pager);
        wordListManager = new WordListManager(this);
        ButterKnife.bind(this);
        handleFabStatus(FabStatus.ADD);

        if(getIntent()!=null&&getIntent().hasExtra(ARG_WORDLIST_ID)&&moveToPos) {
            wordlistId = getIntent().getStringExtra(ARG_WORDLIST_ID);
            moveToPos =false;
        }

        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWordListViewPagerAdapter = new WordListViewPagerAdapter(getSupportFragmentManager(),this);
        overlayToolbar.setVisibility(View.INVISIBLE);
        wordListManager.getAllWordLists(new WordListArrayListener() {
            @Override
            public void onSuccess(@Nullable List<WordList> wordList) {
                if(wordList!=null) {
                    mWordListViewPagerAdapter.set(wordList);
                    if (wordList.size() < 1) {
                        setState(NO_WORD_LIST);
                    }else {
                        setState(FOUND_WORD_LIST);
                        //Move to the position of the wordlist only once
                        gotoWordList(wordlistId);
                    }
                }
            }


            @Override
            public void onFailure(DatabaseError firebaseError) {
                firebaseError.toException().printStackTrace();
                switch (firebaseError.getCode()){
                    case DatabaseError.NETWORK_ERROR:
                        setState(CONNECTION_ERROR);
                        break;
                    default:
                        setState(ERROR);
                }
            }
        });

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mWordListViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                callTracker(getString(R.string.wordlist_viewpager));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setClipToPadding(false);
        pageIndicator.setViewPager(mViewPager);
        pageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Get Current Selection
                wordlistId = mWordListViewPagerAdapter.get(position).getId();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setPageMargin(56);
        newWordlistTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO: Better validation so that user can't enter special chars.
                if(newWordlistTitle.isEnabled())
                if(s.toString().isEmpty())
                    handleFabStatus(FabStatus.CANCEL);
                else
                    handleFabStatus(FabStatus.ACCEPT);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



    void gotoWordList(String id){
        for(int i=0;i<mWordListViewPagerAdapter.getCount();++i){
            WordList w = mWordListViewPagerAdapter.getAll().get(i);
            if(id!=null){
                if(id.equals(w.getId()))
                    mViewPager.setCurrentItem(i,true);
            }
        }
    }


    @OnClick(R.id.fab)
    void onFabClick(){
        if(fab.isClickable()&&fab.isEnabled()) {
            WordListManager wordListManager = new WordListManager(this);
            switch ((String) fab.getTag()) {
                case FabStatus.ADD:
                    //Because Next state should be Cancel
                    handleFabStatus(FabStatus.CANCEL);

                    //Show Edit Text
                    newWordlistTitle.requestFocus();
                    showAddWordlistLayout(true);
                    break;
                case FabStatus.ACCEPT:
                    newWordlistTitle.setEnabled(false);
                    handleFabStatus(FabStatus.ADD);
                    showAddWordlistLayout(false);
                    fab.setEnabled(true);
                    wordListManager.createWordList(newWordlistTitle.getText().toString(), null);
                    newWordlistTitle.setText(null);
                    break;
                //Currently not in use.
                case FabStatus.CANCEL:
                    //Because Next state should be ADD
                    handleFabStatus(FabStatus.ADD);
                    newWordlistTitle.setText(null);
                    showAddWordlistLayout(false);
                    break;
                default:
                    handleFabStatus(FabStatus.ADD);
            }
        }
    }

    void handleFabStatus(@FabStatus String status){
        MaterialIcons icons = MaterialIcons.md_add;
        switch (status){
            case FabStatus.ADD:
                newWordlistTitle.setEnabled(false);
                fab.animate().rotation(0).start();
                break;
            case FabStatus.ACCEPT:
                newWordlistTitle.setEnabled(true);
                fab.animate().rotation(360).start();
                icons = MaterialIcons.md_check;
                break;
            //Currently not in use.
            case FabStatus.CANCEL:
                newWordlistTitle.setEnabled(true);
                fab.animate().rotation(225).start();
                break;
        }

        fab.setImageDrawable(new IconDrawable(this,icons).colorRes(android.R.color.white));
        fab.setTag(status);
    }

    void showAddWordlistLayout(boolean visibility){
        final View fab = findViewById(R.id.fab);
        // get the center for the clipping circle
        int cx = (fab.getLeft() + fab.getRight()) / 2;
        int cy = (fab.getTop() + fab.getBottom()) / 2;

        // get the final radius for the clipping circle
        int dx = Math.max(cx, overlayToolbar.getWidth() - cx);
        int dy = Math.max(cy, overlayToolbar.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        overLayoutToolbarAnimator =
                ViewAnimationUtils.createCircularReveal(overlayToolbar, cx, cy, 0, finalRadius);
        overLayoutToolbarAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        overLayoutToolbarAnimator.setDuration(500);
        reverseOverlayToolbarAnimator = overLayoutToolbarAnimator.reverse();

        if(visibility) {
            overLayoutToolbarAnimator.addListener(new SupportAnimator.AnimatorListener() {
                @Override
                public void onAnimationStart() {
                    overlayToolbar.setVisibility(View.VISIBLE);
                    ((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout)).setTitleEnabled(false);
                    fab.setClickable(false);
                }

                @Override
                public void onAnimationEnd() {
                    fab.setClickable(true);
                }

                @Override
                public void onAnimationCancel() {

                }

                @Override
                public void onAnimationRepeat() {

                }
            });
            overLayoutToolbarAnimator.start();
        }else {
            reverseOverlayToolbarAnimator.addListener(new SupportAnimator.AnimatorListener() {
                @Override
                public void onAnimationStart() {
                    fab.setClickable(false);
                }

                @Override
                public void onAnimationEnd() {
                    overlayToolbar.setVisibility(View.INVISIBLE);
                    ((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout)).setTitleEnabled(true);
                    fab.setClickable(true);
                }

                @Override
                public void onAnimationCancel() {

                }

                @Override
                public void onAnimationRepeat() {

                }
            });
            reverseOverlayToolbarAnimator.start();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({FabStatus.ADD,FabStatus.ACCEPT,FabStatus.CANCEL})
    @interface FabStatus{
        String ADD="add",ACCEPT = "accept",CANCEL = "cancel";
    }


    void setState(@WordListViewPagerState int state){
        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.layout_image, null);
        ImageView img =(ImageView) v.findViewById(R.id.imageView);

        @DrawableRes
        int placeholder = R.drawable.nowordlist; // TODO: Change this to Search Prompt

        switch (state){
            case LOADING:
                // Loading View
                img.setVisibility(View.GONE);
                v.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                break;
            case CONNECTION_ERROR:
                placeholder = R.drawable.networkerror;
                break;
            case NO_WORD_LIST:
                placeholder = R.drawable.nowordlist;
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleFabStatus(FabStatus.ADD);
                        onFabClick();
                    }
                });
                break;
            case FOUND_WORD_LIST:
                hideAlternateLayout();
                break;
            case ERROR:
                placeholder = R.drawable.error;
                break;
        }

        if(state==LOADING){
            img.setVisibility(View.GONE);
            v.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        }else {
            img.setVisibility(View.VISIBLE);
            v.findViewById(R.id.progressBar).setVisibility(View.GONE);
        }

        pageIndicator.setVisibility(state!=FOUND_WORD_LIST?View.INVISIBLE:View.VISIBLE);

        if(state!= FOUND_WORD_LIST &&state!=LOADING){
            Glide.with(App.getContext())
                    .load(placeholder)
                    .into(img);
            showAlternateLayout(v);
        }
    }

    void showAlternateLayout(View view){
        if(alternateLayout.getChildCount()>0)
            alternateLayout.removeAllViews();

        mViewPager.setVisibility(View.GONE);
        pageIndicator.setVisibility(View.GONE);
        alternateLayout.setVisibility(View.VISIBLE);
    }

    void hideAlternateLayout(){
        if(alternateLayout.getChildCount()>0)
            alternateLayout.removeAllViews();

        mViewPager.setVisibility(View.VISIBLE);
        pageIndicator.setVisibility(View.VISIBLE);
        alternateLayout.setVisibility(View.GONE);
    }


    @IntDef({CONNECTION_ERROR,
            FOUND_WORD_LIST,
            LOADING,
            NO_WORD_LIST,
            ERROR,})
    @Retention(RetentionPolicy.SOURCE)
    @interface WordListViewPagerState {
        int LOADING = 0,CONNECTION_ERROR = 2, NO_WORD_LIST = 3, FOUND_WORD_LIST = 4,ERROR = 5;
    }

    @Override
    protected void onResume() {
        super.onResume();
        callTracker(getString(R.string.title_activity_view_pager));
    }

}
