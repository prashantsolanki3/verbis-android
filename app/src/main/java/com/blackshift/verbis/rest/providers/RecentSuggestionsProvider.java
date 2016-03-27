package com.blackshift.verbis.rest.providers;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by Devika on 19-03-2016.
 */
public class RecentSuggestionsProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY =
            RecentSuggestionsProvider.class.getName();

    public static final int MODE = DATABASE_MODE_QUERIES;

    public RecentSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
