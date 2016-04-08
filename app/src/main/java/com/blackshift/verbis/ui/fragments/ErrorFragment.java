package com.blackshift.verbis.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blackshift.verbis.R;
import com.blackshift.verbis.utils.ErrorType;

/**
 * A simple {@link Fragment} subclass.
 */
public class ErrorFragment extends VerbisFragment {

    public static String TEXT_TYPE = "type";
    private int textType;

    public static ErrorFragment newInstance(int textType) {

        Bundle args = new Bundle();

        ErrorFragment fragment = new ErrorFragment();
        args.putInt(TEXT_TYPE, textType);
        fragment.setArguments(args);
        return fragment;
    }

    public ErrorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            textType = getArguments().getInt(TEXT_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_error, container, false);
        TextView textView = (TextView) view.findViewById(R.id.error_text);

        if (textType == ErrorType.NETWORK_CONNECTION_FAILURE.getErrorType()){
            textView.setText(R.string.error_type_0);
        }else if (textType == ErrorType.WORD_NOT_FOUND_FAILURE.getErrorType()){
            textView.setText(R.string.error_message_1);
        }
        return view;
    }

}
