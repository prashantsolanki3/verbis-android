package com.blackshift.verbis.ui.fragments;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blackshift.verbis.R;
import com.blackshift.verbis.services.WordyService;
import com.blackshift.verbis.utils.PreferenceKeys;
import com.prashantsolanki.secureprefmanager.SecurePrefManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.prashantsolanki3.utiloid.Utiloid;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WallpaperFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WallpaperFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WallpaperFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LAYOUT_ID = "layoutId";

    @LayoutRes
    int layoutId;
    @Bind(R.id.wallpaper_part_of_speech)
    TextView partOfSpeech;
    @Bind(R.id.wallpaper_word)
    TextView word;
    @Bind(R.id.wallpaper_meaning)
    TextView meaning;
    @Bind(R.id.wallpaper_background)
    ImageView background;
    @Bind(R.id.wallpaper_text_overlay)
    RelativeLayout textOverlay;
    @Bind(R.id.wallpaper_image_layout)
    FrameLayout wallpaper;

    View view;
    FloatingActionButton fab;


    private OnFragmentInteractionListener mListener;

    public WallpaperFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WallpaperFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WallpaperFragment newInstance(@LayoutRes int layoutId) {
        WallpaperFragment fragment = new WallpaperFragment();
        Bundle args = new Bundle();
        args.putInt(LAYOUT_ID, layoutId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            layoutId = getArguments().getInt(LAYOUT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_wordy,container,false);
        View wallpaperLayout = inflater.inflate(layoutId,(ViewGroup)view, true);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WordyService.class);
                intent.putExtra("layoutId", layoutId);
                getActivity().startService(intent);
            }
        });
        ButterKnife.bind(this, wallpaperLayout);
        setTextOverlayTopMargin(SecurePrefManager.with(getActivity())
                .get(PreferenceKeys.WALLPAPER_TEXT_OVERLAY_TOP_MARGIN + layoutId)
                .defaultValue(16)
                .go());
        setWallpaperHeight();
        setupDrag();
        handleBottomSheet();
        return view;
    }

    void handleBottomSheet(){
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        bottomSheet.bringToFront();
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight((int) Utiloid.CONVERSION_UTILS.dpiToPixels(48));
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        fab.hide();
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        fab.hide();
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        fab.show();
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });
    }

    void setTextOverlayTopMargin(int topMargin){
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) textOverlay.getLayoutParams();
        params.topMargin = topMargin;
        textOverlay.setLayoutParams(params);
    }

    int getTextOverlayTopMargin(){
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) textOverlay.getLayoutParams();
        return params.topMargin;
    }

    void setWallpaperHeight(){
        //Since we only provide portrait wallpapers.
        ViewGroup.LayoutParams layoutParams =new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        wallpaper.setLayoutParams(layoutParams);
    }

    void setupDrag() {
        textOverlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(v) {
                    @Override
                    public void onDrawShadow(Canvas canvas) {
                        //To Remove the Shadow!
                    }
                };
                wallpaper.setOnDragListener(new View.OnDragListener() {

                    int textHt = textOverlay.getHeight();
                    int topMargin;

                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_LOCATION:
                                topMargin = (int) event.getY() - textOverlay.getHeight() / 3;
                                //So that it does go out of the screen
                                if (topMargin < 4)
                                    topMargin = 4;
                                setTextOverlayTopMargin(topMargin);
                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                topMargin = getTextOverlayTopMargin();
                                if (topMargin < 4)
                                    topMargin = 4;

                                int visibleHt = textOverlay.getHeight();
                                if (textHt > visibleHt) {
                                    topMargin = topMargin - (textHt - visibleHt);
                                }
                                setTextOverlayTopMargin(topMargin);
                                //TODO: Scale according to the size of image before setting the margin.
                                SecurePrefManager.with(getActivity()).set(PreferenceKeys.WALLPAPER_TEXT_OVERLAY_TOP_MARGIN+layoutId)
                                        .value(topMargin).go();

                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });

                v.startDrag(data, shadow, null, 0);
                return false;
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
    /*        throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");*/
            //TODO: Implement if needed.
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
