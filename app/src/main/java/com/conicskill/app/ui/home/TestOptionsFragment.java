package com.conicskill.app.ui.home;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.conicskill.app.BuildConfig;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.FeatureModel;
import com.conicskill.app.data.model.candidateLogin.CandidateLoginRequest;
import com.conicskill.app.data.model.candidateLogin.CandidateLoginRequestData;
import com.conicskill.app.data.model.carousel.DisplayData;
import com.conicskill.app.data.model.carousel.HomeCarouselRequest;
import com.conicskill.app.data.model.carousel.HomeDataMain;
import com.conicskill.app.data.model.carousel.RequestData;
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsRequest;
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsRequestData;
import com.conicskill.app.data.model.home.HomeLayoutRequest;
import com.conicskill.app.data.model.home.LayoutItem;
import com.conicskill.app.data.model.videoCouses.CourseListItem;
import com.conicskill.app.data.model.videoCouses.VideoListItem;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.ui.CourseDetailActivity;
import com.conicskill.app.ui.HomeActivity;
import com.conicskill.app.ui.SplashActivity;
import com.conicskill.app.ui.courseDetail.CourseHorizontalAdapter;
import com.conicskill.app.ui.currentAffairs.CurrentAffairsFragment;
import com.conicskill.app.ui.help.HelpFragment;
import com.conicskill.app.ui.login.LoginViewModel;
import com.conicskill.app.ui.news.NewsCategoryFragment;
import com.conicskill.app.ui.news.YoutubeVideoFragment;
import com.conicskill.app.util.Constants;
import com.conicskill.app.util.GlideApp;
import com.conicskill.app.util.StartSnapHelper;
import com.conicskill.app.util.Utils;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.idanatz.oneadapter.OneAdapter;
import com.idanatz.oneadapter.external.event_hooks.ClickEventHook;
import com.idanatz.oneadapter.external.interfaces.Item;
import com.idanatz.oneadapter.external.modules.ItemModule;
import com.idanatz.oneadapter.external.modules.ItemModuleConfig;
import com.idanatz.oneadapter.external.modules.PagingModule;
import com.idanatz.oneadapter.external.modules.PagingModuleConfig;
import com.idanatz.oneadapter.internal.holders.ViewBinder;
import com.synnapps.carouselview.CarouselView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class TestOptionsFragment extends BaseFragment {

    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.recyclerViewFeatures)
    RecyclerView recyclerViewFeatures;
    @BindView(R.id.chipFacebook)
    Chip chipFacebook;
    @BindView(R.id.chipYoutube)
    Chip chipYoutube;
    @BindView(R.id.chipTelegram)
    Chip chipTelegram;
    private HomeViewModel mHomeViewModel;
    private LoginViewModel mLoginViewModel;
    private OneAdapter oneAdapter;
    private CourseHorizontalAdapter courseHorizontalAdapterUpcoming;
    private ArrayList<VideoListItem> upcomingVideoListItem = new ArrayList<>();


    public static TestOptionsFragment newInstance() {
        return new TestOptionsFragment();
    }

    @Override
    protected int layoutRes() {
        return R.layout.layout_test_options;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mHomeViewModel = new ViewModelProvider(this, viewModelFactory).get(HomeViewModel.class);
        mLoginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);
        recyclerViewFeatures.setVisibility(View.VISIBLE);
        ArrayList<FeatureModel> featureList = new ArrayList();
        FeatureModel model = new FeatureModel();
        model.setId(1);
        model.setBackgroundDrawable(R.drawable.drawable_moonlight);
        model.setFeatureIcon(R.drawable.ic_library_books_white_24dp);
        model.setFeatureName(R.string.text_mock_test_new);


        FeatureModel modelSubjectTests = new FeatureModel();
        modelSubjectTests.setId(2);
        modelSubjectTests.setBackgroundDrawable(R.drawable.drawable_moonlight);
        modelSubjectTests.setFeatureIcon(R.drawable.ic_baseline_surround_sound_24);
        modelSubjectTests.setFeatureName(R.string.text_free_tests);

        FeatureModel modelTopicTests = new FeatureModel();
        modelTopicTests.setId(3);
        modelTopicTests.setBackgroundDrawable(R.drawable.drawable_moonlight);
        modelTopicTests.setFeatureIcon(R.drawable.ic_aspect_ratio_white_24dp);
        modelTopicTests.setFeatureName(R.string.text_topic_tests);

        FeatureModel modelDailyQuizzes = new FeatureModel();
        modelDailyQuizzes.setId(4);
        modelDailyQuizzes.setBackgroundDrawable(R.drawable.drawable_moonlight);
        modelDailyQuizzes.setFeatureIcon(R.drawable.ic_today_white_24dp);
        modelDailyQuizzes.setFeatureName(R.string.text_daily_quizzes);

        FeatureModel currentAffairs = new FeatureModel();
        currentAffairs.setId(5);
        currentAffairs.setBackgroundDrawable(R.drawable.drawable_moonlight);
        currentAffairs.setFeatureIcon(R.drawable.ic_trending_up_white_24dp);
        currentAffairs.setFeatureName(R.string.text_current_affairs);

        FeatureModel latestjobs = new FeatureModel();
        latestjobs.setId(7);
        latestjobs.setBackgroundDrawable(R.drawable.drawable_moonlight);
        latestjobs.setFeatureIcon(R.drawable.ic_timeline_white_24dp);
        latestjobs.setFeatureName(R.string.text_latest_jobs);

        FeatureModel instituteUpdates = new FeatureModel();
        instituteUpdates.setId(8);
        instituteUpdates.setBackgroundDrawable(R.drawable.drawable_moonlight);
        instituteUpdates.setFeatureIcon(R.drawable.ic_school_white_24dp);
        instituteUpdates.setFeatureName(R.string.text_institute_updates);

        FeatureModel videos = new FeatureModel();
        videos.setId(9);
        videos.setBackgroundDrawable(R.drawable.drawable_moonlight);
        videos.setFeatureIcon(R.drawable.ic_videocam_white_24dp);
        videos.setFeatureName(R.string.text_videos);

        FeatureModel newspaper = new FeatureModel();
        newspaper.setId(10);
        newspaper.setBackgroundDrawable(R.drawable.drawable_moonlight);
        newspaper.setFeatureIcon(R.drawable.ic_today_white_24dp);
        newspaper.setFeatureName(R.string.text_newspaper);

        FeatureModel about = new FeatureModel();
        about.setId(11);
        about.setBackgroundDrawable(R.drawable.drawable_moonlight);
        about.setFeatureIcon(R.drawable.ic_help_outline_white_24dp);
        about.setFeatureName(R.string.text_contact_us);

        FeatureModel questionDay = new FeatureModel();
        questionDay.setId(12);
        questionDay.setBackgroundDrawable(R.drawable.drawable_moonlight);
        questionDay.setFeatureIcon(R.drawable.ic_dashboard_white_24dp);
        questionDay.setFeatureName(R.string.text_question_of_the_day);

        FeatureModel videoClasses = new FeatureModel();
        videoClasses.setId(13);
        videoClasses.setBackgroundDrawable(R.drawable.drawable_moonlight);
        videoClasses.setFeatureIcon(R.drawable.ic_videocam_white_24dp);
        videoClasses.setFeatureName(R.string.text_video_classes);

        FeatureModel notes = new FeatureModel();
        notes.setId(14);
        notes.setBackgroundDrawable(R.drawable.drawable_moonlight);
        notes.setFeatureIcon(R.drawable.ic_library_books_white_24dp);
        notes.setFeatureName(R.string.text_notes);

        FeatureModel downloads = new FeatureModel();
        downloads.setId(15);
        downloads.setBackgroundDrawable(R.drawable.drawable_moonlight);
        downloads.setFeatureIcon(R.drawable.ic_download);
        downloads.setFeatureName(R.string.text_downloads);

        FeatureModel help = new FeatureModel();
        help.setId(16);
        help.setBackgroundDrawable(R.drawable.drawable_moonlight);
        help.setFeatureIcon(R.drawable.ic_help_outline_white_24dp);
        help.setFeatureName(R.string.text_help);


        FeatureModel facebook = new FeatureModel();
        facebook.setId(17);
        facebook.setBackgroundDrawable(R.drawable.drawable_moonlight);
        facebook.setFeatureIcon(R.drawable.ic_facebook);
        facebook.setFeatureName(R.string.text_facebook);

        FeatureModel telegram = new FeatureModel();
        telegram.setId(18);
        telegram.setBackgroundDrawable(R.drawable.drawable_moonlight);
        telegram.setFeatureIcon(R.drawable.ic_telegram_app);
        telegram.setFeatureName(R.string.text_telegram);

        FeatureModel youtube = new FeatureModel();
        youtube.setId(19);
        youtube.setBackgroundDrawable(R.drawable.drawable_moonlight);
        youtube.setFeatureIcon(R.drawable.ic_youtube);
        youtube.setFeatureName(R.string.text_youtube);


        featureList.add(currentAffairs);
