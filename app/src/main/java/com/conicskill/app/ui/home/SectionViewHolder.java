package com.conicskill.app.ui.home;

import android.view.View;

import com.conicskill.app.R;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class SectionViewHolder extends RecyclerView.ViewHolder {

    AppCompatTextView topicName;

    public SectionViewHolder(View itemView) {
        super(itemView);
        topicName = itemView.findViewById(R.id.textViewSeriesName);
        topicName.setTextSize(20);
    }
}