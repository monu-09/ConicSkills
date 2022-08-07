package com.conicskill.app.ui.courseDetail;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.conicskill.app.R;
import com.conicskill.app.data.model.playlist.CategoryListItem;
import com.conicskill.app.data.model.videoCouses.CourseListItem;
import com.conicskill.app.ui.courseDetail.interfaces.CourseClickListener;
import com.conicskill.app.util.GlideApp;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.PlayListViewHolder> {

    private ArrayList<CategoryListItem> categoryListItems = new ArrayList<>();
    private Context context;
    private CourseListItem courseListItem;
    private CourseClickListener listener;

    public PlayListAdapter(ArrayList<CategoryListItem> categoryListItems, Context context, CourseListItem courseListItem, CourseClickListener courseClickListener) {
        this.categoryListItems = categoryListItems;
        this.context = context;
        this.courseListItem = courseListItem;
        this.listener = courseClickListener;
    }

    @NonNull
    @Override
    public PlayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_recycler_playlist,parent,false);
        return new PlayListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PlayListViewHolder holder, int position) {
        holder.textViewsubject.setText(categoryListItems.get(position).getCategoryName());
        GlideApp.with(context).load(categoryListItems.get(position).getThumbnail().trim())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.exo_controls_play)
                .circleCrop()
                .into(holder.imageView);
        holder.textViewsubject.setTextColor(Color.parseColor(categoryListItems.get(position).getColorCode()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPlayListItemClicked(categoryListItems.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryListItems.size();
    }

    public class PlayListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardViewPlayList)
        CardView cardView;
        @BindView(R.id.textViewPlayListSubject)
        TextView textViewsubject;

        @BindView(R.id.imageViewPlaylistBanner)
        ImageView imageView;

        @BindView(R.id.linearLayoutPlayslist)
        RelativeLayout linearLayout;
        public PlayListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
