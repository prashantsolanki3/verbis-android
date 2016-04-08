package com.blackshift.verbis.rest.model;

import org.parceler.Parcel;

/**
 * Created by Devika on 20-03-2016.
 */
@Parcel
public class RecentWord {

    String id;
    String word;
    Long createdAt;
    Long modifiedAt;

    public RecentWord(String id, String word, Long createdAt, Long modifiedAt) {
        this.id = id;
        this.word = word;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public RecentWord() {
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
