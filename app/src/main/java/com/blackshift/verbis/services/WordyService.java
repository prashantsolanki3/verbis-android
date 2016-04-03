package com.blackshift.verbis.services;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.IBinder;
import android.support.annotation.LayoutRes;

import com.blackshift.verbis.R;
import com.blackshift.verbis.ui.widgets.WallpaperSynthesizer;
import com.blackshift.verbis.utils.PreferenceKeys;
import com.prashantsolanki.secureprefmanager.SecurePrefManager;
import com.prashantsolanki.synthesize.lib.Synthesize;

import java.io.File;

import io.github.prashantsolanki3.utiloid.Utiloid;

public class WordyService extends Service {
    public WordyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        @LayoutRes
        int layoutId = intent.getIntExtra("layoutId",R.layout.wallpaper_layout_1);

        WallpaperSynthesizer synthesizer = new WallpaperSynthesizer(getApplicationContext());
        synthesizer.setLayout(layoutId);

        synthesizer.setParams((int) Utiloid.DISPLAY_UTILS.getScreenWidthPixels(),(int)Utiloid.DISPLAY_UTILS.getScreenHeightPixels());
        synthesizer.setMeaningFont(Typeface.MONOSPACE);
        synthesizer.setPartOfSpeechFont(Typeface.SERIF);
        synthesizer.setWordFont(Typeface.SANS_SERIF);
        synthesizer.setTextOverlayTopMargin(SecurePrefManager.with(getApplicationContext()).get(PreferenceKeys.WALLPAPER_TEXT_OVERLAY_TOP_MARGIN+layoutId).defaultValue(16).go());
        synthesizer.saveImage(new Synthesize.OnSaveListener() {
            @Override
            public void onSuccess(Bitmap bitmap, File file) {
               Bitmap bitmap1 = bitmap.copy(Bitmap.Config.ARGB_8888, false);
                WallpaperManager myWallpaperManager
                        = WallpaperManager.getInstance(getApplicationContext());
                try {
                    myWallpaperManager.setBitmap(bitmap1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Exception e) {

            }
        });
        return super.onStartCommand(intent, flags, startId);
    }


}
