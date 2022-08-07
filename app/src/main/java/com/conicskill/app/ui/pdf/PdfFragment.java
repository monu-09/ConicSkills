package com.conicskill.app.ui.pdf;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.playlist.CategoryListItem;
import com.conicskill.app.data.model.videoCouses.CourseListItem;
import com.conicskill.app.data.model.videoCouses.SubCategoryItem;
import com.conicskill.app.database.Downloads;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.util.Utils;
import com.conicskill.app.workmanager.DownloadFilePRManager;
import com.conicskill.app.workmanager.DownloadFileWorkManager;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.app.NotificationCompat.PRIORITY_MAX;
import static com.conicskill.app.util.MyFirebaseMessagingService.ANDROID_CHANNEL_ID_DASHBOARD;
import static com.conicskill.app.util.MyFirebaseMessagingService.ANDROID_CHANNEL_NAME_DASHBOARD;


public class PdfFragment extends BaseFragment {

    public static int NOTIFICATION_ID = 100;
    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.pdfView)
    PDFView pdfView;
    @BindView(R.id.linearBack)
    LinearLayout linearLayoutTop;
    @BindView(R.id.imageButtonBack)
    ImageButton imageButtonBack;
    @BindView(R.id.textViewFileName)
    TextView textViewFileName;
    @BindView(R.id.fabDownload)
    FloatingActionButton fabDownload;
    @BindView(R.id.textViewDownload)
    AppCompatTextView textViewDownload;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private TextView textViewDownloadProgress;
    private AlertDialog materialAlertDialog;
    private PdfViewModel mViewModel;
    private String pdfUrl;
    private String CallBy;
    private CourseListItem courseListItem;
    private CategoryListItem categoryListItem;
    private SubCategoryItem subCategoryItem;

    public static PdfFragment newInstance(String pdfUrl, String checkCALLbY,
                                          CourseListItem course, CategoryListItem category,
                                          SubCategoryItem subCategory) {
        PdfFragment pdfFragment = new PdfFragment();
        Bundle args = new Bundle();
        args.putSerializable("pdfUrl", pdfUrl);
        args.putSerializable("callBy", checkCALLbY);
        args.putSerializable("course", course);
        args.putSerializable("category", category);
        args.putSerializable("subCategory", subCategory);
        pdfFragment.setArguments(args);
        return pdfFragment;
    }

    @Override
    protected int layoutRes() {
        return R.layout.pdf_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(PdfViewModel.class);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getSerializable("pdfUrl") != null) {
                pdfUrl = bundle.getString("pdfUrl");
            }
            if (bundle.getSerializable("callBy") != null) {
                CallBy = bundle.getString("callBy");
            }
            if (bundle.getSerializable("course") != null) {
                courseListItem = (CourseListItem) bundle.getSerializable("course");
            }
            if (bundle.getSerializable("category") != null) {
                categoryListItem = (CategoryListItem) bundle.getSerializable("category");
            }
            if (bundle.getSerializable("subCategory") != null) {
                subCategoryItem = (SubCategoryItem) bundle.getSerializable("subCategory");
            }
        }
        if (pdfUrl != null && !pdfUrl.isEmpty()) {
            loadPdfFromUrl(pdfUrl, Utils.getInstance(requireContext()).getRootDirectory(),
                    Utils.getInstance(requireContext()).getName(pdfUrl) + ".pdf");
            fabDownload.setOnClickListener(l -> {
                checkIfFileExists();
                Toasty.info(requireContext(), "Starting download...", Toasty.LENGTH_LONG).show();
            });
        } else {
            progressBar.setVisibility(View.GONE);
            textViewDownload.setVisibility(View.VISIBLE);
            textViewDownload.setText("No PDF file available for this class");
        }

        if (CallBy.equalsIgnoreCase("youtube")) {
            linearLayoutTop.setVisibility(View.GONE);
        } else {
            if (pdfUrl != null) {
                textViewFileName.setText(Utils.getInstance(requireContext()).getName(pdfUrl));
            }
        }
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        if(subCategoryItem == null || categoryListItem == null) {
            fabDownload.setVisibility(View.GONE);
        }
    }

    private void loadPdfFromUrl(String pdfUrl, String directoryPath, String fileName) {
        File downloadedFile = new File(directoryPath, fileName);
        if (downloadedFile.exists()) {
            showDownLoadedPdf(downloadedFile);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            PRDownloader.download(pdfUrl, directoryPath, fileName).build()
                    .setOnProgressListener(progress -> {
                        if(textViewDownload != null) {
                            textViewDownload.setText(Utils.getProgressDisplayLine(progress.currentBytes, progress.totalBytes));
                        }
                    })
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            if(textViewDownload != null) {
                                progressBar.setVisibility(View.GONE);
                                textViewDownload.setVisibility(View.GONE);
                                File downloadedFile = new File(directoryPath, fileName);
                                showDownLoadedPdf(downloadedFile);
                            }
                        }

                        @Override
                        public void onError(Error error) {
                            if(progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                                if (error.getResponseCode() == 404) {

                                }
                            }
                        }
                    });
        }
    }

    private void downloadFromUrl(String pdfUrl, String directoryPath, String fileName) {
        if (directoryPath.equalsIgnoreCase("")) {
            // TODO: Show toast
        } else {
            requireActivity().runOnUiThread(this::showProgressBar);

            PRDownloader.download(pdfUrl, directoryPath, fileName).build()
                    .setOnProgressListener(progress -> {
                        textViewDownloadProgress.setText(Utils.getProgressDisplayLine(progress.currentBytes, progress.totalBytes));
                    })
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            insertCourse(courseListItem);
                        }

                        @Override
                        public void onError(Error error) {
                            progressBar.setVisibility(View.GONE);
                            if (error.getResponseCode() == 404) {

                            }
                        }
                    });
        }
    }

    private void checkIfFileExists() {
        mViewModel.mAppDatabase.moduleDao().checkWhetherFileExists(pdfUrl).observeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<List<Downloads>>() {
                    @Override
                    public void onSuccess(@NonNull List<Downloads> downloads) {
                        if (downloads.size() > 0) {
                            File downloadedFile = new File(Utils.getInstance(requireContext()).getRootDirectory(),
                                    Utils.getInstance(requireContext()).getName(pdfUrl) + ".pdf");
                            showDownLoadedPdf(downloadedFile);
                        } else {
                            Data.Builder data = new Data.Builder();
                            data.putString(DownloadFileWorkManager.KEY_INPUT_URL, pdfUrl);
                            data.putString(DownloadFileWorkManager.KEY_NAME, Utils.getInstance(requireContext()).getName(pdfUrl));
                            data.putString(DownloadFileWorkManager.KEY_OUTPUT_FILE_NAME, Utils.getInstance(requireContext()).getName(pdfUrl) + ".pdf");
                            data.putString(DownloadFileWorkManager.KEY_COURSE_ITEM, new Gson().toJson(courseListItem));
                            data.putString(DownloadFileWorkManager.KEY_CATEGORY_ITEM, new Gson().toJson(categoryListItem));
                            data.putString(DownloadFileWorkManager.KEY_SUBCATEGORY_ITEM, new Gson().toJson(subCategoryItem));
                            data.putString(DownloadFileWorkManager.KEY_FILE_TYPE, DownloadFileWorkManager.FILE_TYPE_PDF);

                            OneTimeWorkRequest downloadWorkRequest =
                                    new OneTimeWorkRequest.Builder(DownloadFilePRManager.class)
                                            .addTag(pdfUrl)
                                            .setInputData(data.build())
                                            .build();
                            WorkManager.getInstance(requireContext()).enqueueUniqueWork(
                                    Utils.getInstance(requireContext()).getName(pdfUrl), ExistingWorkPolicy.REPLACE,
                                    downloadWorkRequest);
                            /*downloadFromUrl(pdfUrl, Utils.getInstance(requireContext()).getRootDirectory(),
                                    Utils.getInstance(requireContext()).getName(pdfUrl) + ".pdf");*/
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("TAG, onError: ", e.getLocalizedMessage());
                    }
                });
    }

    private void insertCourse(CourseListItem courseListItem) {
        mViewModel.mAppDatabase.moduleDao().insertCourse(courseListItem).observeOn(Schedulers.io())
                .subscribe(new DisposableMaybeObserver<Long>() {
                    @Override
                    public void onSuccess(@NonNull Long aLong) {
                        Log.e("TAG, onSuccess: ", aLong.toString());
                        // call insert
                        categoryListItem.setCourseId(courseListItem.getCourseId());
                        insertCategory(categoryListItem);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("TAG, onError: ", e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG, onComplete: ", "Completed");
                    }
                });
    }

    private void insertCategory(CategoryListItem category) {
        // insert category details
        mViewModel.mAppDatabase.moduleDao().insertCategory(category).observeOn(Schedulers.io())
                .subscribe(new DisposableMaybeObserver<Long>() {
                    @Override
                    public void onSuccess(@NonNull Long aLong) {
                        Log.e("TAG, onSuccess: ", aLong.toString());
                        // call insert subcategory
                        insertSubCategory(subCategoryItem);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("TAG, onError: ", e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG, onComplete: ", "Completed");
                    }
                });
    }

    private void insertSubCategory(SubCategoryItem subCategoryItem) {
        // insert category details
        mViewModel.mAppDatabase.moduleDao().insertSubCategory(subCategoryItem).observeOn(Schedulers.io())
                .subscribe(new DisposableMaybeObserver<Long>() {
                    @Override
                    public void onSuccess(@NonNull Long aLong) {
                        Log.e("TAG, onSuccess: ", aLong.toString());
                        // call insert subcategory
                        insertDownloads();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("TAG, onError: ", e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG, onComplete: ", "Completed");
                    }
                });
    }

    private void insertDownloads() {
        File downloadedFile = new File(Utils.getInstance(requireContext()).getRootDirectory(),
                Utils.getInstance(requireContext()).getName(pdfUrl) + ".pdf");
        Downloads downloads = new Downloads();
        downloads.setDisplayName(Utils.getInstance(requireContext()).getName(pdfUrl));
        downloads.setDownloadLocation(downloadedFile.getAbsolutePath());
        downloads.setCourseId(courseListItem.getCourseId());
        downloads.setCategoryId(subCategoryItem.getCatId());
        downloads.setSubCategoryId(subCategoryItem.getSubCategoryId());
        downloads.setUrl(pdfUrl);
        mViewModel.mAppDatabase.moduleDao().insertDownloads(downloads).observeOn(Schedulers.io())
                .subscribe(new DisposableMaybeObserver<Long>() {
                    @Override
                    public void onSuccess(@NonNull Long aLong) {
                        showDownLoadedPdf(downloadedFile);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("TAG , onError: ", e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG, onComplete: ", "");
                    }
                });
        requireActivity().runOnUiThread(() ->{
            Toast.makeText(requireActivity().getApplicationContext(), "Download Complete", Toast.LENGTH_LONG).show();
            materialAlertDialog.dismiss();
        });
    }

    private void showDownLoadedPdf(File file) {
        pdfView.fromFile(file)
                .password(null)
                .defaultPage(0)
                .enableDoubletap(true)
                .onLoad(nbPages -> {
                    progressBar.setVisibility(View.GONE);
                })
                .onPageError((page, t) -> {
                    progressBar.setVisibility(View.GONE);
                    Log.e("TAG", "onPageError: " + t.getLocalizedMessage());
                }).load();
    }

    public void showNotificationForDashboard(Context context, String title, String message, String url,
                                             String smallTitle, Uri mFilePath) {

        // create android channel
        Intent intent = null;
        PendingIntent pIntent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(mFilePath, "application/pdf");
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(mFilePath, "application/pdf");
            intent = Intent.createChooser(intent, "Open File");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent finalPIntent = pIntent;
        Glide.with(this).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                inboxStyle.addLine(message);
                if (Build.VERSION.SDK_INT >= 26) {
                    NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID_DASHBOARD,
                            ANDROID_CHANNEL_NAME_DASHBOARD, NotificationManager.IMPORTANCE_DEFAULT);
                    // Sets whether notifications posted to this channel should display notification lights
                    androidChannel.enableLights(true);
                    // Sets whether notification posted to this channel should vibrate.
                    androidChannel.enableVibration(true);
                    // Sets the notification light color for notifications posted to this channel
                    androidChannel.setLightColor(ContextCompat.getColor(context, R.color.colorAccent));
                    // Sets whether notifications posted to this channel appear on the lockscreen or not
                    androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ANDROID_CHANNEL_ID_DASHBOARD);
                    builder.setContentTitle(title)
                            .setContentText(message)
                            .setStyle(inboxStyle)
                            .setColorized(true)
                            .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                            .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                            .setSubText(smallTitle)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_square))
                            .setSmallIcon(R.drawable.icon_square)
                            .setAutoCancel(true)
                            .setPriority(PRIORITY_MAX)
                            .setContentIntent(finalPIntent);

                    NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(androidChannel);
                        notificationManager.notify(NOTIFICATION_ID++, builder.build());
                    }
                } else {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ANDROID_CHANNEL_ID_DASHBOARD);
                    builder.setContentTitle(title)
                            .setContentText(message)
                            .setSubText(smallTitle)
                            .setStyle(inboxStyle)
                            .setColorized(true)
                            .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                            .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                            .setChannelId(ANDROID_CHANNEL_ID_DASHBOARD)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_square))
                            .setSmallIcon(R.drawable.icon_square)
                            .setAutoCancel(true)
                            .setPriority(PRIORITY_MAX)
                            .setContentIntent(finalPIntent);

                    NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(NOTIFICATION_ID++, builder.build());
                }
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                inboxStyle.addLine(message);
                if (Build.VERSION.SDK_INT >= 26) {
                    NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID_DASHBOARD,
                            ANDROID_CHANNEL_NAME_DASHBOARD, NotificationManager.IMPORTANCE_DEFAULT);
                    // Sets whether notifications posted to this channel should display notification lights
                    androidChannel.enableLights(true);
                    // Sets whether notification posted to this channel should vibrate.
                    androidChannel.enableVibration(true);
                    // Sets the notification light color for notifications posted to this channel
                    androidChannel.setLightColor(ContextCompat.getColor(context, R.color.colorAccent));
                    // Sets whether notifications posted to this channel appear on the lockscreen or not
                    androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ANDROID_CHANNEL_ID_DASHBOARD);
                    builder.setContentTitle(title)
                            .setContentText(message)
                            .setStyle(inboxStyle)
                            .setColorized(true)
                            .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                            .setSubText(smallTitle)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_square))
                            .setSmallIcon(R.drawable.icon_square)
                            .setAutoCancel(true)
                            .setPriority(PRIORITY_MAX)
                            .setContentIntent(finalPIntent);

                    NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(androidChannel);
                        notificationManager.notify(NOTIFICATION_ID++, builder.build());
                    }
                } else {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ANDROID_CHANNEL_ID_DASHBOARD);
                    builder.setContentTitle(title)
                            .setContentText(message)
                            .setSubText(smallTitle)
                            .setStyle(inboxStyle)
                            .setColorized(true)
                            .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                            .setChannelId(ANDROID_CHANNEL_ID_DASHBOARD)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_square))
                            .setSmallIcon(R.drawable.icon_square)
                            .setAutoCancel(true)
                            .setPriority(PRIORITY_MAX)
                            .setContentIntent(finalPIntent);

                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(NOTIFICATION_ID++, builder.build());
                }
            }

            @Override
            public void onLoadCleared(Drawable placeholder) {
            }
        });
    }

    private void showProgressBar() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity(),
                R.style.ThemeOverlay_Catalog_MaterialAlertDialog_OutlinedButton_AlertDialog);

        View view = LayoutInflater.from(requireActivity()).inflate(R.layout.item_progress_layout, null);
        ProgressBar progressBarVideoQuality = view.findViewById(R.id.customLoadingImageView);
        textViewDownloadProgress = view.findViewById(R.id.messageItemProgress);
        builder.setTitle("Downloading PDF")
                .setMessage("")
                .setCancelable(false)
                .setView(view);

        materialAlertDialog = builder.create();
        materialAlertDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
