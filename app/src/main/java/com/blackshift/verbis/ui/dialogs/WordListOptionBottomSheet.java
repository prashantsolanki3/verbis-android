package com.blackshift.verbis.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blackshift.verbis.App;
import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.wordlist.WordList;
import com.blackshift.verbis.ui.activity.DictionaryActivity;
import com.blackshift.verbis.ui.viewholders.WordListBottomSheetViewHolder;
import com.blackshift.verbis.utils.listeners.ExistenceListener;
import com.blackshift.verbis.utils.listeners.WordListArrayListener;
import com.blackshift.verbis.utils.listeners.WordListener;
import com.blackshift.verbis.utils.manager.WordListManager;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import io.github.prashantsolanki3.snaplibrary.snap.adapter.SnapAdapter;
import io.github.prashantsolanki3.snaplibrary.snap.layout.wrapper.SnapLayoutWrapper;

/**
 * Created by Devika on 08-04-2016.
 */
public class WordListOptionBottomSheet extends BottomSheetDialogFragment {

    TextView textView;
    RecyclerView recyclerView;
    SnapAdapter<WordList> snapAdapter;
    WordListManager wordListManager;
    List<WordList> wordLists = new ArrayList<>();

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        final View contentView = View.inflate(getContext(), R.layout.wordlist_option_bottom_sheet, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if( behavior != null && behavior instanceof BottomSheetBehavior ) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        textView = (TextView)contentView.findViewById(R.id.header_bottom_sheet);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.wordlist_recyclerview);

        SnapLayoutWrapper wrapper = new SnapLayoutWrapper(WordList.class,
                WordListBottomSheetViewHolder.class, R.layout.wordlist_bottomsheet_item, 1);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        snapAdapter = new SnapAdapter<>(getContext(), wrapper, recyclerView);
        recyclerView.setNestedScrollingEnabled(false);

        if (getArguments() != null) {
            Log.d("word", getArguments().getString(DictionaryActivity.ARG_BOTTOMSHEET_WORD));
        }

        wordListManager = new WordListManager(getContext());
        wordListManager.getAllWordLists(new WordListArrayListener() {
            @Override
            public void onSuccess(@Nullable List<WordList> wordLists) {
                if (wordLists.size() > 0) {
                    WordListOptionBottomSheet.this.wordLists = wordLists;
                    for (WordList wordList : wordLists) {
                        Log.d("wordlist", wordList.getTitle());
                        snapAdapter.add(wordList);
                    }

                    recyclerView.setAdapter(snapAdapter);
                } else {
                    TextView textView = (TextView) contentView.findViewById(R.id.header_bottom_sheet);
                    textView.setText("You have not added any List Yet");
                }
            }

            @Override
            public void onFailure(DatabaseError firebaseError) {

            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getBaseContext(),
                        new RecyclerItemClickListener.OnItemClickListener() {

                            @Override
                            public void onItemClick(View view,final int position) {
                                wordListManager.containsWord(getArguments().getString(DictionaryActivity.ARG_BOTTOMSHEET_WORD),
                                        wordLists.get(position).getId(), new ExistenceListener() {
                                            @Override
                                            public void onSuccess(boolean exists) {
                                                if (exists)
                                                    Toast.makeText(WordListOptionBottomSheet.this.getContext(),
                                                            "Word already exists.", Toast.LENGTH_SHORT).show();
                                                else
                                                    addWordToWordList(wordLists.get(position).getId(),getArguments().getString(DictionaryActivity.ARG_BOTTOMSHEET_WORD));
                                            }

                                            @Override
                                            public void onFailure(DatabaseError firebaseError) {
                                                Toast.makeText(WordListOptionBottomSheet.this.getContext(),
                                                        "Word could not be added. Check your Internet Connection.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
        );
    }

    private void addWordToWordList(String wordlistId, String headword) {
        wordListManager.addWord(headword, headword,
                wordlistId, new WordListener() {
                    @Override
                    public void onSuccess(String firebaseReferenceString) {
                        Toast.makeText(App.getContext(),
                                "Word has been successfully added",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(DatabaseError firebaseError) {
                        Toast.makeText(App.getContext(),
                                "Word could not be added. Check your Internet Connection.",
                                Toast.LENGTH_SHORT).show();
                        Log.e("Error", firebaseError.toString());

                    }
        });
        WordListOptionBottomSheet.this.dismiss();
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            void onItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildPosition(childView));
                return true;
            }
            return false;
        }

        @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
