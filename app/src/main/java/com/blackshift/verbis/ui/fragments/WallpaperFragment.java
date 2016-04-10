package com.blackshift.verbis.ui.fragments;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
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

import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.WallpaperConfig;
import com.blackshift.verbis.ui.dialogs.WallpaperTextConfigDialog;
import com.blackshift.verbis.ui.widgets.FontTextView;
import com.blackshift.verbis.utils.PreferenceKeys;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;
import com.prashantsolanki.secureprefmanager.SecurePrefManager;

import java.util.List;

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
    FontTextView partOfSpeech;
    @Bind(R.id.wallpaper_word)
    FontTextView word;
    @Bind(R.id.wallpaper_meaning)
    FontTextView meaning;
    @Bind(R.id.wallpaper_background)
    ImageView background;
    @Bind(R.id.wallpaper_text_overlay)
    RelativeLayout textOverlay;
    @Bind(R.id.wallpaper_image_layout)
    FrameLayout wallpaper;
    @Bind(R.id.fabtoolbar)
    FABToolbarLayout bottomBar;
    @Bind(R.id.fabtoolbar_fab)
    FloatingActionButton fab;

    WallpaperConfig config;

    View view;
    View wallpaperLayout;
    @Bind({R.id.fabtoolbar_toolbar_ic_source,
            R.id.fabtoolbar_toolbar_ic_background,
            R.id.fabtoolbar_toolbar_ic_word,
            R.id.fabtoolbar_toolbar_ic_meaning,
            R.id.fabtoolbar_toolbar_ic_part_of_speech})
    List<ImageView> bottomIcons;
    final int SOURCE=0,BACKGROUND=1,WORD=2,MEANING=3,PART_OF_SPEECH=4;
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
        config = new WallpaperConfig();
    }

    void defaultWallpaperConfig(){
        config.setAlignment(WallpaperConfig.Alignment.CENTER);
        config.setBackgroundType(WallpaperConfig.BackgroundType.COLOR);
        config.setFontRelative(1);
        config.setBackground("#00a0afff");
        config.setId(layoutId);
        config.setMarginTopPercent(150);

        WallpaperConfig.TextConfig word = new WallpaperConfig
                .TextConfig()
                .setColor("#ff3322ff")
                .setSize(42);

        config.setMeaning(word);
        config.setWord(word);
        config.setPartOfSpeech(word);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_wordy,container,false);
        wallpaperLayout = inflater.inflate(layoutId,(ViewGroup)view, true);
        ButterKnife.bind(this, wallpaperLayout);
        wallpaperLayout.findViewById(R.id.fabtoolbar_container).bringToFront();
        wallpaperLayout.findViewById(R.id.fabtoolbar_toolbar).bringToFront();
        bottomBar.bringToFront();
        fab.bringToFront();
        defaultWallpaperConfig();
        setIcons();
        setupDrag();
        setListeners();
        return view;
    }

    void setIcons(){
        fab.setImageDrawable(new IconDrawable(getContext(), MaterialIcons.md_mode_edit)
                .colorRes(android.R.color.white).actionBarSize());
        bottomIcons.get(BACKGROUND).setImageDrawable(new IconDrawable(getContext(), MaterialIcons.md_wallpaper)
                .colorRes(android.R.color.white).actionBarSize());
        bottomIcons.get(WORD).setImageDrawable(new IconDrawable(getContext(), MaterialIcons.md_subtitles)
                .colorRes(android.R.color.white).actionBarSize());
        bottomIcons.get(MEANING).setImageDrawable(new IconDrawable(getContext(), MaterialIcons.md_flip_to_back)
                .colorRes(android.R.color.white).actionBarSize());
        bottomIcons.get(SOURCE).setImageDrawable(new IconDrawable(getContext(), MaterialIcons.md_input)
                .colorRes(android.R.color.white).actionBarSize());
        bottomIcons.get(PART_OF_SPEECH).setImageDrawable(new IconDrawable(getContext(), MaterialIcons.md_flip_to_back)
                .colorRes(android.R.color.white).actionBarSize());
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
                if (bottomBar.isFab())
                    bottomBar.show();
                else
                    bottomBar.hide();
            }
        });
    }

    @OnClick(R.id.wallpaper_background)
    void onClickBackground(){
        if(bottomBar.isFab())
            bottomBar.show();
        else
            bottomBar.hide();
    }


    @OnClick({R.id.fabtoolbar_toolbar_ic_background,
            R.id.fabtoolbar_toolbar_ic_word,
            R.id.fabtoolbar_toolbar_ic_part_of_speech,
            R.id.fabtoolbar_toolbar_ic_source,
            R.id.fabtoolbar_toolbar_ic_meaning})
    void onClickBottomBar(View view){
        int id = view.getId();
        bottomBar.hide();
        WallpaperTextConfigDialog dialog = new WallpaperTextConfigDialog().setWallpaperConfig(config);
        switch (id){
            case R.id.fabtoolbar_toolbar_ic_background:
                new ShadeMelangeDialog(getContext()).setMelangeCancelable(true)
                        .columns(2)
                        .setShadeType(ShadeTypeEnum.MATERIAL_SHADES)
                        .setSelectionMode(SelectionModeEnum.SINGLE_SELECTION_MODE)
                        .setPositiveButton("Select", new OnDialogButtonClickListener() {
                            @Override
                            public void onButtonClicked(ShadeMelangeDialog shadeMelangeDialog) {
                                setBackgroundColor(shadeMelangeDialog.getSelectedShade().getShadeCode());
                            }
                        }).showMelange();
                break;
            case R.id.fabtoolbar_toolbar_ic_word:
                dialog.setOnWallpaperTextConfigDialogListener(new WallpaperTextConfigDialog.OnWallpaperTextConfigDialogListener() {
                    @Override
                    public void onAccept(WallpaperConfig.TextConfig textConfig) {
                             word.setTextConfig(textConfig);
                    }

                    @Override
                    public void onDismissed() {

                    }
                });
                dialog.setLayoutComponent(1).show((getActivity()).getSupportFragmentManager(),"word");
                break;
            case R.id.fabtoolbar_toolbar_ic_part_of_speech:
                dialog.setOnWallpaperTextConfigDialogListener(new WallpaperTextConfigDialog.OnWallpaperTextConfigDialogListener() {
                    @Override
                    public void onAccept(WallpaperConfig.TextConfig textConfig) {
                        partOfSpeech.setTextConfig(textConfig);
                    }

                    @Override
                    public void onDismissed() {

                    }
                });
                dialog.setLayoutComponent(3).show((getActivity()).getSupportFragmentManager(),"part_of_speech");
                break;
            case R.id.fabtoolbar_toolbar_ic_source:
                break;
            case R.id.fabtoolbar_toolbar_ic_meaning:
                dialog.setOnWallpaperTextConfigDialogListener(new WallpaperTextConfigDialog.OnWallpaperTextConfigDialogListener() {
                    @Override
                    public void onAccept(WallpaperConfig.TextConfig textConfig) {
                        meaning.setTextConfig(textConfig);
                    }

                    @Override
                    public void onDismissed() {

                    }
                });
                dialog.setLayoutComponent(2).show((getActivity()).getSupportFragmentManager(),"meaning");
                break;
        }
    }

    public void setBackgroundColor(int color){
        background.setBackgroundColor(color);
        background.setImageDrawable(new ColorDrawable(color));
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
