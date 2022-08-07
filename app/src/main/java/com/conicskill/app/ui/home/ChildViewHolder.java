package com.conicskill.app.ui.home;

import android.view.View;

import com.conicskill.app.R;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class ChildViewHolder extends RecyclerView.ViewHolder {

    AppCompatImageView featureIcon;
    AppCompatTextView featureName;
    public ChildViewHolder(View itemView) {
        super(itemView);
        featureIcon = itemView.findViewById(R.id.featureIcon);
        featureName = itemView.findViewById(R.id.featureName);
    }
}