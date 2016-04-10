
package com.blackshift.verbis.rest.model.pearsonmodels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Sense {

    @SerializedName("definition")
    ArrayList<String> definition = new ArrayList<String>();
    @SerializedName("variants")
    ArrayList<Variant> variants = new ArrayList<Variant>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Sense() {
    }

    /**
     *  @param definition
     * @param variants*/
    public Sense(ArrayList<String> definition, ArrayList<Variant> variants) {
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
    public void setDefinition(ArrayList<String> definition) {
        this.definition = definition;
    }

    /**
     * 
     * @return
     *     The variants
     */
    public ArrayList<Variant> getVariants() {
        return variants;
    }

    /**
     *
     * @param variants
     *     The variants
     */
    public void setVariants(ArrayList<Variant> variants) {
        this.variants = variants;
    }

}
