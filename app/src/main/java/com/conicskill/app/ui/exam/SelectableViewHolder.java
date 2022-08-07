package com.conicskill.app.ui.exam;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.conicskill.app.R;
import com.conicskill.app.data.model.SelectableOption;

public class SelectableViewHolder extends RecyclerView.ViewHolder {

    public static final int MULTI_SELECTION = 2;
    public static final int SINGLE_SELECTION = 1;
    public AppCompatTextView textView;
    public SelectableOption mItem;
    private OnItemSelectedListener itemSelectedListener;

    public SelectableViewHolder(View view, OnItemSelectedListener listener) {
        super(view);
        itemSelectedListener = listener;
        textView = view.findViewById(R.id.textViewOptions);
        if(mItem != null) {
            if (mItem.isSelected()) {
                setChecked(true);
            } else {
                setChecked(false);
            }
        }
        textView.setOnClickListener((v) -> {
            if (mItem.isSelected() && getItemViewType() == MULTI_SELECTION) {
                setChecked(false);
            } else {
                setChecked(true);
            }
            itemSelectedListener.onItemSelected(mItem);
        });
    }

    public void setChecked(boolean value) {
        if (value) {
            textView.setBackgroundResource(R.drawable.drawable_cornered_bg_purple);
            textView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.md_white_1000));
        } else {
            textView.setBackgroundResource(R.drawable.border_purple);
            textView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimaryDark));
        }
        mItem.setSelected(value);
    }

    public interface OnItemSelectedListener {

        void onItemSelected(SelectableOption item);
    }

}