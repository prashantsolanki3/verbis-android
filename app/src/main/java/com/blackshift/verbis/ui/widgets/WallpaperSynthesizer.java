package com.blackshift.verbis.ui.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blackshift.verbis.R;
import com.prashantsolanki.synthesize.lib.Synthesize;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    @Bind(R.id.wallpaper_background)
    ImageView background;
    @Bind(R.id.wallpaper_text_overlay)
    RelativeLayout textOverlay;
    @Bind(R.id.wallpaper_part_of_speech)
    TextView partOfSpeech;
    @Bind(R.id.wallpaper_word)
    TextView word;
    @Bind(R.id.wallpaper_meaning)
    TextView meaning;


    public WallpaperSynthesizer(Context context) {
        super(context);
    }

    @Override
    public void setLayout(int layoutRes) {
        super.setLayout(layoutRes);
        view = getLayout();
        ButterKnife.bind(this, view);
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

    public TextView getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech.setText(partOfSpeech);
    }

    public TextView getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word.setText(word);
    }

    public TextView getMeaning() {
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

    public void setWordFont(Typeface font){
        getWord().setTypeface(font);
    }

    public void setPartOfSpeechFont(Typeface font){
        getPartOfSpeech().setTypeface(font);
    }

    public void setMeaningFont(Typeface meaning) {
        getMeaning().setTypeface(meaning);
    }
}
