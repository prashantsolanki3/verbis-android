
package com.blackshift.verbis.rest.model.wordapimodels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Result {

    @SerializedName("definition")
    String definition;

    @SerializedName("partOfSpeech")
    String partOfSpeech;

    @SerializedName("synonyms")
    ArrayList<String> synonyms = new ArrayList<String>();

    @SerializedName("antonyms")
    ArrayList<String> antonyms = new ArrayList<String>();

    @SerializedName("typeOf")
    ArrayList<String> typeOf = new ArrayList<String>();

    @SerializedName("hasTypes")
    ArrayList<String> hasTypes = new ArrayList<String>();

    @SerializedName("derivation")
    ArrayList<String> derivation = new ArrayList<String>();

    @SerializedName("examples")
    ArrayList<String> examples = new ArrayList<String>();

    /**
     * 
     * @return
     *     The definition
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * 
     * @param definition
     *     The definition
     */
    public void setDefinition(String definition) {
        this.definition = definition;
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
     *     The partOfSpeech
     */
    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    /**
     * 
     * @return
     *     The synonyms
     */
    public ArrayList<String> getSynonyms() {
        return synonyms;
    }

    /**
     * 
     * @param synonyms
     *     The synonyms
     */
    public void setSynonyms(ArrayList<String> synonyms) {
        this.synonyms = synonyms;
    }

    /**
     * 
     * @return
     *     The typeOf
     */
    public ArrayList<String> getTypeOf() {
        return typeOf;
    }

    /**
     * 
     * @param typeOf
     *     The typeOf
     */
    public void setTypeOf(ArrayList<String> typeOf) {
        this.typeOf = typeOf;
    }

    /**
     * 
     * @return
     *     The hasTypes
     */
    public ArrayList<String> getHasTypes() {
        return hasTypes;
    }

    /**
     * 
     * @param hasTypes
     *     The hasTypes
     */
    public void setHasTypes(ArrayList<String> hasTypes) {
        this.hasTypes = hasTypes;
    }

    /**
     * 
     * @return
     *     The derivation
     */
    public ArrayList<String> getDerivation() {
        return derivation;
    }

    /**
     * 
     * @param derivation
     *     The derivation
     */
    public void setDerivation(ArrayList<String> derivation) {
        this.derivation = derivation;
    }

    /**
     * 
     * @return
     *     The examples
     */
    public ArrayList<String> getExamples() {
        return examples;
    }

    /**
     * 
     * @param examples
     *     The examples
     */
    public void setExamples(ArrayList<String> examples) {
        this.examples = examples;
    }

    public ArrayList<String> getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(ArrayList<String> antonyms) {
        this.antonyms = antonyms;
    }
}
