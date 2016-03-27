package com.blackshift.verbis.utils.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Package com.blackshift.verbis.utils.annotations
 * <p>
 * Created by Prashant on 3/19/2016.
 * <p>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({PrivacyLevel.PUBLIC, PrivacyLevel.PRIVATE,PrivacyLevel.FRIENDS})
public @interface PrivacyLevel {
    int PUBLIC=10,PRIVATE=0,FRIENDS=5;
}
