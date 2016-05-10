package com.blackshift.verbis.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackshift.verbis.R;
import com.blackshift.verbis.ui.viewholders.SuggestedWordsViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.prashantsolanki3.snaplibrary.snap.adapter.SnapAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapViewHolder;
import io.github.prashantsolanki3.snaplibrary.snap.layout.wrapper.SnapLayoutWrapper;
import io.github.prashantsolanki3.snaplibrary.snap.listeners.touch.SnapOnItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuggestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuggestionFragment extends VerbisFragment {

    @Bind(R.id.wordlist_title_recycler)
    RecyclerView recyclerView;
    SnapAdapter<String> wordAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public SuggestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SuggestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SuggestionFragment newInstance(String param1, String param2) {
        SuggestionFragment fragment = new SuggestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_word_list, container, false);
        ButterKnife.bind(this, view);
        populateRecycler(view);
        return view;
    }
    void populateRecycler(View view){
        recyclerView.setHasFixedSize(true);
        final SnapLayoutWrapper snapLayoutWrapper = new SnapLayoutWrapper(String.class, SuggestedWordsViewHolder.class, R.layout.suggested_words, 1);
        wordAdapter = new SnapAdapter<>(getContext(),snapLayoutWrapper, recyclerView, (ViewGroup) view.findViewById(R.id.recyclerView_alternate));

        wordAdapter.setOnItemClickListener(new SnapOnItemClickListener() {
            @Override
            public void onItemClick(SnapViewHolder snapViewHolder, View view, int i) {

            }

            @Override
            public void onItemLongPress(SnapViewHolder snapViewHolder, View view, int i) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(wordAdapter);

    }

}
