package com.blackshift.verbis.rest.model.wordlist;

public class Word{
    private String id;
    private String headword;
    private String url;
    private long addedOn;

    public Word(String headword, String url) {
        this.headword = headword;
        this.url = url;
    }

    public Word(String headword) {
        this.headword = headword;
    }

    public Word(String id, String headword, String url) {
        this.id = id;
        this.headword = headword;
        this.url = url;
    }


    public Word() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadword() {
        return headword;
    }

    public void setHeadword(String headword) {
        this.headword = headword;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(long addedOn) {
        this.addedOn = addedOn;
    }
}
