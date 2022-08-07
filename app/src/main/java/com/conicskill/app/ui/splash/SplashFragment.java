package com.conicskill.app.ui.splash;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.lifecycle.ViewModelProvider;

import com.conicskill.app.BuildConfig;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.ui.HomeActivity;
import com.conicskill.app.ui.LoginActivity;
import com.conicskill.app.ui.PaymentActivity;
import com.conicskill.app.util.CommonView;
import com.conicskill.app.util.Constants;
import com.conicskill.app.util.OnResponse;
import com.conicskill.app.util.Utils;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;

public class SplashFragment extends BaseFragment {

    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.videoViewPost)
    PlayerView videoViewPost;
    private SplashViewModel mViewModel;
    private SimpleExoPlayer simplePlayer = null;

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    public static Uri getUriFromRawFile(Context context, @RawRes int rawResourceId) {
        return new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(context.getPackageName())
                .path(String.valueOf(rawResourceId))
                .build();
    }

    @Override
    protected int layoutRes() {
        return R.layout.splash_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(SplashViewModel.class);
        //initializePlayer();
        observeViewModel();
    }

    private void initializePlayer() {
        /** simplePlayer = new SimpleExoPlayer.Builder(requireContext()).build();
         DataSpec dataSpec = new DataSpec(RawResourceDataSource.buildRawResourceUri(R.raw.ic_promo));
         final RawResourceDataSource rawResourceDataSource = new RawResourceDataSource(requireContext());
         try {
         rawResourceDataSource.open(dataSpec);
         } catch (RawResourceDataSource.RawResourceDataSourceException e) {
         e.printStackTrace();
         }

         assert rawResourceDataSource.getUri() != null;
         MediaItem mediaItem = MediaItem.fromUri(rawResourceDataSource.getUri());
         DataSource.Factory factory = () -> rawResourceDataSource;
         ProgressiveMediaSource mediaSource = new ProgressiveMediaSource
         .Factory(factory)
         .createMediaSource(mediaItem);
         videoViewPost.setPlayer(simplePlayer);
         videoViewPost.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
         simplePlayer.setMediaSource(mediaSource);
         simplePlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
         simplePlayer.prepare();
         simplePlayer.setPlayWhenReady(true);*/
    }

    private void observeViewModel() {
        mViewModel.observeUpdateCheck().observe(getViewLifecycleOwner(), checkUpdateResponseResponse -> {
            if (checkUpdateResponseResponse != null && checkUpdateResponseResponse.body() != null) {
                if (checkUpdateResponseResponse.body().getCode() == HttpsURLConnection.HTTP_OK) {
                    if (Integer.parseInt(Objects.requireNonNull(checkUpdateResponseResponse.body().getData()).getLatestVersion()) > BuildConfig.VERSION_CODE) {
                        // show update
                        if (checkUpdateResponseResponse.body().getData().getForceUpdate().equalsIgnoreCase("1")) {
                            // show force update popup
                            showForceUpdate();
                        } else {
                            // show update popup
                            showUpdate();
                        }
                    } else {
                        // redirect to home screen
                        redirectToHomeScreen();
                    }
                }
            } else {
                // redirect to home screen
                redirectToHomeScreen();
            }
        });

        mViewModel.getError().observe(getViewLifecycleOwner(), isError -> {
            if(isError != null) {
                redirectToHomeScreen();
            }
        });
    }

    private void showForceUpdate() {
        CommonView.INSTANCE.showRoundedAlertDialog(requireContext(), "Update available",
                "Please update the app in order to continue", "Update", "Exit",
                result -> {
                    if ((boolean) result) {
                        redirectToPlayStore();
                        requireActivity().finish();
                    } else {
                        requireActivity().finish();
                    }
                });
    }

    private void showUpdate() {
        CommonView.INSTANCE.showRoundedAlertDialog(requireContext(), "Update available",
                "Please update the app in order to continue", "Update",
                "Continue to App",
                result -> {
                    if ((boolean) result) {
                        redirectToPlayStore();
                        requireActivity().finish();
                    } else {
                        redirectToHomeScreen();
                    }
                });
    }

    private void redirectToHomeScreen() {
        if (mViewModel.mTinyDB.getBoolean(Constants.IS_LOGGED_IN)) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void redirectToPlayStore() {
        final String appPackageName = requireActivity().getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}