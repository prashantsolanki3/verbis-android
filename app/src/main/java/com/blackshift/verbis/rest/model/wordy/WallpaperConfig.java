package com.blackshift.verbis.rest.model.wordy;

import android.support.annotation.IntDef;
import android.support.annotation.IntRange;

import org.parceler.Parcel;
import org.parceler.Transient;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Package com.blackshift.verbis.rest.model
 * <p>
 * Created by Prashant on 4/6/2016.
 * <p>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
@Parcel
public class WallpaperConfig {
    int id;

    float marginTopPercent;
    @IntRange(from = -2,to =2)
    int fontRelative;
    @Alignment
    int alignment;

    String background;
    @BackgroundType
    int backgroundType;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getBackgroundType() {
        return backgroundType;
    }

    public void setBackgroundType(@BackgroundType int backgroundType) {
        this.backgroundType = backgroundType;
    }

    public OnWallpaperConfigChangedListener getListener() {
        return listener;
    }

    public void setListener(OnWallpaperConfigChangedListener listener) {
        this.listener = listener;
    }

    TextConfig word,meaning,partOfSpeech;

    @Transient
    OnWallpaperConfigChangedListener listener;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({BackgroundType.COLOR})
    public @interface BackgroundType {
        int COLOR = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Alignment.LEFT,Alignment.RIGHT,Alignment.CENTER})
    public  @interface Alignment{
        int LEFT = 1,CENTER = 2,RIGHT = 3;
    }

    public WallpaperConfig() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        if(listener!=null)
            listener.onChanged(this);
    }

    public float getMarginTopPercent() {
        return marginTopPercent;
    }

    public void setMarginTopPercent(float marginTopPercent) {
        this.marginTopPercent = marginTopPercent;
        if(listener!=null)
            listener.onChanged(this);
    }

    public int getFontRelative() {
        return fontRelative;
    }

    public void setFontRelative(@IntRange(from = -2,to =2) int fontRelative) {
        this.fontRelative = fontRelative;
        if(listener!=null)
            listener.onChanged(this);
    }

    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(@Alignment int alignment) {
        this.alignment = alignment;
        if(listener!=null)
            listener.onChanged(this);
    }

    public TextConfig getWord() {
        return word;
    }

    public void setWord(TextConfig word) {
        this.word = word;
        if(listener!=null)
            listener.onChanged(this);
    }

    public TextConfig getMeaning() {
        return meaning;
    }

    public void setMeaning(TextConfig meaning) {
        this.meaning = meaning;
        if(listener!=null)
            listener.onChanged(this);
    }

    public TextConfig getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(TextConfig partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
        if(listener!=null)
            listener.onChanged(this);
    }

    public interface OnWallpaperConfigChangedListener{
        void onChanged(WallpaperConfig config);
    }
}
