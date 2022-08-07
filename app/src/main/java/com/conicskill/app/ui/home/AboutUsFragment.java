package com.conicskill.app.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.util.GlideApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class AboutUsFragment extends BaseFragment {

    @BindView(R.id.imageViewProfile)
    AppCompatImageView imageViewProfile;

    @BindView(R.id.textViewAboutUs)
    AppCompatTextView textViewAboutUs;

    public AboutUsFragment() {

    }

    @Override
    protected int layoutRes() {
        return R.layout.about_us_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textViewAboutUs.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

        GlideApp.with(getContext()).load(R.drawable.icon_square)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageViewProfile);
    }

}
