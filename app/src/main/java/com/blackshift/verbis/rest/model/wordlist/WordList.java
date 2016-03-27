package com.blackshift.verbis.rest.model.wordlist;

import com.blackshift.verbis.utils.annotations.PrivacyLevel;

import java.util.List;

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
    private Long createdAt;
    private Long modifiedAt;

    @PrivacyLevel
    private Integer privacy;
    private List<String> likedBy;
    private boolean starred;

    public WordList() {
        privacy = PrivacyLevel.PRIVATE;
    }

    public WordList(String title) {
        this();
        this.title = title;
    }

    public WordList(String id, String title) {
        this(title);
        this.id = id;
    }

    public WordList(String id, String title,@PrivacyLevel Integer privacy) {
        this(id,title);
        this.privacy = privacy;
    }

    @PrivacyLevel
    public Integer getPrivacy() {
        return privacy;
    }

    public void setPrivacy(@PrivacyLevel Integer privacy) {
        this.privacy = privacy;
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

    public List<String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(List<String> likedBy) {
        this.likedBy = likedBy;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }
}
