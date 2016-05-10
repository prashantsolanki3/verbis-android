package com.blackshift.verbis.ui.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

import com.blackshift.verbis.rest.model.wordy.TextConfig

/**
 * Package com.blackshift.verbis.ui.widgets
 *
 *
 * Created by Prashant on 4/9/2016.
 *
 *
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
class FontTextView : TextView {

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    fun setTextConfig(config: TextConfig?) {
        if (config != null) {
            if (config.color != null)
                setTextColor(Color.parseColor(config.color))
            if (config.size != 0)
                textSize = config.size.toFloat()
            if (config.font != null)
                typeface = Typeface.createFromFile(config.font)
        }
    }

}
