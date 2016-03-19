
package com.blackshift.verbis.rest.model;

import com.google.gson.annotations.SerializedName;

public class Audio {

    @SerializedName("lang")
    private String lang;
    @SerializedName("type")
    private String type;
    @SerializedName("url")
    private String url;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Audio() {
    }

    /**
     * 
     * @param type
     * @param lang
     * @param url
     */
    public Audio(String lang, String type, String url) {
        this.lang = lang;
        this.type = type;
        this.url = url;
    }

    /**
     * 
     * @return
     *     The lang
     */
    public String getLang() {
        return lang;
    }

    /**
     * 
     * @param lang
     *     The lang
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
