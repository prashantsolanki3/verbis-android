
package com.blackshift.verbis.rest.model.pearsonmodels;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class Result {

    @SerializedName("datasets")
    ArrayList<String> datasets = new ArrayList<String>();
    @SerializedName("headword")
    String headword;
    @SerializedName("homnum")
    long homnum;
    @SerializedName("id")
    String id;
    @SerializedName("part_of_speech")
    String partOfSpeech;
    @SerializedName("senses")
    ArrayList<Sense> senses = new ArrayList<Sense>();
    @SerializedName("url")
    String url;
    @SerializedName("pronunciations")
    ArrayList<Pronunciation> pronunciations = new ArrayList<Pronunciation>();

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
    public Result(ArrayList<String> datasets, String headword, long homnum, String id, String partOfSpeech, ArrayList<Sense> senses, String url, ArrayList<Pronunciation> pronunciations) {
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
    public ArrayList<String> getDatasets() {
        return datasets;
    }

    /**
     * 
     * @param datasets
     *     The datasets
     */
    public void setDatasets(ArrayList<String> datasets) {
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
    public ArrayList<Sense> getSenses() {
        return senses;
    }

    /**
     * 
     * @param senses
     *     The senses
     */
    public void setSenses(ArrayList<Sense> senses) {
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
    public ArrayList<Pronunciation> getPronunciations() {
        return pronunciations;
    }

    /**
     * 
     * @param pronunciations
     *     The pronunciations
     */
    public void setPronunciations(ArrayList<Pronunciation> pronunciations) {
        this.pronunciations = pronunciations;
    }

}
