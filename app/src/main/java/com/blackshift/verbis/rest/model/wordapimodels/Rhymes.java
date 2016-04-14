package com.blackshift.verbis.rest.model.wordapimodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mohan on 14-04-2016.
 */
public class Rhymes {

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
