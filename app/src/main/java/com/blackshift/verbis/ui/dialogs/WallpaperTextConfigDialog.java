package com.blackshift.verbis.ui.dialogs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.blackshift.verbis.R;
import com.blackshift.verbis.adapters.ToolbarSpinnerAdapter;
import com.blackshift.verbis.rest.model.WallpaperConfig;
import com.blackshift.verbis.ui.widgets.FontTextView;
import com.blackshift.verbis.utils.manager.StorageManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import awe.devikamehra.shademelange.Enum.SelectionModeEnum;
import awe.devikamehra.shademelange.Enum.ShadeTypeEnum;
import awe.devikamehra.shademelange.Interface.OnDialogButtonClickListener;
import awe.devikamehra.shademelange.ShadeMelangeDialog;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

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

    @Bind(R.id.font_spinner)
    Spinner spinner;
    @Bind(R.id.preview_tv)
    FontTextView preview;
    List<File> allFonts;
    @Bind(R.id.text_color_picker)
    Button colorPicker;
    WallpaperConfig.TextConfig textConfig;
    WallpaperConfig wallpaperConfig;
    OnWallpaperTextConfigDialogListener listener = null;
    int layoutComponent;

    public WallpaperTextConfigDialog setLayoutComponent(int layoutComponent) {
        this.layoutComponent = layoutComponent;
        return this;
    }

    public WallpaperTextConfigDialog setWallpaperConfig(WallpaperConfig wallpaperConfig) {
        this.wallpaperConfig = wallpaperConfig;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_wallpaper_text_config, container, false);
        ButterKnife.bind(this, view);
        List<WallpaperConfig.TextConfig> configs = new ArrayList<>();
        StorageManager storageManager = new StorageManager();
        allFonts = storageManager.getFonts();
        if (layoutComponent == 1) {
            textConfig = wallpaperConfig.getWord();
            preview.setText("Word");
        } else if (layoutComponent == 2) {
            textConfig = wallpaperConfig.getMeaning();
            preview.setText("Meaning");
        } else if (layoutComponent == 3){
            textConfig = wallpaperConfig.getPartOfSpeech();
            preview.setText("(n.)");
        }
        for(File file:allFonts){
            configs.add(new WallpaperConfig.TextConfig().setColor("#ff332200").setSize(42).setFont(file));
        }
        preview.setBackgroundColor(Color.parseColor(wallpaperConfig.getBackground()));

        preview.setTextConfig(textConfig);
        ToolbarSpinnerAdapter adapter = new ToolbarSpinnerAdapter(getActivity(),configs);
        spinner.setAdapter(adapter);
        return view;
    }

    @OnItemSelected(R.id.font_spinner)
    void onFontSelected(int pos){
        textConfig.setFont(allFonts.get(pos));
        preview.setTextConfig(textConfig);
    }

    @OnClick(R.id.text_color_picker)
    public void onColorPicked(){
        new ShadeMelangeDialog(getActivity())
                .columns(2)
                .setShadeType(ShadeTypeEnum.MATERIAL_SHADES)
                .setSelectionMode(SelectionModeEnum.SINGLE_SELECTION_MODE)
                .setPositiveButton("Select", new OnDialogButtonClickListener() {
                    @Override
                    public void onButtonClicked(ShadeMelangeDialog shadeMelangeDialog) {
                            if(shadeMelangeDialog.getSelectedShade()!=null) {
                                textConfig.setColor(String.format("#%08X", (shadeMelangeDialog.getSelectedShade().getShadeCode())));
                            }
                            preview.setTextConfig(textConfig);
                    }
                }).showMelange();
    }

    @OnClick(R.id.text_size_increment)
    public void onTextSizeIncrement(){
        preview.setTextConfig(textConfig.setSize(textConfig.getSize()+2));
    }

    @OnClick(R.id.text_size_decrement)
    public void onTextSizeDecrement(){
        int size = textConfig.getSize();
        if(size<=12)
            preview.setTextConfig(textConfig.setSize(12));
        else
            preview.setTextConfig(textConfig.setSize(size - 2));
    }

    @OnClick(R.id.action_accept)
    public void onAccept(){
        if(listener!=null)
            listener.onAccept(textConfig);
        this.dismiss();
    }

    @OnClick(R.id.action_dismiss)
    public void onDismiss(){
        if(listener!=null)
            listener.onDismissed();
        this.dismiss();
    }

    public interface OnWallpaperTextConfigDialogListener{
        void onAccept(WallpaperConfig.TextConfig textConfig);
        void onDismissed();
    }

    public WallpaperTextConfigDialog setOnWallpaperTextConfigDialogListener(OnWallpaperTextConfigDialogListener listener) {
        this.listener = listener;
        return this;
    }
}
