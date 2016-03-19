package com.blackshift.verbis.rest.model;

import com.blackshift.verbis.utils.annotations.PrivacyLevel;

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

    @PrivacyLevel
    private Integer privacy;

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
}
