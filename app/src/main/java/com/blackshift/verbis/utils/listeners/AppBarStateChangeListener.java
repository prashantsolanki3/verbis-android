package com.blackshift.verbis.utils.listeners;

import android.support.annotation.StringDef;
import android.support.design.widget.AppBarLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {
 
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({State.COLLAPSED,State.EXPANDED,State.IDLE})
    public @interface State {
        String EXPANDED = "expanded",COLLAPSED = "collapsed",IDLE = "idle";
    }

    @State
    private String mCurrentState = State.IDLE;
 
    @Override 
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (mCurrentState.equals(State.EXPANDED)) {
                onStateChanged(appBarLayout, State.EXPANDED);
            } 
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState.equals(State.COLLAPSED)) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            } 
            mCurrentState = State.COLLAPSED;
        } else { 
            if (mCurrentState.equals(State.IDLE)) {
                onStateChanged(appBarLayout, State.IDLE);
            } 
            mCurrentState = State.IDLE;
        } 
    } 
 
    public abstract void onStateChanged(AppBarLayout appBarLayout, @State String state);
} 