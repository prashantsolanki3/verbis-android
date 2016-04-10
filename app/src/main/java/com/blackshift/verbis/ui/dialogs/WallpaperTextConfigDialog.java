package com.blackshift.verbis.ui.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blackshift.verbis.R;

/**
 * Package com.blackshift.verbis.ui.dialogs
 * <p>
 * Created by Prashant on 4/8/2016.
 * <p>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
public class WallpaperTextConfigDialog extends DialogFragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_wallpaper_text_config,container,false);
        //TODO: Some shit!!!!!!
        return view;
    }
}
