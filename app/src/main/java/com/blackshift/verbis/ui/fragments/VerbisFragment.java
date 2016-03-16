package com.blackshift.verbis.ui.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackshift.verbis.BuildConfig;


/**
 * Created by Prashant on 5/10/2015.
 */
public abstract class VerbisFragment extends Fragment {

    private boolean debugMode = BuildConfig.DEBUG;

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(debugMode)
        Log.i(getClass().getSimpleName(), "onCreate");
        setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(debugMode)
        Log.i(getClass().getSimpleName(), "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(debugMode)
        Log.i(getClass().getSimpleName(), "onActivityCreated");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(debugMode)
        Log.i(getClass().getSimpleName(), "onViewCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        if(debugMode)
        Log.i(getClass().getSimpleName(), "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(debugMode)
        Log.i(getClass().getSimpleName(), "onResume");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(debugMode)
        Log.i(getClass().getSimpleName(), "onConfigurationChanged");
    }

    @Override
    public void onPause() {
        super.onPause();
        if(debugMode)
        Log.i(getClass().getSimpleName(), "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(debugMode)
        Log.i(getClass().getSimpleName(), "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(debugMode)
        Log.i(getClass().getSimpleName(), "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(debugMode)
        Log.i(getClass().getSimpleName(), "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(debugMode)
        Log.i(getClass().getSimpleName(), "onDetach");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(debugMode)
        Log.i(getClass().getSimpleName(), "onViewStateRestored");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(debugMode)
        Log.i(getClass().getSimpleName(), "onSaveInstanceState");
    }
}