//        featureList.add(modelDailyQuizzes);
        featureList.add(latestjobs);
        featureList.add(newspaper);
//        featureList.add(model);
//        featureList.add(modelSubjectTests);
//        featureList.add(notes);
//        featureList.add(facebook);
//        featureList.add(youtube);
//        featureList.add(telegram);
//        featureList.add(downloads);
//        featureList.add(help);

//        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewFeatures.setLayoutManager(layoutManager);

        recyclerViewFeatures.setItemAnimator(null);
        oneAdapter = new OneAdapter(recyclerViewFeatures)
                .attachItemModule(featureItems().addEventHook(clickEventHook()));

        oneAdapter.setItems(featureList);

         chipFacebook.setOnClickListener(v -> {
             Intent i = new Intent(Intent.ACTION_VIEW);
             i.setData(Uri.parse(BuildConfig.FACEBOOK_DOMAIN));
             startActivity(i);
         });

         chipYoutube.setOnClickListener(v -> {
             Intent i = new Intent(Intent.ACTION_VIEW);
             i.setData(Uri.parse("https://www.youtube.com/channel/" + BuildConfig.YOUTUBE_CHANNEL_ID));
             startActivity(i);
         });

        chipTelegram.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=" + BuildConfig.TELEGRAM_LINK_DOMAIN));
                startActivity(intent);
            } catch (Exception e) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=org.telegram.messenger"));
                startActivity(intent);
            }
        });
    }


    @NotNull
    private ItemModule<FeatureModel> featureItems() {
        return new ItemModule<FeatureModel>() {
            @NotNull
            @Override
            public ItemModuleConfig provideModuleConfig() {
                return new ItemModuleConfig() {
                    @Override
                    public int withLayoutResource() {
                        return R.layout.item_study_material;
                    }

                    @Nullable
                    @Override
                    public Animator withFirstBindAnimation() {
                        // can be implemented by inflating Animator Xml
                        return null;
                    }
                };
            }

            @Override
            public void onBind(@NotNull Item<FeatureModel> item, @NotNull ViewBinder viewBinder) {
                CircleImageView imageViewStudy = viewBinder.findViewById(R.id.imageViewStudy);
                CardView cardView = viewBinder.findViewById(R.id.cardView);
                AppCompatTextView featureName = viewBinder.findViewById(R.id.featureName);
                featureName.setText(item.getModel().getFeatureName());

                imageViewStudy.setImageResource(item.getModel().getFeatureIcon());

                cardView.setOnClickListener(v ->
                    setOnClickListener(item.getModel())
                );
            }
        };
    }

    private void setOnClickListener(FeatureModel model) {
        if (model.getId() == 1) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack("homeFragment")
                    .replace(R.id.screenContainer, MockTestFragment.newInstance(null, "class"))
                    .commit();
        } else if (model.getId() == 2) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.screenContainer, MockTestFragment.newInstance("1", "live"))
                    .commit();
        } else if (model.getId() == 4) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack("homeFragment")
                    .replace(R.id.screenContainer, MockTestFragment.newInstance("100", "quiz"))
                    .commit();
        } else if (model.getId() == 11) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=" + BuildConfig.TELEGRAM_LINK_DOMAIN));
                startActivity(intent);
            } catch (Exception e) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=org.telegram.messenger"));
                startActivity(intent);
            }
        } else if (model.getId() == 10) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack("newsFragment")
                    .replace(R.id.screenContainer, NewsCategoryFragment.newInstance())
                    .commit();
        } else if (model.getId() == 5) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack("currentFragment")
                    .replace(R.id.screenContainer, CurrentAffairsFragment.newInstance())
                    .commit();
        } else if (model.getId() == 12) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack("wordFragment")
                    .replace(R.id.screenContainer, WordFragment.newInstance(null))
                    .commit();
        } else if (model.getId() == 9) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack("youtubeFragment")
                    .replace(R.id.screenContainer, YoutubeVideoFragment.newInstance())
                    .commit();
        } else if (model.getId() == 13) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack("videoListingFragment")
                    .replace(R.id.screenContainer, VideoCourseListingFragment.newInstance())
                    .commit();
        } else if (model.getId() == 7) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack("videoListingFragment")
                    .replace(R.id.screenContainer, JobNotificationFragment.Companion.newInstance())
                    .commit();
        }else if (model.getId() == 15) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack("videoListingFragment")
                    .replace(R.id.screenContainer, DownloadFragment.Companion.newInstance())
                    .commit();
        }
        else if (model.getId() == 16) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack("videoListingFragment")
                    .replace(R.id.screenContainer, HelpFragment.Companion.newInstance())
                    .commit();
        } else if(model.getId() == 18) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=" + BuildConfig.TELEGRAM_LINK_DOMAIN));
                startActivity(intent);
            } catch (Exception e) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=org.telegram.messenger"));
                startActivity(intent);
            }
        } else if(model.getId() == 17) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(BuildConfig.TELEGRAM_LINK_DOMAIN));
            startActivity(i);
        } else if(model.getId() == 19) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://www.youtube.com/channel/" + BuildConfig.YOUTUBE_CHANNEL_ID));
            startActivity(i);
        }

    }

    @NotNull
    private ClickEventHook<FeatureModel> clickEventHook() {
        return new ClickEventHook<FeatureModel>() {
            @Override
            public void onClick(@NotNull FeatureModel model, @NotNull ViewBinder viewBinder) {
                setOnClickListener(model);
            }
        };
    }
}
