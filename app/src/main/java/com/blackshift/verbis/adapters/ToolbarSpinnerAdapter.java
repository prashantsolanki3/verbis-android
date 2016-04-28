package com.blackshift.verbis.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.blackshift.verbis.R;
import com.blackshift.verbis.rest.model.wordy.TextConfig;
import com.blackshift.verbis.ui.widgets.FontTextView;

import java.util.List;

public class ToolbarSpinnerAdapter extends ArrayAdapter<TextConfig> {

    public Resources res;
    LayoutInflater inflater;
    private Context context;
    private List<TextConfig> data;

    public ToolbarSpinnerAdapter(Context context, List<TextConfig> objects) {
        super(context, R.layout.spinner_item, objects);
        this.context = context;
        data = objects;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.spinner_item, parent, false);
        FontTextView tvCategory = (FontTextView) row.findViewById(R.id.spinner_tv);
        Uri uri = new Uri.Builder().path(data.get(position).getFont().toString()).build();
        String fontFile = uri.getLastPathSegment().replace(".ttf","").replace(".otf","");
        tvCategory.setText(fontFile);
        tvCategory.setTextConfig(data.get(position));
        return row;
    }
}