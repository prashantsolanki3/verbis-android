package com.blackshift.verbis.ui.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.wordy.WallpaperConfig;
import com.prashantsolanki.synthesize.lib.Synthesize;

/**
 * Package com.blackshift.verbis.ui.widgets
 * <p>
 * Created by Prashant on 4/3/2016.
 * <p>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
public class WallpaperSynthesizer extends Synthesize{

    View view;
    ImageView background;
    RelativeLayout textOverlay;
    FontTextView partOfSpeech;
    FontTextView word;
    FontTextView meaning;


    public WallpaperSynthesizer(Context context) {
        super(context);
    }

    public void setWallpaperConfig(WallpaperConfig config){
        setLayout(config.getId());

        if(config.getBackgroundType()==WallpaperConfig.BackgroundType.COLOR)
        setWallpaperBackground(new ColorDrawable(Color.parseColor(config.getBackground())));

        setTextOverlayTopMargin((int)config.getMarginTopPercent());
        getMeaning().setTextConfig(config.getMeaning());
        getPartOfSpeech().setTextConfig(config.getPartOfSpeech());
        getWord().setTextConfig(config.getWord());
    }

    @Deprecated
    @Override
    public void setLayout(int layoutRes) {
        super.setLayout(layoutRes);
        view = getLayout();

        background = (ImageView) view.findViewById(R.id.wallpaper_background);
        textOverlay = (RelativeLayout) view.findViewById(R.id.wallpaper_text_overlay);
        partOfSpeech = (FontTextView) view.findViewById(R.id.wallpaper_part_of_speech);
        word = (FontTextView) view.findViewById(R.id.wallpaper_word);
        meaning =(FontTextView) view.findViewById(R.id.wallpaper_meaning);
    }

    public void setWallpaperBackground(Drawable drawable){
        background.setImageDrawable(drawable);
    }

    public void setWallpaperBackground(Bitmap bitmap){
        background.setImageBitmap(bitmap);
    }

    public void setWallpaperBackground(@DrawableRes int resId){
        background.setImageResource(resId);
    }

    public ImageView getWallpaperBackground() {
        return background;
    }

    public RelativeLayout getTextOverlay() {
        return textOverlay;
    }

    public FontTextView getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech.setText(partOfSpeech);
    }

    public FontTextView getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word.setText(word);
    }

    public FontTextView getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning.setText(meaning);
    }

    public void setTextOverlayTopMargin(int topMargin){
        FrameLayout.MarginLayoutParams layoutParams = (FrameLayout.MarginLayoutParams)textOverlay.getLayoutParams();
        layoutParams.topMargin = topMargin;
        textOverlay.setLayoutParams(layoutParams);
    }

}
