package com.conicskill.app.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import com.conicskill.app.R;
import com.conicskill.app.data.model.SupportModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.ContactViewHolder> {

    private final List<SupportModel> data = new ArrayList<>();
    public SupportSelectedListener ecgSelectedListener;
    public Context mContext;

    SupportAdapter(Context context, LifecycleOwner lifecycleOwner, SupportViewModel supportViewModel, SupportSelectedListener ecgSelectedListener) {
        this.ecgSelectedListener = ecgSelectedListener;
        supportViewModel.getContacts().observe(lifecycleOwner, supportModels -> {
            if (supportModels != null) {
                data.addAll(supportModels);
                notifyDataSetChanged();
            }
        });
        this.mContext = context;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public SupportAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacts, parent, false);
        return new SupportAdapter.ContactViewHolder(view, ecgSelectedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SupportAdapter.ContactViewHolder holder, int position) {
        holder.bind(data.get(position), position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        private SupportModel ecgModel;

        @Nullable
        @BindView(R.id.cardViewEcgOption)
        CardView cardViewECGOption;

        @BindView(R.id.relativeLayoutHolder)
        RelativeLayout relativeLayoutHolder;

        @BindView(R.id.imageViewEcgOption)
        AppCompatImageView imageViewEcgOption;

        @BindView(R.id.textViewEcgHeading)
        AppCompatTextView textViewEcgHeading;

        @BindView(R.id.textViewEcgDescription)
        AppCompatTextView textViewEcgDescription;

        @BindView(R.id.textViewSubDescription)
        AppCompatTextView textViewSubDescription;

        ContactViewHolder(View itemView, SupportSelectedListener ecgSelectedListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                if (ecgModel != null) {
                    ecgSelectedListener.onSupportClicked(ecgModel);
                }
            });

            setIsRecyclable(false);
        }

        void bind(SupportModel supportModel, int position) {
            this.ecgModel = supportModel;

//            relativeLayoutHolder.setBackground(ContextCompat.getDrawable(mContext,supportModel.getBackground()));
            imageViewEcgOption.setImageDrawable(ContextCompat.getDrawable(mContext, supportModel.getIcon()));
            textViewEcgHeading.setText(supportModel.getHeading());
            textViewEcgDescription.setText(supportModel.getDescription());
            textViewSubDescription.setText(supportModel.getSubDescription());
        }
    }
}
