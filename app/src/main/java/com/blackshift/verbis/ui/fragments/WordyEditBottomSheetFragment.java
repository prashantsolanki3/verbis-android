package com.blackshift.verbis.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.wordlist.WordList;
import com.blackshift.verbis.ui.viewholders.WordListTitleViewHolder;

import java.util.ArrayList;
import java.util.List;

import io.github.prashantsolanki3.snaplibrary.snap.adapter.SnapAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.layout.wrapper.SnapLayoutWrapper;

public class WordyEditBottomSheetFragment extends BottomSheetDialogFragment{

    View view;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wordy_edit_fragment, container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.edit_component_recycler);
        populateBottomRecycler();
        return view;
    }

    void populateBottomRecycler(){
        SnapLayoutWrapper wrapper = new SnapLayoutWrapper(WordList.class, WordListTitleViewHolder.class,R.layout.wordlist_title_item,1);
        GridLayoutManager layoutManager =new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        SnapAdapter<WordList> adapter = new SnapAdapter<>(getContext(),wrapper,recyclerView);
        recyclerView.setAdapter(adapter);
        List<WordList> list = new ArrayList<>();
        list.add(new WordList());
        list.add(new WordList());
        list.add(new WordList());
        list.add(new WordList());
        list.add(new WordList());
        list.add(new WordList());
        adapter.set(list);
        adapter.add(new WordList());
    }

}