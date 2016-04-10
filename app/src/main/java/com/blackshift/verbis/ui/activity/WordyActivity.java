package com.blackshift.verbis.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.blackshift.verbis.R;
import com.blackshift.verbis.ui.fragments.WallpaperFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WordyActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.wallpaper_view_pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordy);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        viewPager.setAdapter(new WordyFragmentAdapter(getSupportFragmentManager()));
        if(getSupportActionBar()!=null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    class WordyFragmentAdapter extends FragmentStatePagerAdapter{
        public WordyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:return WallpaperFragment.newInstance(R.layout.wallpaper_layout_1);
                case 1:return WallpaperFragment.newInstance(R.layout.wallpaper_layout_2);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
