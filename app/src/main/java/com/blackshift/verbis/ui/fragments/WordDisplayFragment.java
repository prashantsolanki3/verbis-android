package com.blackshift.verbis.ui.fragments;


import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.recyclerviewmodels.Meaning;
import com.blackshift.verbis.rest.model.recyclerviewmodels.MeaningAndExample;
import com.blackshift.verbis.rest.model.wordapimodels.WordsApiResult;
import com.blackshift.verbis.ui.viewholders.MeaningViewHolder;
import com.blackshift.verbis.ui.viewholders.PartOfSpeechViewHolder;
import com.blackshift.verbis.utils.Utils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.prashantsolanki3.snaplibrary.snap.adapter.SnapAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.layout.wrapper.SnapLayoutWrapper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@Deprecated
public class WordDisplayFragment extends Fragment implements View.OnClickListener{

    @Bind(R.id.share)
    FloatingActionButton shareFAB;
    @Bind(R.id.fab_add_to_list)
    FloatingActionButton addToListFAB;
    @Bind(R.id.fab_pronounce)
    FloatingActionButton pronounceFAB;

   // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private WordsApiResult mParam1;
    private String mParam2;
    private SnapAdapter snapMultiAdapter;

    @Bind(R.id.dictionary_recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    public WordDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WordDisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordDisplayFragment newInstance(WordsApiResult param1, String param2) {
        WordDisplayFragment fragment = new WordDisplayFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, Parcels.wrap(param1));
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = Parcels.unwrap(getArguments().getParcelable(ARG_PARAM1));
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_word_display, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        collapsingToolbar.setTitle(mParam1.getWord() + "(" + mParam1.getPronunciation().getAll() + ")");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<SnapLayoutWrapper> layoutWrappers = new ArrayList<>();
        layoutWrappers.add(new SnapLayoutWrapper(String.class, PartOfSpeechViewHolder.class, R.layout.part_of_speech_item, 1));
        layoutWrappers.add(new SnapLayoutWrapper(Meaning.class, MeaningViewHolder.class, R.layout.meaning_item, 2));

        snapMultiAdapter = new SnapAdapter(getContext(), layoutWrappers, recyclerView);

        Set<String> partOfSpeech = Utils.getAllPartOfSpeech(mParam1.getResults());
        ArrayList<ArrayList<MeaningAndExample>> meaningAndExampleList =
                Utils.getResultSortedByPartOfSpeech(mParam1.getResults(), partOfSpeech);

        Log.d("size", meaningAndExampleList.size() + "");
        for (ArrayList<MeaningAndExample> meaningAndExamples : meaningAndExampleList){
            for (MeaningAndExample meaningAndExample : meaningAndExamples){
                Log.d("Meaning", meaningAndExample.getMeaning());
                for (String s : meaningAndExample.getExample()){
                    Log.d("Example", s);
                }
            }
        }

        int i = 0;
        for (String string : partOfSpeech){
            snapMultiAdapter.add(string);
            for (MeaningAndExample meaningAndExample : meaningAndExampleList.get(i)){
                Log.d("meaning", meaningAndExample.getMeaning());
                for (String st: meaningAndExample.getExample()){
                    Log.d("example", st);
                }
                snapMultiAdapter.add(meaningAndExample);
            }
            i++;
        }
        recyclerView.setAdapter(snapMultiAdapter);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
/*
    @OnClick(R.id.fab_add_to_list)
    public void addWordToTheList(){

    }
    */
}
