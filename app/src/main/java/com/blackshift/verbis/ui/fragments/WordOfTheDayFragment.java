package com.blackshift.verbis.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackshift.verbis.R;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordOfTheDayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordOfTheDayFragment extends VerbisFragment {

    private static final String ARG_PARAM1 = "word";
    private static final String ARG_PARAM2 = "meaning";
    private static final String ARG_PARAM3 = "pronunciation";

    private String mWord;
    private String mMeaning;
    private String mPronunciation;

    @Bind(R.id.word_of_the_day_text)
    TextView wordOfTheDayText;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.word)
    TextView word;
    @Bind(R.id.meaning)
    TextView meaning;
    @Bind(R.id.pronunciation)
    TextView pronunciation;
    @Bind(R.id.speaker)
    ImageView speaker;

    public WordOfTheDayFragment() {
        // Required empty public constructor
    }

    public static WordOfTheDayFragment newInstance(String word, String meaning, String pronunciation) {
        WordOfTheDayFragment fragment = new WordOfTheDayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, word);
        args.putString(ARG_PARAM2, meaning);
        args.putString(ARG_PARAM3, pronunciation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWord = getArguments().getString(ARG_PARAM1);
            mMeaning = getArguments().getString(ARG_PARAM2);
            mPronunciation = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_word_of_the_day, container, false);
        //To initialize the views
        ButterKnife.bind(this, view);

        populatingTextView();

        return view;
    }

    private void populatingTextView() {
        Date d = new Date();
        //TODO: Didi don't do this. :p Har jaga date format same nai hota. Use the method. isme kabi b error aa skta h.
        date.setText(d.toString().split("I")[0]);
        word.setText(mWord);
        meaning.setText(mMeaning);
        pronunciation.setText(mPronunciation);
        //TODO: Set Speaker icon using Iconify
    }



}
