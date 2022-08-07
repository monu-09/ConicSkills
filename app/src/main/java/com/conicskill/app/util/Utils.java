/*
 * Copyright (c) 2017. Alfanse Developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package com.conicskill.app.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.conicskill.app.R;
import com.conicskill.app.data.model.candidateLogin.PhoneInformation;
import com.conicskill.app.data.model.playlist.CategoryListItem;
import com.conicskill.app.data.model.videoCouses.CourseListItem;
import com.conicskill.app.data.model.videoCouses.SubCategoryItem;
import com.conicskill.app.data.model.videoCouses.VideoListItem;
import com.conicskill.app.ui.pdf.AdapterVideoQ;
import com.conicskill.app.ui.pdf.PlayerActivity;
import com.conicskill.app.data.model.VideoList;
import com.conicskill.app.ui.pdf.YoutubeVideoActivity;
import com.github.kotvertolet.youtubejextractor.YoutubeJExtractor;
import com.github.kotvertolet.youtubejextractor.exception.ExtractionException;
import com.github.kotvertolet.youtubejextractor.exception.VideoIsUnavailable;
import com.github.kotvertolet.youtubejextractor.exception.YoutubeRequestException;
import com.github.kotvertolet.youtubejextractor.models.AdaptiveVideoStream;
import com.github.kotvertolet.youtubejextractor.models.newModels.VideoPlayerConfig;
import com.github.kotvertolet.youtubejextractor.models.youtube.playerResponse.MuxedStream;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Utils {

    public static AlertDialog alertDialog;
    private static Utils sInstance;
    private static Context mContext;

    private Utils(Context context) {
        mContext = context;
    }

    public static synchronized Utils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Utils(context);
        }
        return sInstance;
    }

    public static String getTimeStamp() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        return ts;
    }

    public static boolean isUpdateAvailable(Context context, int code) {
        try {
            PackageInfo packageManager = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageManager.versionCode < code;

        } catch (Exception e) {

        }
        return false;
    }

    public static void hideSoftKeyboard(Context context) {
        try {
            if (((Activity) context).getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                //noinspection ConstantConditions,ConstantConditions
                inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            Log.d("", "Exception in hiding keyboard: " + e.toString());
        }
    }

    /**
     * Is online boolean.
     *
     * @param context the context
     * @return the boolean
     */
    public static boolean isOnline(Context context) {
        return true;
    }

    /**
     * Show snack bar.
     *
     * @param view    the view
     * @param context the context
     * @param message the message
     */
    public static void showSnackBar(View view, Context context, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        snackbar.show();
    }

    public static void deleteDatabaseFile(Context context, String databaseName) {
        File databases = new File(context.getApplicationInfo().dataDir + "/databases");
        File db = new File(databases, databaseName);
        if (db.delete())
            System.out.println("Database deleted");
        else
            System.out.println("Failed to delete database");

        File journal = new File(databases, databaseName + "-journal");
        if (journal.exists()) {
            if (journal.delete())
                System.out.println("Database journal deleted");
            else
                System.out.println("Failed to delete database journal");
        }
    }

    public static void showRoundedAlertDialog(final Context context, String title, String textToBeShown,
                                              boolean showPosBtn, boolean showNegBtn, String posText,
                                              String negText, final OnResponse response) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context,
                R.style.ThemeOverlay_Catalog_MaterialAlertDialog_OutlinedButton)
                .setTitle(title)
                .setMessage(textToBeShown)
                .setCancelable(false);
        if (showPosBtn) {
            builder.setPositiveButton(posText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    response.onResponse(true);
                }
            });
        }

        if (showNegBtn) {
            builder.setNegativeButton(negText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    response.onResponse(false);
                }
            });
        }

        alertDialog = builder.create();

        try {
            alertDialog.show();
        } catch (Exception e) {
            Log.e("AlertDialog ", e.getMessage());
        }

    }

    public Drawable getDrawable(int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.LOLLIPOP) {
            return ContextCompat.getDrawable(mContext, id);
        } else {
            return mContext.getResources().getDrawable(id);
        }
    }

    public String getReadableDate(String createdTs) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            originalFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.ENGLISH);
            Date date = originalFormat.parse(createdTs);
            return targetFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public String getHumanReadableDate(String createdTs) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            originalFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            Date date = originalFormat.parse(createdTs);
            return targetFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public String getHumanReadableDateTime(String createdTs) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH);
            targetFormat.setTimeZone(TimeZone.getDefault());
            Date date = originalFormat.parse(createdTs);
            return targetFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public int getWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        return width;
    }

    public int getHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        return height;
    }

    public void voidOpen(Activity activity, String title, String videoId, VideoListItem model,
                         CourseListItem courseListItem,
                         CategoryListItem categoryListItem, SubCategoryItem subCategoryItem,
                         boolean isLive) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity,
                R.style.ThemeOverlay_Catalog_MaterialAlertDialog_OutlinedButton_AlertDialog);

        View view = LayoutInflater.from(activity).inflate(R.layout.custom_recycler_view, null);
        ProgressBar progressBarVideoQuality = view.findViewById(R.id.progressBarVideoQuality);
        RecyclerView recyclerViewVideoQuality = view.findViewById(R.id.recyclerViewVideoQuality);
        recyclerViewVideoQuality.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));

        builder.setTitle("Select Player")
                .setMessage("")
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("Cancel", ((dialog, which) -> {
                    dialog.dismiss();
                }));

        AlertDialog materialAlertDialog = builder.create();
        materialAlertDialog.show();
        /* activity.runOnUiThread(() -> {*/
        List<VideoList> videoList = new ArrayList<>();
        VideoList videoListItem = new VideoList(
                "",
                "Player 1",
                "",
                "",
                videoId,
                ""
        );
        VideoList videoListItem1 = new VideoList(
                "",
                "Player 2",
                "",
                "",
                videoId,
                ""
        );
        videoList.add(videoListItem);
        videoList.add(videoListItem1);
        progressBarVideoQuality.setVisibility(View.GONE);
        AdapterVideoQ videoQ = new AdapterVideoQ(mContext, videoList, (position, key) -> {
            materialAlertDialog.dismiss();
            Intent intent = new Intent(mContext, YoutubeVideoActivity.class);
            if(position == 0) {
                try {
                    String[] url = model.getUrl().split("=");
                    intent.putExtra("videoId", url[1]);
                    intent.putExtra("isLive", isLive);
                    intent.putExtra("video", model);
                    intent.putExtra("playerType", 1);
                    intent.putExtra("course", courseListItem);
                    intent.putExtra("category", categoryListItem);
                    intent.putExtra("subCategory", subCategoryItem);
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    intent.putExtra("videoId", "");
                    intent.putExtra("isLive", isLive);
                    intent.putExtra("video", model);
                    intent.putExtra("playerType", 1);
                    intent.putExtra("course", courseListItem);
                    intent.putExtra("category", categoryListItem);
                    intent.putExtra("subCategory", subCategoryItem);
                    mContext.startActivity(intent);
                }
            } else {
                try {
                    String[] url = model.getUrl().split("=");
                    intent.putExtra("videoId", url[1]);
                    intent.putExtra("isLive", isLive);
                    intent.putExtra("video", model);
                    intent.putExtra("playerType", 2);
                    intent.putExtra("course", courseListItem);
                    intent.putExtra("category", categoryListItem);
                    intent.putExtra("subCategory", subCategoryItem);
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    intent.putExtra("videoId", "");
                    intent.putExtra("isLive", isLive);
                    intent.putExtra("video", model);
                    intent.putExtra("playerType", 2);
                    intent.putExtra("course", courseListItem);
                    intent.putExtra("category", categoryListItem);
                    intent.putExtra("subCategory", subCategoryItem);
                    mContext.startActivity(intent);
                }
            }
        });
        recyclerViewVideoQuality.setAdapter(videoQ);
        /*        });*/
    }

    public void voidOpenYoutube(Activity mContext, String title, String videoId, VideoListItem model) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext,
                R.style.ThemeOverlay_Catalog_MaterialAlertDialog_OutlinedButton_AlertDialog);

        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_recycler_view, null);
        ProgressBar progressBarVideoQuality = view.findViewById(R.id.progressBarVideoQuality);
        RecyclerView recyclerViewVideoQuality = view.findViewById(R.id.recyclerViewVideoQuality);
        recyclerViewVideoQuality.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));

        builder.setTitle("Select Quality")
                .setMessage("")
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("Cancel", ((dialog, which) -> {
                    dialog.dismiss();
                }));

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        new Thread(() -> {
            progressBarVideoQuality.setVisibility(View.VISIBLE);
            YoutubeJExtractor youtubeJExtractor = new YoutubeJExtractor();
            VideoPlayerConfig videoData = null;
            try {
                videoData = youtubeJExtractor.extract(videoId);
                if (Objects.requireNonNull(videoData.getVideoDetails()).isLiveContent()) {
                    if (Objects.requireNonNull(videoData.getStreamingData()).getHlsManifestUrl() != null) {
                        alertDialog.dismiss();
                        Intent intent = new Intent(mContext, PlayerActivity.class);
                        intent.putExtra("isLive", true);
                        intent.putExtra("pdfUrl", model.getPdfUrl());
                        intent.putExtra("chatId", model.getChatId());
                        intent.putExtra("chatEnabled", model.getChatEnabled());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("streaming", videoData.getStreamingData());
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    } else {
                        extractNonLive(mContext, videoData, recyclerViewVideoQuality,
                                progressBarVideoQuality, alertDialog, model);
                    }
                } else {
                    extractNonLive(mContext, videoData, recyclerViewVideoQuality,
                            progressBarVideoQuality, alertDialog, model);
                }
            } catch (ExtractionException | YoutubeRequestException e) {

                // Something really bad happened, nothing we can do except just show some error notification to the user
            } // Possibly there are some connection problems, ask user to check the internet connection and then retry
            catch (VideoIsUnavailable videoIsUnavailable) {
                progressBarVideoQuality.setVisibility(View.GONE);
                videoIsUnavailable.printStackTrace();
            }
        }).start();
    }

    private void extractNonLive(Activity activity,
                                VideoPlayerConfig videoData, RecyclerView recyclerView,
                                ProgressBar progressBar,
                                AlertDialog dialog, VideoListItem model) {
        ArrayList<VideoList> videoList = new ArrayList<VideoList>();
        if (videoData.getStreamingData().getAdaptiveVideoStreams().size() >
                videoData.getStreamingData().getMuxedStreams().size()) {
            for (int i = 0; i < videoData.getStreamingData().getAdaptiveVideoStreams().size(); i++) {

                AdaptiveVideoStream adaptiveVideoStream = videoData.getStreamingData().getAdaptiveVideoStreams().get(i);
                VideoList videoListItem = new VideoList(
                        adaptiveVideoStream.getUrl(),
                        adaptiveVideoStream.getQualityLabel(),
                        adaptiveVideoStream.getExtension(),
                        "",
                        "",
                        videoData.getStreamingData().getAdaptiveAudioStreams().get(0).getUrl()
                );
                boolean isAdded = false;
                for (int j = 0; j < videoList.size(); j++) {
                    if (videoList.get(j).getQuailty().equals(videoListItem.getQuailty())) {
                        isAdded = true;
                    }
                }
                if (!isAdded) {
                    videoList.add(videoListItem);
                }
            }
        } else {
            for (int i = 0; i < videoData.getStreamingData().getMuxedStreams().size(); i++) {

                MuxedStream adaptiveVideoStream = videoData.getStreamingData().getMuxedStreams().get(i);
                VideoList videoListItem = new VideoList(
                        adaptiveVideoStream.getUrl(),
                        adaptiveVideoStream.getQualityLabel(),
                        "",
                        "",
                        "",
                        ""
                );
                boolean isAdded = false;
                for (int j = 0; j < videoList.size(); j++) {
                    if (videoList.get(j).getQuailty().equals(videoListItem.getQuailty())) {
                        isAdded = true;
                    }
                }
                if (!isAdded) {
                    videoList.add(videoListItem);
                }
            }
        }

        activity.runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            AdapterVideoQ videoQ = new AdapterVideoQ(activity, videoList, (position, key) -> {
                dialog.dismiss();
                redirectToPlayer(activity, videoList.get(position).getUrl()
                        , videoList.get(position).getAudioUrl(), model);
            });
            recyclerView.setAdapter(videoQ);
        });
    }

    private void redirectToPlayer(Activity activity, String videoUrl, String audioUrl, VideoListItem model) {
        Intent intent = new Intent(activity, PlayerActivity.class);
        intent.putExtra("isLive", false);
        intent.putExtra("videoUrl", videoUrl);
        intent.putExtra("audioUrl", audioUrl);
        intent.putExtra("pdfUrl", model.getPdfUrl());
        intent.putExtra("chatId", model.getChatId());
        intent.putExtra("chatEnabled", model.getChatEnabled());
        activity.startActivity(intent);
    }

    public static String getPhoneInfo() {
        String phoneModel = Build.MODEL;
        String manufacturer = Build.MANUFACTURER;
        String androidSdkVersion = String.valueOf(Build.VERSION.SDK_INT);
        String androidVersion = String.valueOf(Build.VERSION.RELEASE);

        PhoneInformation phoneInformation
                = new PhoneInformation(phoneModel, manufacturer, androidVersion, androidSdkVersion);

        return new Gson().toJson(phoneInformation);
    }

    public static String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(),
                    null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }

    public static String getProgressDisplayLine(long currentBytes, long totalBytes) {
        return getBytesToMBString(currentBytes) + " / " + getBytesToMBString(totalBytes);
    }

    public static String getBytesToMBString(long bytes){
        return String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00));
    }

    public String getName(String url) {
        int slashIndex = url.lastIndexOf('/');
        int dotIndex = url.lastIndexOf('.');
        String filenameWithoutExtension;
        if (dotIndex == -1) {
            filenameWithoutExtension = url.substring(slashIndex + 1);
        } else {
            filenameWithoutExtension = url.substring(slashIndex + 1, dotIndex);
        }
        return filenameWithoutExtension;
    }

    public String getRootDirectory() {
        try {
            File pdfFolder = new File(mContext.getExternalFilesDir("Notes"), "pdf");
            if (!pdfFolder.exists()) {
                pdfFolder.mkdir();
            }
            return pdfFolder.getAbsolutePath();
        } catch (Exception e) {
            return "";
        }
    }


    public static String getQualityString(String quality) {
        if(quality.contains("144p")) {
            return "Low (144p)";
        } else if (quality.contains("240p")) {
            return "Normal (240p)";
        }else if (quality.contains("360p")) {
            return "Medium (360p)";
        }else if (quality.contains("480p")) {
            return "Mid-Normal (480p)";
        }else if (quality.contains("720p")) {
            return "High Definition (HD) (720p)";
        }else if (quality.contains("1080p")) {
            return "Full HD (1080p)";
        }else if (quality.contains("1440p")) {
            return "Ultra HD (1440p)";
        }else if (quality.contains("2160p")) {
            return "2K (2160p)";
        } else {
            return quality;
        }
    }

    @NonNull
    public static String getETAString(@NonNull final Context context, final long etaInMilliSeconds) {
        if (etaInMilliSeconds < 0) {
            return "";
        }
        int seconds = (int) (etaInMilliSeconds / 1000);
        long hours = seconds / 3600;
        seconds -= hours * 3600;
        long minutes = seconds / 60;
        seconds -= minutes * 60;
        if (hours > 0) {
            return context.getString(R.string.download_eta_hrs, hours, minutes, seconds);
        } else if (minutes > 0) {
            return context.getString(R.string.download_eta_min, minutes, seconds);
        } else {
            return context.getString(R.string.download_eta_sec, seconds);
        }
    }

    @NonNull
    public static String getDownloadSpeedString(@NonNull final Context context, final long downloadedBytesPerSecond) {
        if (downloadedBytesPerSecond < 0) {
            return "";
        }
        double kb = (double) downloadedBytesPerSecond / (double) 1000;
        double mb = kb / (double) 1000;
        final DecimalFormat decimalFormat = new DecimalFormat(".##");
        if (mb >= 1) {
            return context.getString(R.string.download_speed_mb, decimalFormat.format(mb));
        } else if (kb >= 1) {
            return context.getString(R.string.download_speed_kb, decimalFormat.format(kb));
        } else {
            return context.getString(R.string.download_speed_bytes, downloadedBytesPerSecond);
        }
    }

    @NonNull
    public static File createFile(String filePath) {
        final File file = new File(filePath);
        if (!file.exists()) {
            final File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static int getProgress(long downloaded, long total) {
        if (total < 1) {
            return -1;
        } else if (downloaded < 1) {
            return 0;
        } else if (downloaded >= total) {
            return 100;
        } else {
            return (int) (((double) downloaded / (double) total) * 100);
        }
    }

}
