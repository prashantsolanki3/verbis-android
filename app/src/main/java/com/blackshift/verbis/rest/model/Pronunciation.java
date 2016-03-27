
package com.blackshift.verbis.rest.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Pronunciation {

    @SerializedName("audio")
    private List<Audio> audio = new ArrayList<Audio>();
    @SerializedName("ipa")
    private String ipa;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Pronunciation() {
    }

    /**
     * 
     * @param ipa
     * @param audio
     */
    public Pronunciation(List<Audio> audio, String ipa) {
        this.audio = audio;
        this.ipa = ipa;
    }

    /**
     * 
     * @return
     *     The audio
     */
    public List<Audio> getAudio() {
        return audio;
    }

    /**
     * 
     * @param audio
     *     The audio
     */
    public void setAudio(List<Audio> audio) {
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
