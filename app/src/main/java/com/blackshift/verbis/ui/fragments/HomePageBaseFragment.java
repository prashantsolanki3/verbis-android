package com.blackshift.verbis.ui.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackshift.verbis.R;
import com.blackshift.verbis.adapters.HomePageBaseAdapter;
import com.blackshift.verbis.ui.activity.HomePageActivity;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePageBaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageBaseFragment extends VerbisFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public HomePageBaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePageBaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePageBaseFragment newInstance(String param1, String param2) {
        HomePageBaseFragment fragment = new HomePageBaseFragment();
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

    @Bind(R.id.home_page_base_pager)
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page_base, container, false);
        ButterKnife.bind(this,view);
        viewPager.setAdapter(new HomePageBaseAdapter(getActivity().getSupportFragmentManager()));

        ((HomePageActivity)getActivity()).getTabLayout().setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.
                TabLayoutOnPageChangeListener(((HomePageActivity) getActivity())
                .getTabLayout()));

        return view;
    }

}