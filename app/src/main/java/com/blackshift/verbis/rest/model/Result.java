
package com.blackshift.verbis.rest.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Result {

    @SerializedName("datasets")
    private List<String> datasets = new ArrayList<String>();
    @SerializedName("headword")
    private String headword;
    @SerializedName("homnum")
    private long homnum;
    @SerializedName("id")
    private String id;
    @SerializedName("part_of_speech")
    private String partOfSpeech;
    @SerializedName("senses")
    private List<Sense> senses = new ArrayList<Sense>();
    @SerializedName("url")
    private String url;
    @SerializedName("pronunciations")
    private List<Pronunciation> pronunciations = new ArrayList<>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Result() {
    }

    /**
     * 
     * @param id
     * @param pronunciations
     * @param datasets
     * @param headword
     * @param url
     * @param senses
     * @param partOfSpeech
     * @param homnum
     */
    public Result(List<String> datasets, String headword, long homnum, String id, String partOfSpeech, List<Sense> senses, String url, List<Pronunciation> pronunciations) {
        this.datasets = datasets;
        this.headword = headword;
        this.homnum = homnum;
        this.id = id;
        this.partOfSpeech = partOfSpeech;
        this.senses = senses;
        this.url = url;
        this.pronunciations = pronunciations;
    }

    /**
     * 
     * @return
     *     The datasets
     */
    public List<String> getDatasets() {
        return datasets;
    }

    /**
     * 
     * @param datasets
     *     The datasets
     */
    public void setDatasets(List<String> datasets) {
        this.datasets = datasets;
    }

    /**
     * 
     * @return
     *     The headword
     */
    public String getHeadword() {
        return headword;
    }

    /**
     * 
     * @param headword
     *     The headword
     */
    public void setHeadword(String headword) {
        this.headword = headword;
    }

    /**
     * 
     * @return
     *     The homnum
     */
    public long getHomnum() {
        return homnum;
    }

    /**
     * 
     * @param homnum
     *     The homnum
     */
    public void setHomnum(long homnum) {
        this.homnum = homnum;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The partOfSpeech
     */
    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    /**
     * 
     * @param partOfSpeech
     *     The part_of_speech
     */
    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    /**
     * 
     * @return
     *     The senses
     */
    public List<Sense> getSenses() {
        return senses;
    }

    /**
     * 
     * @param senses
     *     The senses
     */
    public void setSenses(List<Sense> senses) {
        this.senses = senses;
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

    /**
     * 
     * @return
     *     The pronunciations
     */
    public List<Pronunciation> getPronunciations() {
        return pronunciations;
    }

    /**
     * 
     * @param pronunciations
     *     The pronunciations
     */
    public void setPronunciations(List<Pronunciation> pronunciations) {
        this.pronunciations = pronunciations;
    }

}
