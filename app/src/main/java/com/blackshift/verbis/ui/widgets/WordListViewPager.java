package com.blackshift.verbis.ui.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Package com.blackshift.verbis.ui.widgets
 * <p/>
 * Created by Prashant on 4/13/2016.
 * <p/>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
public class WordListViewPager extends ViewPager {

    public WordListViewPager(Context context) {
        super(context);
    }

    public WordListViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return getChildCount()!=1 && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return getChildCount()!=1 && super.onTouchEvent(event);
    }

}
