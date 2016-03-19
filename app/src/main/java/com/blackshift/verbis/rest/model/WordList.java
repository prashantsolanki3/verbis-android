package com.blackshift.verbis.rest.model;

/**
 * Package com.blackshift.verbis.rest.model
 * <p/>
 * Created by Prashant on 3/19/2016.
 * <p/>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
public class WordList {
    private String id;
    private String title;

    public WordList() {
    }

    public WordList(String title) {
        this.title = title;
    }

    public WordList(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
