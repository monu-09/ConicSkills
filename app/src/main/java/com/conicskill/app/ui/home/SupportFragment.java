package com.conicskill.app.ui.home;

import static com.conicskill.app.BuildConfig.GOOGLE_MAP_ADDRESS;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.SupportModel;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.util.GlideApp;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SupportFragment extends BaseFragment implements SupportSelectedListener {

    @BindView(R.id.recyclerViewContacts)
    RecyclerView recyclerViewContacts;

    @BindView(R.id.floatingActionCall)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.imageViewDesignedBy)
    AppCompatImageView imageViewDesignedBy;

    @Inject
    ViewModelFactory viewModelFactory;
    private SupportViewModel mViewModel;

    public SupportFragment() {

    }

    @Override
    protected int layoutRes() {
        return R.layout.contact_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(SupportViewModel.class);

        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(getBaseActivity().getApplicationContext()));
        recyclerViewContacts.setAdapter(new SupportAdapter(getBaseActivity().getApplicationContext(), this, mViewModel, this));

        observeViewModel();

        GlideApp.with(getActivity().getApplicationContext())
                .load(R.drawable.icon_header_new)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageViewDesignedBy);

    }






    public void observeViewModel() {
        mViewModel.getContacts().observe(getViewLifecycleOwner(), values -> {
            if (values != null) {
                recyclerViewContacts.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onSupportClicked(SupportModel supportModel) {
        if (supportModel.getId() == 1) {
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_MAP_ADDRESS));
            mapIntent.setPackage("com.google.android.apps.maps");
            /*if (mapIntent.resolveActivity(getPackageManager()) != null) {*/
            startActivity(mapIntent);
            /*}*/
            /*else{
                Toast.makeText(getActivity(), getString(R.string.text_no_application_maps), Toast.LENGTH_SHORT).show();
            }*/
        } else if (supportModel.getId() == 2) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + getString(R.string.text_phone)));
            startActivity(intent);
        } else if (supportModel.getId() == 3) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_EMAIL, getResources().getString(R.string.cprep_email));
            intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, "Dear,\n");
            /*if (intent.resolveActivity(getPackageManager()) != null) {*/
            startActivity(intent);
            /*}
            else{
                Toast.makeText(ContactUsActivity.this, "No default email applications found...", Toast.LENGTH_SHORT).show();
            }*/
        } else if (supportModel.getId() == 4) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + getString(R.string.text_alternate_phone)));
            startActivity(intent);
        }
    }

    @OnClick(R.id.floatingActionCall)
    public void callToAction() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + getString(R.string.text_phone)));
        startActivity(intent);
    }

    @OnClick(R.id.floatingActionAlternateCall)
    public void callToAlternatePhone() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + getString(R.string.text_alternate_phone)));
        startActivity(intent);
    }
}
