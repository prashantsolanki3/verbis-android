
package com.blackshift.verbis.rest.model;

import com.google.gson.annotations.SerializedName;

public class Variant {

    @SerializedName("link_word")
    private String linkWord;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Variant() {
    }

    /**
     * 
     * @param linkWord
     */
    public Variant(String linkWord) {
        this.linkWord = linkWord;
    }

    /**
     * 
     * @return
     *     The linkWord
     */
    public String getLinkWord() {
        return linkWord;
    }

    /**
     * 
     * @param linkWord
     *     The link_word
     */
    public void setLinkWord(String linkWord) {
        this.linkWord = linkWord;
    }

}
