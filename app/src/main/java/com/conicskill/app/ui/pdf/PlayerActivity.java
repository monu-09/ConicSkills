/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.conicskill.app.ui.pdf;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.conicskill.app.R;
import com.conicskill.app.base.BaseActivity;
import com.conicskill.app.util.ytextractor.DemoUtil;
import com.conicskill.app.util.ytextractor.TrackSelectionDialog;
import com.github.kotvertolet.youtubejextractor.models.newModels.VideoPlayerConfig;
import com.github.kotvertolet.youtubejextractor.models.youtube.videoData.StreamingData;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.DecoderInitializationException;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.ads.AdsLoader;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.StyledPlayerControlView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.util.ErrorMessageProvider;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;

import static com.google.android.exoplayer2.util.Assertions.checkNotNull;

/**
 * An activity that plays media using {@link SimpleExoPlayer}.
 */
public class PlayerActivity extends BaseActivity
        implements OnClickListener, StyledPlayerControlView.VisibilityListener {

    // Saved instance state keys.

    private static final String KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";

    protected StyledPlayerView playerView;
    //  protected LinearLayout debugRootView;
    //  protected TextView debugTextView;
    protected SimpleExoPlayer player;

    private boolean isShowingTrackSelectionDialog;
    // private Button selectTracksButton;
    private DataSource.Factory dataSourceFactory;
    private List<MediaItem> mediaItems;
    private DefaultTrackSelector trackSelector;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    // private DebugTextViewHelper debugViewHelper;
    private TrackGroupArray lastSeenTrackGroupArray;
    private boolean startAutoPlay;
    private int startWindow;
    private long startPosition;

    private boolean isLiveClass = false;
    private boolean isDownloadDone = false;
    // For ad playback only.
    private boolean OFFLINE = false;
    private AdsLoader adsLoader;
    private View controlView;
    private ImageView mFullScreenIcon;
    private Runnable runnableCode;
    private String live_classid = "";
    private EditText et_sendmesg;
    private String TAG = "vkm";
    private String videoUrl = "";
    private RecyclerView rv_chatlist;
    public VideoPlayerConfig videoPlayerConfig = new VideoPlayerConfig();
	public String audioUrl = "";
    private String pdfUrl = "";
    private String chatId = "";
    private String chatEnabled = "";

    @Override
    protected int layoutRes() {
        return R.layout.player_activity;
    }

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.pager)
    ViewPager viewPager2;

    private String[] titles = new String[]{"PDF for Class", "Live Chat"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        progressBar = findViewById(R.id.progressBar_player);
        dataSourceFactory = DemoUtil.getDataSourceFactory(/* context= */ this);
        playerView = findViewById(R.id.player_view);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
        playerView.setControllerVisibilityListener(this);
        playerView.setErrorMessageProvider(new PlayerErrorMessageProvider());
        playerView.requestFocus();
        Handler handler = new Handler(Looper.getMainLooper());



        controlView = playerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);

        isLiveClass = getIntent().getBooleanExtra("isLive", false);
        pdfUrl = getIntent().getStringExtra("pdfUrl");
        chatId = getIntent().getStringExtra("chatId");
        chatEnabled = getIntent().getStringExtra("chatEnabled");
        if (isLiveClass) {
            controlView.findViewById(R.id.live_key).setVisibility(View.VISIBLE);
            videoPlayerConfig.setStreamingData((StreamingData) getIntent().getExtras().getSerializable("streaming"));
        } else {
            audioUrl = getIntent().getStringExtra("audioUrl");
            videoUrl = getIntent().getStringExtra("videoUrl");
        }
        controlView.findViewById(R.id.exo_play).setOnClickListener(v -> startPlayer());
        controlView.findViewById(R.id.exo_pause).setOnClickListener(v -> pausePlayer());


        controlView.findViewById(R.id.exo_speedo).setOnClickListener(view -> {
            final String[] payback_speed = getResources().getStringArray(R.array.speeds);
            final String[] payback_value = getResources().getStringArray(R.array.speed_values);
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PlayerActivity.this);
            alertBuilder.setTitle("Select Playback Speed");
            alertBuilder.setItems(payback_speed, (dialog, which) -> {
                PlaybackParameters param = new PlaybackParameters(Float.parseFloat(payback_value[which]));
                // 1f is 1x, 2f is 2x
                if (player != null) {
                    player.setPlaybackParameters(param);
                }

            });
            AlertDialog dialog = alertBuilder.create();
            dialog.show();
        });
        controlView.findViewById(R.id.exo_ffwdmy).setOnClickListener(view -> {
            if (player != null) {
                long p = player.getCurrentPosition() + 10000;
                player.seekTo(p);

            }

        });
        controlView.findViewById(R.id.exo_rewmy).setOnClickListener(view -> {

            if (player != null) {
                if (player.getCurrentPosition() > 10000) {
                    long p = player.getCurrentPosition() - 10000;
                    player.seekTo(p);

                }
            }
        });
        controlView.findViewById(R.id.exo_quality_btn).setOnClickListener(view -> {
            if (!isShowingTrackSelectionDialog
                    && TrackSelectionDialog.willHaveContent(trackSelector)) {
                isShowingTrackSelectionDialog = true;
                TrackSelectionDialog trackSelectionDialog =
                        TrackSelectionDialog.createForTrackSelector(
                                trackSelector,
                                /* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false);
                trackSelectionDialog.show(getSupportFragmentManager(), /* tag= */ null);
            }
        });


        if (savedInstanceState != null) {
            trackSelectorParameters = savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS);
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            startWindow = savedInstanceState.getInt(KEY_WINDOW);
            startPosition = savedInstanceState.getLong(KEY_POSITION);
        } else {
            DefaultTrackSelector.ParametersBuilder builder =
                    new DefaultTrackSelector.ParametersBuilder(/* context= */ this);
            trackSelectorParameters = builder.build();
            clearStartPosition();
        }

        if(chatEnabled.equalsIgnoreCase("1") || pdfUrl != null) {
            viewPager2.setAdapter(new ViewPagerFragmentAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
            viewPager2.setOffscreenPageLimit(5);
            tabLayout.setupWithViewPager(viewPager2);
        } else {
            tabLayout.setVisibility(View.GONE);
        }

    }

    private Dialog mFullScreenDialog;
    private boolean mExoPlayerFullscreen = false;

    private void pausePlayer() {
        Log.e(TAG, "pausePlayer: ");
        controlView.findViewById(R.id.exo_pause).setVisibility(View.GONE);
        controlView.findViewById(R.id.exo_play).setVisibility(View.VISIBLE);
        player.setPlayWhenReady(false);
        player.getPlaybackState();
    }

    private void startPlayer() {
        Log.e(TAG, "startPlayer: ");
        controlView.findViewById(R.id.exo_pause).setVisibility(View.VISIBLE);
        controlView.findViewById(R.id.exo_play).setVisibility(View.GONE);
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }

    private void openFullscreenDialog() {
        // topview.setVisibility(View.GONE);
        ((ViewGroup) playerView.getParent()).removeView(playerView);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
        mFullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_exit_24dp));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    /**
     * Close player full screen
     */
    private void closeFullscreenDialog() {
        //  topview.setVisibility(View.VISIBLE);
        ((ViewGroup) playerView.getParent()).removeView(playerView);
        ((ConstraintLayout) findViewById(R.id.youtube_player_view)).addView(playerView);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_24dp));
    }


    public static final class PreventScreenshot {

        public PreventScreenshot() {
        }

        public static void on(Window window) {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }

        public static void on(Activity activity) {
            PreventScreenshot.on(activity.getWindow());
        }

        public void on(Fragment fragment) {
            PreventScreenshot.on(fragment.requireActivity());
        }

        public void on(WindowManager.LayoutParams layoutParams) {
            layoutParams.flags |= WindowManager.LayoutParams.FLAG_SECURE;
        }

        public void on(Dialog dialog) {
            on(Objects.requireNonNull(dialog.getWindow()));
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void on(SurfaceView surfaceView) {
            surfaceView.setSecure(true);
        }

    }

    private void initFullscreenDialog() {
        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            @Override
            public void onBackPressed() {
                if (mExoPlayerFullscreen) {
                    setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    closeFullscreenDialog();
                }
            }
        };
        PreventScreenshot preventScreenshot = new PreventScreenshot();
        preventScreenshot.on(mFullScreenDialog);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        releasePlayer();
        releaseAdsLoader();
        clearStartPosition();
        setIntent(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        } else {
            startPlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        } else {
            pausePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseAdsLoader();
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        updateTrackSelectorParameters();
        updateStartPosition();
        outState.putParcelable(KEY_TRACK_SELECTOR_PARAMETERS, trackSelectorParameters);
        outState.putBoolean(KEY_AUTO_PLAY, startAutoPlay);
        outState.putInt(KEY_WINDOW, startWindow);
        outState.putLong(KEY_POSITION, startPosition);
    }

    // Activity input

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // See whether the player view wants to handle media or DPAD keys events.
        return playerView.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    // OnClickListener methods

    @Override
    public void onClick(View view) {
       /* if (view == selectTracksButton
                && !isShowingTrackSelectionDialog
                && TrackSelectionDialog.willHaveContent(trackSelector)) {
            isShowingTrackSelectionDialog = true;
            TrackSelectionDialog trackSelectionDialog =
                    TrackSelectionDialog.createForTrackSelector(
                            trackSelector,
                            *//* onDismissListener= *//* dismissedDialog -> isShowingTrackSelectionDialog = false);
            trackSelectionDialog.show(getSupportFragmentManager(), *//* tag= *//* null);
        }*/
    }

    // PlayerControlView.VisibilityListener implementation

    @Override
    public void onVisibilityChange(int visibility) {
        // debugRootView.setVisibility(visibility);
    }

    // Internal methods
    private ProgressBar progressBar;


    /**
     */
    protected void initializePlayer() {
        MediaSourceFactory mediaSourceFactory = null;
        if (player == null) {
            Intent intent = getIntent();
            progressBar.setVisibility(View.VISIBLE);
//            mediaItems = createMediaItems(intent);
            /*if (mediaItems.isEmpty()) {
                return false;
            }
*/
            boolean preferExtensionDecoders = true/*intent.getBooleanExtra(IntentUtil.PREFER_EXTENSION_DECODERS_EXTRA, false)*/;
            RenderersFactory renderersFactory =
                    DemoUtil.buildRenderersFactory(/* context= */ this, preferExtensionDecoders);
            mediaSourceFactory =
                    new DefaultMediaSourceFactory(dataSourceFactory)
                            .setAdsLoaderProvider(this::getAdsLoader)
                            .setAdViewProvider(playerView);

            trackSelector = new DefaultTrackSelector(/* context= */ this);
            trackSelector.setParameters(trackSelectorParameters);
            lastSeenTrackGroupArray = null;
            player = new SimpleExoPlayer.Builder(/* context= */ this, renderersFactory)
                            .setMediaSourceFactory(mediaSourceFactory)
                            .setTrackSelector(trackSelector)
                            .build();
            player.addListener(new PlayerEventListener());
            player.addAnalyticsListener(new EventLogger(trackSelector));
            player.setAudioAttributes(AudioAttributes.DEFAULT, /* handleAudioFocus= */ true);
            player.setPlayWhenReady(startAutoPlay);
            playerView.setPlayer(player);
            // debugViewHelper = new DebugTextViewHelper(player, debugTextView);
            // debugViewHelper.start();
        }
        boolean haveStartPosition = startWindow != C.INDEX_UNSET;
        if (haveStartPosition) {
            player.seekTo(startWindow, startPosition);
        }
        if(isLiveClass) {
            Uri uri;
            if(videoPlayerConfig.getStreamingData().getHlsManifestUrl() != null) {
                uri = Uri.parse(videoPlayerConfig.getStreamingData().getHlsManifestUrl());
            } else {
                uri = Uri.parse(videoPlayerConfig.getStreamingData().getDashManifestUrl());
            }
            MediaItem mediaItem = MediaItem.fromUri(uri);
            player.setMediaItem(mediaItem);
        } else {
            if(!audioUrl.isEmpty()) {
                MediaItem videoItem = MediaItem.fromUri(videoUrl);
                MediaItem audioItem = MediaItem.fromUri(audioUrl);
                assert mediaSourceFactory != null;
                MediaSource videoSource = mediaSourceFactory.createMediaSource(videoItem);
                MediaSource audioSource = mediaSourceFactory.createMediaSource(audioItem);

                MergingMediaSource mergedSource = new MergingMediaSource(videoSource, audioSource);
                player.setMediaSource(mergedSource);
            } else {
                MediaItem mediaItem = MediaItem.fromUri(videoUrl);
                player.setMediaItem(mediaItem);
            }
        }
        player.prepare();
        initFullscreenDialog();
        initFullscreenButton();
        updateButtonVisibility();
    }

    private void initFullscreenButton() {
        FrameLayout mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(v -> {
            if (!mExoPlayerFullscreen) {
                setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                openFullscreenDialog();
            } else {
                setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                closeFullscreenDialog();
            }
        });
    }

    private void setScreenOrientation(int screenOrientationPortrait) {
        setRequestedOrientation(screenOrientationPortrait);
    }

    /*private List<MediaItem> createMediaItems(Intent intent) {
        String action = intent.getAction();
        boolean actionIsListView = IntentUtil.ACTION_VIEW_LIST.equals(action);
        if (!actionIsListView && !IntentUtil.ACTION_VIEW.equals(action)) {
            showToast(getString(R.string.unexpected_intent_action, action));
            finish();
            return Collections.emptyList();
        }

        List<MediaItem> mediaItems =
                createMediaItems(intent, DemoUtil.getDownloadTracker(*//* context= *//* this));
        boolean hasAds = false;
        for (int i = 0; i < mediaItems.size(); i++) {
            MediaItem mediaItem = mediaItems.get(i);

            if (!Util.checkCleartextTrafficPermitted(mediaItem)) {
                showToast(R.string.error_cleartext_not_permitted);
                finish();
                return Collections.emptyList();
            }
            if (Util.maybeRequestReadExternalStoragePermission(*//* activity= *//* this, mediaItem)) {
                // The player will be reinitialized if the permission is granted.
                return Collections.emptyList();
            }

            MediaItem.DrmConfiguration drmConfiguration =
                    checkNotNull(mediaItem.playbackProperties).drmConfiguration;
            if (drmConfiguration != null) {
                if (!FrameworkMediaDrm.isCryptoSchemeSupported(drmConfiguration.uuid)) {
                    showToast(R.string.error_drm_unsupported_scheme);
                    finish();
                    return Collections.emptyList();
                }
            }
            hasAds |= mediaItem.playbackProperties.adsConfiguration != null;
        }
        if (!hasAds) {
            releaseAdsLoader();
        }
        return mediaItems;
    }*/

    private AdsLoader getAdsLoader(MediaItem.AdsConfiguration adsConfiguration) {
        // The ads loader is reused for multiple playbacks, so that ad playback can resume.
        if (adsLoader == null) {
            adsLoader = new ImaAdsLoader.Builder(/* context= */ this).build();
        }
        adsLoader.setPlayer(player);
        return adsLoader;
    }

    protected void releasePlayer() {
        if (player != null) {
            updateTrackSelectorParameters();
            updateStartPosition();
            //   debugViewHelper.stop();
            //  debugViewHelper = null;
            player.release();
            player = null;
            mediaItems = Collections.emptyList();
            trackSelector = null;
        }
        if (adsLoader != null) {
            adsLoader.setPlayer(null);
        }
    }

    private void releaseAdsLoader() {
        if (adsLoader != null) {
            adsLoader.release();
            adsLoader = null;
            Objects.requireNonNull(playerView.getOverlayFrameLayout()).removeAllViews();
        }
    }

    private void updateTrackSelectorParameters() {
        if (trackSelector != null) {
            trackSelectorParameters = trackSelector.getParameters();
        }
    }

    private void updateStartPosition() {
        if (player != null) {
            startAutoPlay = player.getPlayWhenReady();
            startWindow = player.getCurrentWindowIndex();
            startPosition = Math.max(0, player.getContentPosition());
        }
    }

    protected void clearStartPosition() {
        startAutoPlay = true;
        startWindow = C.INDEX_UNSET;
        startPosition = C.TIME_UNSET;
    }

    // User controls

    private void updateButtonVisibility() {
//        selectTracksButton.setEnabled(
//                player != null && TrackSelectionDialog.willHaveContent(trackSelector));
    }

    private void showControls() {
        //   debugRootView.setVisibility(View.VISIBLE);
    }

    private void showToast(int messageId) {
        showToast(getString(messageId));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private String mPlayerStatus;
    static final String LOADING = "PLAYER_LOADING";
    static final String STOPPED = "PLAYER_STOPPED";
    static final String PAUSED = "PLAYER_PAUSED";
    static final String PLAYING = "PLAYER_PLAYING";
    static final String IDLE = "PLAYER_IDLE";

    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    private class PlayerEventListener implements Player.EventListener {

        @Override
        public void onPlaybackStateChanged(@Player.State int playbackState) {
            if (playbackState == Player.STATE_ENDED) {
                mPlayerStatus = STOPPED;
                showControls();
            }
            if (playbackState == Player.STATE_BUFFERING) {
                mPlayerStatus = LOADING;
                runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
            }
            if (playbackState == Player.STATE_READY) {
                // mPlayerStatus = ? PLAYING : PAUSED;
                runOnUiThread(() -> progressBar.setVisibility(View.INVISIBLE));
            }
            if (playbackState == Player.STATE_ENDED) {
                mPlayerStatus = STOPPED;
                runOnUiThread(() -> progressBar.setVisibility(View.INVISIBLE));
            }

            updateButtonVisibility();
        }

        @Override
        public void onPlayerError(@NonNull ExoPlaybackException e) {
            if (isBehindLiveWindow(e)) {
                clearStartPosition();
                initializePlayer();
            } else {
                updateButtonVisibility();
                showControls();
            }
        }

        @Override
        @SuppressWarnings("ReferenceEquality")
        public void onTracksChanged(
                @NonNull TrackGroupArray trackGroups, @NonNull TrackSelectionArray trackSelections) {
            updateButtonVisibility();
            if (trackGroups != lastSeenTrackGroupArray) {
                MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                            == MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        showToast(R.string.error_unsupported_video);
                    }
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_AUDIO)
                            == MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        showToast(R.string.error_unsupported_audio);
                    }
                }
                lastSeenTrackGroupArray = trackGroups;
            }
        }
    }

    private class PlayerErrorMessageProvider implements ErrorMessageProvider<ExoPlaybackException> {

        @Override
        @NonNull
        public Pair<Integer, String> getErrorMessage(@NonNull ExoPlaybackException e) {
            String errorString = getString(R.string.error_generic);
            if (e.type == ExoPlaybackException.TYPE_RENDERER) {
                Exception cause = e.getRendererException();
                if (cause instanceof DecoderInitializationException) {
                    // Special case for decoder initialization failures.
                    DecoderInitializationException decoderInitializationException =
                            (DecoderInitializationException) cause;
                    if (decoderInitializationException.codecInfo == null) {
                        if (decoderInitializationException.getCause() instanceof DecoderQueryException) {
                            errorString = getString(R.string.error_querying_decoders);
                        } else if (decoderInitializationException.secureDecoderRequired) {
                            errorString =
                                    getString(
                                            R.string.error_no_secure_decoder, decoderInitializationException.mimeType);
                        } else {
                            errorString =
                                    getString(R.string.error_no_decoder, decoderInitializationException.mimeType);
                        }
                    } else {
                        errorString =
                                getString(
                                        R.string.error_instantiating_decoder,
                                        decoderInitializationException.codecInfo.name);
                    }
                }
            }
            return Pair.create(0, errorString);
        }
    }

    /*private static List<MediaItem> createMediaItems(Intent intent, DownloadTracker downloadTracker) {
        List<MediaItem> mediaItems = new ArrayList<>();
        for (MediaItem item : IntentUtil.createMediaItemsFromIntent(intent)) {
            @Nullable
            DownloadRequest downloadRequest =
                    downloadTracker.getDownloadRequest(checkNotNull(item.playbackProperties).uri);
            if (downloadRequest != null) {
                MediaItem.Builder builder = item.buildUpon();
                builder
                        .setMediaId(downloadRequest.id)
                        .setUri(downloadRequest.uri)
                        .setCustomCacheKey(downloadRequest.customCacheKey)
                        .setMimeType(downloadRequest.mimeType)
                        .setStreamKeys(downloadRequest.streamKeys)
                        .setDrmKeySetId(downloadRequest.keySetId)
                        .setDrmLicenseRequestHeaders(getDrmRequestHeaders(item));

                mediaItems.add(builder.build());
            } else {
                mediaItems.add(item);
            }
        }
        return mediaItems;
    }*/

    @Nullable
    private static Map<String, String> getDrmRequestHeaders(MediaItem item) {
        MediaItem.DrmConfiguration drmConfiguration = Objects.requireNonNull(item.playbackProperties).drmConfiguration;
        return drmConfiguration != null ? drmConfiguration.requestHeaders : null;
    }

    private class ViewPagerFragmentAdapter extends FragmentStatePagerAdapter {

        public ViewPagerFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                /*case 0:
                    return PdfFragment.newInstance(pdfUrl, "youtube");*/
                default:
                    return ChatFragment.newInstance(chatId, chatEnabled);
            }
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }


}
