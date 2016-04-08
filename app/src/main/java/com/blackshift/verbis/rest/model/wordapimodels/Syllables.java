
package com.blackshift.verbis.rest.model.wordapimodels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Syllables {

    @SerializedName("count")
    long count;

    @SerializedName("list")
    ArrayList<String> list = new ArrayList<String>();

    /**
     * 
     * @return
     *     The count
     */
    public long getCount() {
        return count;
    }

    /**
     * 
     * @param count
     *     The count
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * 
     * @return
     *     The ArrayList
     */
    public ArrayList<String> getList() {
        return list;
    }

    /**
     * 
     * @param list
     *     The ArrayList
     */
    public void setList(ArrayList<String> list) {
        this.list = list;
    }

}
