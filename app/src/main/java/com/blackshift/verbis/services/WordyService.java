package com.blackshift.verbis.services;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;

import com.blackshift.verbis.rest.model.wordy.WallpaperConfig;
import com.blackshift.verbis.ui.widgets.WallpaperSynthesizer;
import com.blackshift.verbis.utils.StorageManager;
import com.google.gson.Gson;
import com.prashantsolanki.synthesize.lib.Synthesize;

import org.parceler.Parcels;

import java.io.File;

import io.github.prashantsolanki3.utiloid.Utiloid;

public class WordyService extends Service {

    WallpaperConfig config = null;

    public WordyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {

        config = Parcels.unwrap(intent.getParcelableExtra("wallpaper_config"));

        WallpaperSynthesizer synthesizer = new WallpaperSynthesizer(getApplicationContext());
        synthesizer.setWallpaperConfig(config);
        synthesizer.setParams((int)Utiloid.DISPLAY_UTILS.getScreenWidthPixels(),(int)Utiloid.DISPLAY_UTILS.getScreenHeightPixels());
        Log.e("Config",new Gson().toJson(config));
        synthesizer.setOutputPath(new File(StorageManager.ROOT_VERBIS));
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
                stopSelfResult(startId);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                stopSelfResult(startId);
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }


}
