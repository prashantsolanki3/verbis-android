package com.blackshift.verbis.ui.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.blackshift.verbis.rest.model.wordy.TextConfig;

/**
 * Package com.blackshift.verbis.ui.widgets
 * <p/>
 * Created by Prashant on 4/9/2016.
 * <p/>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
public class FontTextView extends TextView {

    public FontTextView(Context context) {
        super(context);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTextConfig(TextConfig config){
        if(config!=null){
            if(config.getColor()!=null)
                setTextColor(Color.parseColor(config.getColor()));
            if(config.getSize()!=0)
                setTextSize(config.getSize());
            if(config.getFont()!=null)
                setTypeface(Typeface.createFromFile(config.getFont()));
        }
    }

}
