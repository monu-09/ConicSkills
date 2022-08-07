package com.conicskill.app.ui.currentAffairs;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsItem;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.util.GlideApp;
import com.idanatz.oneadapter.OneAdapter;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;

public class CurrentAffairsDetailFragment extends BaseFragment {

    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.imageViewCurrentAffairDetail)
    AppCompatImageView currentAffairDetailImage;
    @BindView(R.id.textViewCurrentDetailHeading)
    AppCompatTextView currentAffairDetailHeading;
    @BindView(R.id.textViewFullDescription)
    AppCompatTextView currentAffairFullDescription;
    @BindView(R.id.textViewReferences)
    AppCompatTextView textViewReferences;
    @BindView(R.id.textViewRefDetails)
    AppCompatTextView currentAffairRefDetail;
    private CurrentAffairsViewModel mViewModel;
    CurrentAffairsItem data;

//    public static CurrentAffairsDetailFragment newInstance() {
//        return new CurrentAffairsDetailFragment();
//    }

    public CurrentAffairsDetailFragment (CurrentAffairsItem item){
        this.data = item;
        Log.e("data**",""+item);
    }

    @Override
    protected int layoutRes() {
        return R.layout.current_affair_detail_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(CurrentAffairsViewModel.class);
      setData();

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setData(){
        GlideApp.with(getContext()).load(data.getImageUrl()).into(currentAffairDetailImage);
        currentAffairDetailHeading.setText(data.getTitle());
//        currentAffairDetailSubHeading.setText();
//        Typeface detailtypeface = Typeface.createFromAsset(getContext().getAssets(),"fonts/Gidole.otf");
//        Typeface refTypeface = Typeface.createFromAsset(getContext().getAssets(),"fonts/Gidole.otf");
//        currentAffairFullDescription.setTypeface(detailtypeface);
//        textViewReferences.setTypeface(refTypeface);
//        currentAffairRefDetail.setTypeface(refTypeface);
       if(data.getBody() == null){
           currentAffairFullDescription.setText(data.getSmallBody());
       }
       else {
           currentAffairFullDescription.setText(data.getBody());
       }
       if(data.getRefrences() != null){
           currentAffairRefDetail.setText(data.getRefrences());
       }
    }
}


