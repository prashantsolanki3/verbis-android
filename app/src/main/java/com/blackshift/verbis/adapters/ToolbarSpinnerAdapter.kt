package com.blackshift.verbis.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.blackshift.verbis.R
import com.blackshift.verbis.rest.model.wordy.TextConfig
import com.blackshift.verbis.ui.widgets.FontTextView

class ToolbarSpinnerAdapter(context: Context, val data: List<TextConfig>) : ArrayAdapter<TextConfig>(context, R.layout.spinner_item, data) {

    internal var inflater: LayoutInflater

    init {
        inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getDropDownView(position: Int, convertView: View, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        val row = inflater.inflate(R.layout.spinner_item, parent, false)
        val tvCategory = row.findViewById(R.id.spinner_tv) as FontTextView
        val uri = Uri.Builder().path(data[position].font.toString()).build()
        val fontFile = uri.lastPathSegment.replace(".ttf", "").replace(".otf", "")
        tvCategory.text = fontFile
        tvCategory.setTextConfig(data[position])
        return row
    }
}