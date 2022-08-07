package com.conicskill.app.ui.exam;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conicskill.app.R;
import com.conicskill.app.data.model.SelectableOption;
import com.conicskill.app.data.model.exam.Options;
import com.conicskill.app.util.PicassoImageGetter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SelectableAdapter extends RecyclerView.Adapter implements SelectableViewHolder.OnItemSelectedListener {

    private List<SelectableOption> mValues = new ArrayList<>();
    SelectableViewHolder.OnItemSelectedListener listener;
    private boolean isMultiSelectionEnabled = false;
    private Context mContext;


    public SelectableAdapter(SelectableViewHolder.OnItemSelectedListener listener,
                             List<Options> options, List<String> selectedOptions , boolean isMultiSelectionEnabled,
                             String correctAnswer) {
        this.listener = listener;
        this.isMultiSelectionEnabled = isMultiSelectionEnabled;


        mValues = new ArrayList<>();
        for(int i=0;i<options.size();i++) {
            boolean isSelected = false;
            boolean isCorrect = false;
            if(selectedOptions != null && selectedOptions.size() != 0) {
                if (selectedOptions.contains(options.get(i).getKey())) {
                    isSelected = true;
                }
                if(correctAnswer != null && !correctAnswer.isEmpty()){
                    if (checkCorrectAnswer(options.get(i).getKey(), correctAnswer)) {
                        isCorrect = true;
                    }
                }
            } else {
                if(correctAnswer != null && !correctAnswer.isEmpty()) {
                    if (checkCorrectAnswer(options.get(i).getKey(), correctAnswer)) {
                        isCorrect = true;
                    }
                }
            }
            mValues.add(new SelectableOption(options.get(i), isSelected, isCorrect));
        }
    }

    @Override
    @NonNull
    public SelectableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_options, parent, false);

        mContext = parent.getContext();
        return new SelectableViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        SelectableViewHolder holder = (SelectableViewHolder) viewHolder;
        SelectableOption selectableItem = mValues.get(position);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(selectableItem.getValue().contains("<img")) {
                holder.textView.setText(Html.fromHtml("     " + selectableItem.getKey() + ")   " + selectableItem.getValue(), Html.FROM_HTML_MODE_COMPACT,new PicassoImageGetter(Picasso.get(), holder.textView, mContext), null));
            } else {
                holder.textView.setText(Html.fromHtml("     " + selectableItem.getKey() + ")   " + selectableItem.getValue(), Html.FROM_HTML_MODE_COMPACT));
            }
        } else {
            if(selectableItem.getValue().contains("<img")) {
                holder.textView.setText(Html.fromHtml(selectableItem.getKey() + ")   " + selectableItem.getValue(),new PicassoImageGetter(Picasso.get(), holder.textView, mContext), null));
            } else {
                holder.textView.setText(Html.fromHtml(selectableItem.getKey() + ")   " + selectableItem.getValue()));
            }
//            holder.textView.setText(Html.fromHtml(selectableItem.getKey() + ")   " + selectableItem.getValue()));
        }

        if(selectableItem.isCorrect()) {
            holder.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.layout_right, 0);
            holder.textView.setCompoundDrawablePadding(15);
        }

        if (selectableItem.isSelected() && !(selectableItem.isCorrect())) {
            holder.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.layout_wrong, 0);
            holder.textView.setCompoundDrawablePadding(15);
        }

        holder.mItem = selectableItem;
        holder.setChecked(holder.mItem.isSelected());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public List<Options> getSelectedItems() {
        List<Options> selectedItems = new ArrayList<>();
        for (SelectableOption item : mValues) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    public List<String> getSelectedKeys() {
        List<String> selectedItems = new ArrayList<>();
        for (SelectableOption item : mValues) {
            if (item.isSelected()) {
                selectedItems.add(item.getKey());
            }
        }
        return selectedItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (isMultiSelectionEnabled) {
            return SelectableViewHolder.MULTI_SELECTION;
        } else {
            return SelectableViewHolder.SINGLE_SELECTION;
        }
    }

    public void clearOptions() {
        for (SelectableOption opt: mValues) {
            opt.setSelected(false);
        }
    }

    @Override
    public void onItemSelected(SelectableOption item) {
        if (!isMultiSelectionEnabled) {
            for (SelectableOption selectableItem : mValues) {
                if (!selectableItem.equals(item) && selectableItem.isSelected()) {
                    selectableItem.setSelected(false);
                } else if (selectableItem.equals(item)
                        && item.isSelected()) {
                    selectableItem.setSelected(true);
                }
            }
            notifyDataSetChanged();
        }
        listener.onItemSelected(item);
    }

    private boolean checkCorrectAnswer(String selectedOption, String correctAnswer) {
        TypeToken<List<String>> token = new TypeToken<List<String>>() {};
        List<String> animals = new Gson().fromJson(correctAnswer, token.getType());
        return animals.get(0).equalsIgnoreCase(selectedOption);
    }
}