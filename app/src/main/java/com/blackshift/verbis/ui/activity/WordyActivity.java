package com.blackshift.verbis.ui.activity;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
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

public class WordyActivity extends AppCompatActivity {

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
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordy);
        ButterKnife.bind(this);
        setTextOverlayTopMargin(SecurePrefManager.with(this).get(PreferenceKeys.WALLPAPER_TEXT_OVERLAY_TOP_MARGIN).defaultValue(16).go());
        setSupportActionBar(toolbar);
        setWallpaperHeight();
        setupDrag();
        if(getSupportActionBar()!=null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        ViewGroup.LayoutParams layoutParams =new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
                                if (topMargin < 0)
                                    topMargin = 0;
                                setTextOverlayTopMargin(topMargin);
                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                    topMargin = getTextOverlayTopMargin();
                                if (topMargin < 0)
                                    topMargin = 0;

                                int visibleHt = textOverlay.getHeight();
                                if(textHt>visibleHt) {
                                    topMargin = topMargin - (textHt - visibleHt);
                                }
                                setTextOverlayTopMargin(topMargin);
                                //TODO: Scale according to the size of image before setting the margin.
                                SecurePrefManager.with(getApplicationContext()).set(PreferenceKeys.WALLPAPER_TEXT_OVERLAY_TOP_MARGIN)
                                        .value(topMargin).go();
                                startService(new Intent(getApplicationContext(),WordyService.class));
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
}
