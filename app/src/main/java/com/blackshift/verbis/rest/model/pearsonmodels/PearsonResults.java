
package com.blackshift.verbis.rest.model.pearsonmodels;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
@Parcel
public class PearsonResults {

    @SerializedName("status")
    long status;
    @SerializedName("offset")
    long offset;
    @SerializedName("limit")
    long limit;
    @SerializedName("count")
    long count;
    @SerializedName("total")
    long total;
    @SerializedName("url")
    String url;
    @SerializedName("results")
    ArrayList<Result> results = new ArrayList<Result>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public PearsonResults() {
    }

    /**
     *  @param status
     * @param offset
     * @param limit
     * @param count
     * @param total
     * @param url
     * @param results
     */
    public PearsonResults(long status, long offset, long limit, long count, long total, String url, ArrayList<Result> results) {
        this.status = status;
        this.offset = offset;
        this.limit = limit;
        this.count = count;
        this.total = total;
        this.url = url;
        this.results = results;
    }

    /**
     * 
     * @return
     *     The status
     */
    public long getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(long status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The offset
     */
    public long getOffset() {
        return offset;
    }

    /**
     * 
     * @param offset
     *     The offset
     */
    public void setOffset(long offset) {
        this.offset = offset;
    }

    /**
     * 
     * @return
     *     The limit
     */
    public long getLimit() {
        return limit;
    }

    /**
     * 
     * @param limit
     *     The limit
     */
    public void setLimit(long limit) {
        this.limit = limit;
    }

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
     *     The total
     */
    public long getTotal() {
        return total;
    }

    /**
     * 
     * @param total
     *     The total
     */
    public void setTotal(long total) {
        this.total = total;
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

}
