package com.conicskill.app.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.conicskill.app.R;
import com.conicskill.app.data.model.examlisting.TestListItem;
import com.conicskill.app.data.model.examlisting.TestSeriesItem;
import com.conicskill.app.util.GlideApp;
import com.intrusoft.sectionedrecyclerview.SectionRecyclerViewAdapter;

import java.util.List;

public class AdapterSectionRecycler extends SectionRecyclerViewAdapter<TestListItem, TestSeriesItem, SectionViewHolder, ChildViewHolder> {

    private Context context;
    private HomeClickListener clickListener;
    private List<TestListItem> data;

    public AdapterSectionRecycler(Context context, List<TestListItem> sectionItemList, HomeClickListener clickListener) {
        super(context, sectionItemList);
        this.context = context;
        this.data = sectionItemList;
        this.clickListener = clickListener;
    }

    @Override
    public SectionViewHolder onCreateSectionViewHolder(ViewGroup sectionViewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_menu, sectionViewGroup, false);
        return new SectionViewHolder(view);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_feature_list, childViewGroup, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindSectionViewHolder(SectionViewHolder sectionViewHolder, int sectionPosition, TestListItem section) {
        sectionViewHolder.topicName.setText(section.getTopic());
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder childViewHolder, int sectionPosition, int childPosition, TestSeriesItem child) {
        childViewHolder.featureName.setText(child.getTitle());
        GlideApp.with(context).load(child.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(childViewHolder.featureIcon);
        childViewHolder.itemView.setOnClickListener(l -> {
            clickListener.homeClicked(data.get(sectionPosition).getChildItems().get(childPosition));
        });
    }
}