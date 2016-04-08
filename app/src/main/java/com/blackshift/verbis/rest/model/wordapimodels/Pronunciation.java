
package com.blackshift.verbis.rest.model.wordapimodels;

import com.google.gson.annotations.SerializedName;

public class Pronunciation {

    @SerializedName("all")
    String all;

    /**
     *
     * @return
     *     The all
     */
    public String getAll() {
        return all;
    }

    /**
     * 
     * @param all
     *     The all
     */
    public void setAll(String all) {
        this.all = all;
    }

}
