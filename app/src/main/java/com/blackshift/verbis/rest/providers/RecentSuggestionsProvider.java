package com.blackshift.verbis.rest.providers;

import android.content.SearchRecentSuggestionsProvider;

import com.blackshift.verbis.BuildConfig;

/**
 * Created by Devika on 19-03-2016.
 */
public class RecentSuggestionsProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID +
            RecentSuggestionsProvider.class.getSimpleName();

    public static final int MODE = DATABASE_MODE_QUERIES;

    public RecentSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
