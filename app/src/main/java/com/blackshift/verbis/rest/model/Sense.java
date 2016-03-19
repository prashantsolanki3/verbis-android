
package com.blackshift.verbis.rest.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Sense {

    @SerializedName("definition")
    private List<String> definition = new ArrayList<String>();
    @SerializedName("variants")
    private List<Variant> variants = new ArrayList<Variant>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Sense() {
    }

    /**
     * 
     * @param definition
     * @param variants
     */
    public Sense(List<String> definition, List<Variant> variants) {
        this.definition = definition;
        this.variants = variants;
    }

    /**
     * 
     * @return
     *     The definition
     */
    public List<String> getDefinition() {
        return definition;
    }

    /**
     * 
     * @param definition
     *     The definition
     */
    public void setDefinition(List<String> definition) {
        this.definition = definition;
    }

    /**
     * 
     * @return
     *     The variants
     */
    public List<Variant> getVariants() {
        return variants;
    }

    /**
     * 
     * @param variants
     *     The variants
     */
    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }

}
