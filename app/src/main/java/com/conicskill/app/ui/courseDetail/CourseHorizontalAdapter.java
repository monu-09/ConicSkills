package com.conicskill.app.ui.courseDetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.conicskill.app.R;
import com.conicskill.app.data.model.videoCouses.CourseListItem;
import com.conicskill.app.data.model.videoCouses.VideoListItem;
import com.conicskill.app.ui.CourseDetailActivity;
import com.conicskill.app.util.GlideApp;
import com.conicskill.app.util.Utils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseHorizontalAdapter extends RecyclerView.Adapter<CourseHorizontalAdapter.CourseViewHolder> {

    private ArrayList<VideoListItem> videoListItemArrayList = new ArrayList<>();
    private Activity mContext;
    private final int cardWidth;
    private final int cardHeight;
    private String isCoursePurchased;
    private CourseListItem course;

    public CourseHorizontalAdapter(Activity context, ArrayList<VideoListItem> data,
                                   String isCoursePurchased,
                                   CourseListItem courseListItem) {
        this.cardWidth = computeCardWidth(context);
        this.cardHeight = computeCardHeight(context, cardWidth);
        this.mContext = context;
        this.videoListItemArrayList = data;
        this.isCoursePurchased = isCoursePurchased;
        this.course = courseListItem;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
//        view.getRootView().getLayoutParams().width = cardWidth;
//        view.getRootView().getLayoutParams().height = cardHeight;
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        holder.bind(videoListItemArrayList.get(position), position);
        holder.itemView.setOnClickListener(v ->{
            if(videoListItemArrayList.get(position).isUpcoming() == 0) {
                if (videoListItemArrayList.get(position).getIsPaid().equalsIgnoreCase("0")) {
                    try {
                        String[] url = videoListItemArrayList.get(position).getUrl().split("=");
                        /*Utils.getInstance(mContext).voidOpen(mContext,
                                videoListItemArrayList.get(position).getTitle(),
                                url[1],
                                videoListItemArrayList.get(position),
                                course, , );*/
                    } catch (Exception e) {

                    }
                } else {
                    if (isCoursePurchased.equalsIgnoreCase("0")) {
                        Intent intent = new Intent(mContext, CourseDetailActivity.class);
                        intent.putExtra("course", course);
                        mContext.startActivity(intent);
                    } else if (isCoursePurchased.equalsIgnoreCase("1")) {
                        try {
                            String[] url = videoListItemArrayList.get(position).getUrl().split("=");
                            /*Utils.getInstance(mContext).voidOpen(mContext,videoListItemArrayList.get(position).getTitle(),
                                    url[1],
                                    videoListItemArrayList.get(position),
                                    course);*/
                        } catch (Exception e) {

                        }
                    }
                }
            } else {
                // do nothing
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return videoListItemArrayList.size();
    }

    private int computeCardWidth(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().widthPixels
                - context.getResources().getDimensionPixelSize(R.dimen.peek_width)
                - context.getResources().getDimensionPixelSize(R.dimen.item_spacing);
    }

    private int computeCardHeight(@NonNull Context context, int width) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, null, false);
        view.getRootView().measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), 0);
        return view.getRootView().getMeasuredHeight();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewNewsHeading)
        AppCompatTextView textViewNewsHeading;

        @BindView(R.id.textViewSubDescription)
        AppCompatTextView textViewSubDescription;

        @BindView(R.id.imageViewNews)
        AppCompatImageView imageViewNews;

        @BindView(R.id.linearLayoutVideoLecture)
        LinearLayout linearLayoutTotalLecture;

        @BindView(R.id.textViewEventTime)
        TextView textViewEventTime;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(VideoListItem videoListItem, int position) {
            linearLayoutTotalLecture.setVisibility(View.GONE);

            textViewNewsHeading.setText(videoListItem.getTitle());
            if(videoListItem.getDescription() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textViewSubDescription.setText(Html.fromHtml(videoListItem.getDescription(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    textViewSubDescription.setText(Html.fromHtml(videoListItem.getDescription()));
                }
            } else {
                textViewSubDescription.setVisibility(View.GONE);
            }
            GlideApp.with(mContext).load(videoListItem.getThumbnail())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(imageViewNews);

            textViewNewsHeading.setTextColor(ContextCompat.getColor(mContext, R.color.persian_green));
            textViewNewsHeading.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            textViewEventTime.setVisibility(View.VISIBLE);
            textViewEventTime.setText("Live at: " + Utils.getInstance(mContext).getHumanReadableDateTime(videoListItem.getEventDateTime()));
        }
    }
}
