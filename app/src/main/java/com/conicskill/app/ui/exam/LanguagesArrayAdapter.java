package com.conicskill.app.ui.exam;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.AppCompatTextView;

import com.conicskill.app.R;
import com.conicskill.app.data.model.exam.Languages;

import java.util.ArrayList;
import java.util.List;

public class LanguagesArrayAdapter extends ArrayAdapter<Languages> {
    private final Context mContext;
    private final List<Languages> mLanguages;
    private final int mLayoutResourceId;

    public LanguagesArrayAdapter(Context context, int resource, List<Languages> languages) {
        super(context, resource, languages);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mLanguages = new ArrayList<>(languages);
    }

    public int getCount() {
        return mLanguages.size();
    }

    public Languages getItem(int position) {
        return mLanguages.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(mLayoutResourceId, parent, false);
            }
            Languages languages = getItem(position);
            AppCompatTextView name = convertView.findViewById(R.id.textViewSeriesName);
            name.setText(languages.getLanguage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
}