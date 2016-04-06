package com.blackshift.verbis.ui.fragments;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
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
import com.blackshift.verbis.utils.PreferenceKeys;
import com.bowyer.app.fabtoolbar.FabToolbar;
import com.prashantsolanki.secureprefmanager.SecurePrefManager;

import awe.devikamehra.shademelange.Enum.SelectionModeEnum;
import awe.devikamehra.shademelange.Enum.ShadeTypeEnum;
import awe.devikamehra.shademelange.Interface.OnDialogButtonClickListener;
import awe.devikamehra.shademelange.ShadeMelangeDialog;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    FabToolbar bottomBar;
    View view;
    View wallpaperLayout;
    FloatingActionButton fab;

    private OnFragmentInteractionListener mListener;

    public WallpaperFragment() {
        // Required empty public constructor
    }

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
        wallpaperLayout = inflater.inflate(layoutId,(ViewGroup)view, true);
        ButterKnife.bind(this, wallpaperLayout);
        bottomBar = (FabToolbar) wallpaperLayout.findViewById(R.id.fabtoolbar);
        bottomBar.bringToFront();
        wallpaperLayout.findViewById(R.id.fab).bringToFront();

        fab = (FloatingActionButton) wallpaperLayout.findViewById(R.id.fab);
        bottomBar.setFab(fab);

        setTextOverlayTopMargin(SecurePrefManager.with(getActivity())
                .get(PreferenceKeys.WALLPAPER_TEXT_OVERLAY_TOP_MARGIN + layoutId)
                .defaultValue(16)
                .go());

        setWallpaperHeight();
        setupDrag();
        setListeners();
        return view;
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
                v.startDrag(data, shadow, null, 0);
                return false;
            }
        });
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
                        SecurePrefManager.with(getActivity()).set(PreferenceKeys.WALLPAPER_TEXT_OVERLAY_TOP_MARGIN + layoutId)
                                .value(topMargin).go();

                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    void setListeners(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomBar.isFabExpanded())
                    bottomBar.contractFab();
                else
                    bottomBar.expandFab();
            }
        });
    }

    @OnClick(R.id.wallpaper_background)
    void setBackground(final View background){
        new ShadeMelangeDialog(getContext()).setMelangeCancelable(true)
                .setShadeType(ShadeTypeEnum.MATERIAL_SHADES)
                .setSelectionMode(SelectionModeEnum.SINGLE_SELECTION_MODE)
                .setPositiveButton("Select", new OnDialogButtonClickListener() {
                    @Override
                    public void onButtonClicked(ShadeMelangeDialog shadeMelangeDialog) {
                        background.setBackgroundColor(shadeMelangeDialog.getSelectedShade().getShadeCode());
                        ((ImageView)background).setImageDrawable(new ColorDrawable(shadeMelangeDialog.getSelectedShade().getShadeCode()));
                    }
                }).showMelange();
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
