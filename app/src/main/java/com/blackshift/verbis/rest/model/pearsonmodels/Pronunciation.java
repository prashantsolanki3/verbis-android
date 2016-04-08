
package com.blackshift.verbis.rest.model.pearsonmodels;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class Pronunciation {

    @SerializedName("audio")
    ArrayList<Audio> audio = new ArrayList<Audio>();
    @SerializedName("ipa")
    String ipa;
    @Nullable
    @SerializedName("lang")
    String lang;

    @Nullable
    public String getLang() {
        return lang;
    }

    public void setLang(@Nullable String lang) {
        this.lang = lang;
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Pronunciation() {
    }

    /**
     *
     * @param audio
     * @param ipa
     */
    public Pronunciation(ArrayList<Audio> audio, String ipa) {
        this.audio = audio;
        this.ipa = ipa;
    }

    public Pronunciation(ArrayList<Audio> audio, String ipa, String lang) {
        this.audio = audio;
        this.ipa = ipa;
        this.lang = lang;
    }

    /**
     * 
     * @return
     *     The audio
     */
    public ArrayList<Audio> getAudio() {
        return audio;
    }

    /**
     *
     * @param audio
     *     The audio
     */
    public void setAudio(ArrayList<Audio> audio) {
        this.audio = audio;
    }

    /**
     * 
     * @return
     *     The ipa
     */
    public String getIpa() {
        return ipa;
    }

    /**
     * 
     * @param ipa
     *     The ipa
     */
    public void setIpa(String ipa) {
        this.ipa = ipa;
    }

}
