package com.conicskill.app.ui.pdf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.conicskill.app.R;
import com.conicskill.app.data.model.VideoList;

import java.util.List;


public class AdapterVideoQ extends RecyclerView.Adapter<AdapterVideoQ.Holder> {
    private Context context;
    private List<VideoList> videoLists;
    private OnClickInterface onClickInterface;

    public AdapterVideoQ(Context context, List<VideoList> videoLists, OnClickInterface onClickInterface) {
        this.context = context;
        this.videoLists = videoLists;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.list_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.btn_video.setText(videoLists.get(position).getQuailty());
        holder.btn_video.setOnClickListener(v -> onClickInterface.position(position, ""));
    }

    @Override
    public int getItemCount() {
        return videoLists.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private AppCompatButton btn_video;

        public Holder(@NonNull View itemView) {
            super(itemView);
            btn_video = itemView.findViewById(R.id.btn_video);
        }
    }
}
