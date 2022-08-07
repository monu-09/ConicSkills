package com.conicskill.app.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.conicskill.app.BuildConfig;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseActivity;
import com.conicskill.app.data.model.currentAffairDetails.CADetailRequest;
import com.conicskill.app.data.model.currentAffairDetails.CADetailRequestData;
import com.conicskill.app.data.model.currentAffairDetails.CADetailResponseData;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.ui.currentAffairs.CurrentAffairsFragment;
import com.conicskill.app.ui.currentAffairs.CurrentAffairsViewModel;
import com.conicskill.app.util.Constants;
import com.conicskill.app.util.GlideApp;
import com.conicskill.app.util.ProgressOverlay;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class CADetailActivity extends BaseActivity {

    @Inject
    ViewModelFactory viewModelFactory;
    private CurrentAffairsViewModel mViewModel;
    private ProgressDialog progressOverlay;
    String currentAffairId;
    ShapeableImageView caDetailImage;
    AppCompatTextView caDetailHeading;
    AppCompatTextView caDetailDescription;
    AppCompatTextView caDetailRef;
    AppCompatImageButton backtoCurrentAffairs;
    FloatingActionButton shareButton;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected int layoutRes() {
        return R.layout.activity_c_a_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_a_detail);
        /*To handle the FileUriExposedException on SDK greater than N or greater*/
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = getIntent();
        currentAffairId = intent.getStringExtra("currentAffairId");
        Log.e("cid**",currentAffairId);
        getLayoutDetails();
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(CurrentAffairsViewModel.class);
        observeViewModel();
        CADetailRequestData caDetailRequestData = new CADetailRequestData();
        caDetailRequestData.setCurrentAffairId(currentAffairId);
        CADetailRequest caDetailRequest = new CADetailRequest();
        caDetailRequest.setToken(mViewModel.tinyDB.getString(Constants.AUTH_TOKEN));
        caDetailRequest.setCaDetailRequestData(caDetailRequestData);
        mViewModel.fetchCurrentAffairDetailsAPI(caDetailRequest);
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private void observeViewModel(){

        mViewModel.getLoading().observe(this, loading -> {
            if (loading) {
                progressOverlay = ProgressOverlay.show(this, "");
            } else {
                progressOverlay.dismiss();
            }
        });

        mViewModel.getError().observe(this, isError -> {
            if (isError != null) {
                if (isError) {
                    progressOverlay.dismiss();
                    this.onBackPressed();
                }
            }
        });

        mViewModel.checkDetails().observe(this,detailsResponse -> {
            if(detailsResponse.code() == HttpsURLConnection.HTTP_OK){
                if(detailsResponse.body() != null){
                    if(detailsResponse.body().getCaDetailResponseData()!= null){
//                        Log.e("Details**",""+detailsResponse.body().getCaDetailResponseData().getSmallBody());
                        CADetailResponseData data = detailsResponse.body().getCaDetailResponseData();
                        float radius = getResources().getDimension(R.dimen.spacing_small);
                        caDetailImage.setShapeAppearanceModel(caDetailImage.getShapeAppearanceModel()
                                .toBuilder()
                                .setBottomLeftCorner(CornerFamily.ROUNDED,radius)
                                .setBottomRightCorner(CornerFamily.ROUNDED,radius)
                                .build());
                       GlideApp.with(this).load(data.getImageUrl())
                               .diskCacheStrategy(DiskCacheStrategy.DATA)
                                .into(caDetailImage);
                        caDetailHeading.setText(data.getTitle());
                        caDetailDescription.setText(data.getBody());
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Log.e("back pressed","");
        Fragment currentAffairFragment = getSupportFragmentManager().findFragmentByTag("currentFragment");
        if(currentAffairFragment instanceof CurrentAffairsFragment && currentAffairFragment.isVisible()){
            getSupportFragmentManager().popBackStack();
        }
    }

    public Bitmap takeScreenshot() {
        Log.i("screenShot**","");
        View rootView = findViewById(R.id.constrainViewCurrentAffair).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {
        Log.i("screenShot**","saving**"+bitmap);
        File imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            shareIt(imagePath);
        } catch (FileNotFoundException e) {
            Log.e("Screenshot file Ex", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("Screenshot IO Ex", e.getMessage(), e);
        }
    }

    private void shareIt(File imagePath) {
        Log.i("share it**","called");
        Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " current affair");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(CADetailActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(CADetailActivity.this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            Bitmap bitmap = takeScreenshot();
            saveBitmap(bitmap);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == STORAGE_PERMISSION_CODE){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
            }
            else {
                Toast.makeText(CADetailActivity.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void getLayoutDetails(){
        /*Links of all UI views*/
        caDetailImage = findViewById(R.id.imageCADetail);
        caDetailHeading = findViewById(R.id.textCADetailHeading);
        caDetailDescription = findViewById(R.id.textCADetailDescription);
        caDetailRef = findViewById(R.id.textCADetailReferences);
        backtoCurrentAffairs = findViewById(R.id.backtoCurrentAffairs);
        shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        STORAGE_PERMISSION_CODE);
            }

        });
        backtoCurrentAffairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


}


