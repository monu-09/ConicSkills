package com.conicskill.app.ui.exam;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.AppCompatTextView;

import com.conicskill.app.R;
import com.conicskill.app.data.model.exam.Modules;

import java.util.ArrayList;
import java.util.List;

public class ModulesArrayAdapter extends ArrayAdapter<Modules> {
    private final Context mContext;
    private final List<Modules> mDepartments;
    private final int mLayoutResourceId;

    public ModulesArrayAdapter(Context context, int resource, List<Modules> departments) {
        super(context, resource, departments);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mDepartments = new ArrayList<>(departments);
    }

    public int getCount() {
        return mDepartments.size();
    }

    public Modules getItem(int position) {
        return mDepartments.get(position);
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
            Modules department = getItem(position);
            AppCompatTextView name = convertView.findViewById(R.id.textViewSeriesName);
            name.setText(department.getModuleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
}