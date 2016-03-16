package com.blackshift.verbis;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordOfTheDayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordOfTheDayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "word";
    private static final String ARG_PARAM2 = "meaning";
    private static final String ARG_PARAM3 = "pronunciation";

    // TODO: Rename and change types of parameters
    private String mWord;
    private String mMeaning;
    private String mPronunciation;

    private TextView wordOfTheDayText;
    private TextView date;
    private TextView word;
    private TextView meaning;
    private TextView pronunciation;
    private MaterialIconView speaker;

    public WordOfTheDayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param word Parameter 1.
     * @param meaning Parameter 2.
     * @return A new instance of fragment WordOfTheDayFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        init(view);
        populatingTextView();

        return view;
    }

    private void populatingTextView() {
        Date d = new Date();
        date.setText(d.toString().split("I")[0]);
        word.setText(mWord);
        meaning.setText(mMeaning);
        pronunciation.setText(mPronunciation);
        speaker.setIcon(MaterialDrawableBuilder.IconValue.MICROPHONE);
    }

    private void init(View view) {
        wordOfTheDayText = (TextView) view.findViewById(R.id.word_of_the_day_text);
        date = (TextView) view.findViewById(R.id.date);
        word = (TextView) view.findViewById(R.id.word);
        meaning = (TextView) view.findViewById(R.id.meaning);
        pronunciation = (TextView) view.findViewById(R.id.pronunciation);
        speaker = (MaterialIconView) view.findViewById(R.id.speaker);
    }

}
