package com.blackshift.verbis.ui.viewholders

import android.content.Context
import android.view.View
import android.widget.TextView

import com.blackshift.verbis.R
import com.blackshift.verbis.rest.model.recyclerviewmodels.Example

import io.github.prashantsolanki3.snaplibrary.snap.layout.viewholder.SnapViewHolder

/**
 * Created by Devika on 04-05-2016.
 */
class ExampleViewHolder(itemView: View, context: Context) : SnapViewHolder<Example>(itemView, context) {

    internal var exampleText: TextView

    init {
        exampleText = itemView.findViewById(R.id.example) as TextView
    }

    override fun populateViewHolder(example: Example, i: Int) {

        exampleText.text = null
        exampleText.text = example.example

    }

}
