
package com.blackshift.verbis.rest.model.wordapimodels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WordsApiResult {

    @SerializedName("word")
    String word;

    @SerializedName("results")
    ArrayList<Result> results = new ArrayList<Result>();

    @SerializedName("syllables")
    Syllables syllables;

    @SerializedName("rhymes")
    Rhymes rhymes;

    @SerializedName("pronunciation")
    Pronunciation pronunciation;

    @SerializedName("frequency")
    double frequency;

    /**
     * 
     * @return
     *     The word
     */
    public String getWord() {
        return word;
    }

    /**
     * 
     * @param word
     *     The word
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * 
     * @return
     *     The results
     */
    public ArrayList<Result> getResults() {
        return results;
    }

    /**
     * 
     * @param results
     *     The results
     */
    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    /**
     * 
     * @return
     *     The syllables
     */
    public Syllables getSyllables() {
        return syllables;
    }

    /**
     * 
     * @param syllables
     *     The syllables
     */
    public void setSyllables(Syllables syllables) {
        this.syllables = syllables;
    }

    /**
     *
     * @return
     *     The rhymes
     */
    public Rhymes getRhymes() {
        return rhymes;
    }

    /**
     *
     * @param rhymes
     *     The rhymes
     */
    public void setRhymes(Rhymes rhymes) {
        this.rhymes = rhymes;
    }

    /**
     * 
     * @return
     *     The pronunciation
     */
    public Pronunciation getPronunciation() {
        return pronunciation;
    }

    /**
     * 
     * @param pronunciation
     *     The pronunciation
     */
    public void setPronunciation(Pronunciation pronunciation) {
        this.pronunciation = pronunciation;
    }

    /**
     * 
     * @return
     *     The frequency
     */
    public double getFrequency() {
        return frequency;
    }

    /**
     * 
     * @param frequency
     *     The frequency
     */
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

}
