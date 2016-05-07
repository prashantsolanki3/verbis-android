package com.blackshift.verbis.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;

import com.blackshift.verbis.R;
import com.blackshift.verbis.adapters.WordListViewPagerAdapter;
import com.blackshift.verbis.rest.model.wordlist.WordList;
import com.blackshift.verbis.ui.widgets.WordListViewPager;
import com.blackshift.verbis.utils.listeners.WordListArrayListener;
import com.blackshift.verbis.utils.manager.WordListManager;
import com.firebase.client.FirebaseError;
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

    SupportAnimator overLayoutToolbarAnimator,reverseOverlayToolbarAnimator;

    boolean moveToPos =true;

    public static Intent createIntent(Context context, int pos){
        Intent intent = createIntent(context);
        intent.putExtra("pos",pos);
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
        pageIndicator.setVisibility(View.INVISIBLE);
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
                    if (wordList.size() < 1)
                        pageIndicator.setVisibility(View.INVISIBLE);
                    else {
                        pageIndicator.setVisibility(View.VISIBLE);

                        //Move to the position of the wordlist only once
                        if(wordList.size()>getIntent().getIntExtra("pos",0)&&moveToPos){
                            mViewPager.setCurrentItem(getIntent().getIntExtra("pos",0));
                            moveToPos=false;
                        }
                    }
                }
            }

            @Override
            public void onFailure(FirebaseError firebaseError) {
                firebaseError.toException().printStackTrace();
            }
        });

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mWordListViewPagerAdapter);

        mViewPager.setClipToPadding(false);
        pageIndicator.setViewPager(mViewPager);
        pageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

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

    @OnClick(R.id.fab)
    void onFabClick(){
        WordListManager wordListManager = new WordListManager(this);
        switch ((String)fab.getTag()){
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
                wordListManager.createWordList(newWordlistTitle.getText().toString(),null);
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
        View fab = findViewById(R.id.fab);
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
                }

                @Override
                public void onAnimationEnd() {

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
            reverseOverlayToolbarAnimator.start();
            reverseOverlayToolbarAnimator.addListener(new SupportAnimator.AnimatorListener() {
                @Override
                public void onAnimationStart() {

                }

                @Override
                public void onAnimationEnd() {
                    overlayToolbar.setVisibility(View.INVISIBLE);
                    ((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout)).setTitleEnabled(true);
                }

                @Override
                public void onAnimationCancel() {

                }

                @Override
                public void onAnimationRepeat() {

                }
            });
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({FabStatus.ADD,FabStatus.ACCEPT,FabStatus.CANCEL})
    @interface FabStatus{
        String ADD="add",ACCEPT = "accept",CANCEL = "cancel";
    }

}
