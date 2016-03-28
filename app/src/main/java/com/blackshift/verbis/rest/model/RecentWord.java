package com.blackshift.verbis.rest.model;

/**
 * Created by Devika on 20-03-2016.
 */
public class RecentWord {

    private String id;
    private String word;



    public RecentWord(String id, String word) {
        this.id = id;
        this.word = word;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
