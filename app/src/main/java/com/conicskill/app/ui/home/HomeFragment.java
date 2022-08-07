package com.conicskill.app.ui.home;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import com.conicskill.app.ui.login.LoginViewModel;
import com.conicskill.app.ui.news.NewsCategoryFragment;
import com.conicskill.app.ui.news.YoutubeVideoFragment;
import com.conicskill.app.util.Constants;
import com.conicskill.app.util.GlideApp;
import com.conicskill.app.util.StartSnapHelper;
import com.conicskill.app.util.Utils;
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
import es.dmoral.toasty.Toasty;

public class HomeFragment extends BaseFragment {

    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.carouselViewHome)
    CarouselView carouselViewHome;
    @BindView(R.id.carouselViewQuestion)
    CarouselView carouselViewQuestion;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.recyclerViewFeatures)
    RecyclerView recyclerViewFeatures;
    @BindView(R.id.recyclerViewUpcoming)
    RecyclerView recyclerViewUpcoming;
    @BindView(R.id.textViewNoUpcomingClasses)
    AppCompatTextView textViewNoUpcomingClasses;
    @BindView(R.id.textWelcome)
    AppCompatTextView textWelcome;
    @BindView(R.id.recyclerViewCourses)
    RecyclerView recyclerViewCourses;
    private HomeViewModel mHomeViewModel;
    private LoginViewModel mLoginViewModel;
    private OneAdapter oneAdapter;
    private CourseHorizontalAdapter courseHorizontalAdapterUpcoming;
    private ArrayList<VideoListItem> upcomingVideoListItem = new ArrayList<>();


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int layoutRes() {
        return R.layout.home_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mHomeViewModel = new ViewModelProvider(this, viewModelFactory).get(HomeViewModel.class);
        mLoginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);
        textWelcome.setText("Welcome, " + mHomeViewModel.tinyDB.getString(Constants.NAME) + "   ");

        recyclerViewFeatures.setVisibility(View.GONE);
        observeViewModel();
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
        newspaper.setFeatureIcon(R.drawable.ic_chrome_reader_mode_white_24dp);
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


        featureList.add(currentAffairs);
        featureList.add(modelDailyQuizzes);
        featureList.add(latestjobs);
        featureList.add(newspaper);
        featureList.add(model);
        featureList.add(modelSubjectTests);
        featureList.add(notes);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);

        /*layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 3;
                } else if (position < 4) {
                    return 3;
                } else {
                    return 2;
                }
                *//*if (position < 4) {
                    return 3;
                } else {
                    return 2;
                }*//*
            }
        });*/

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewFeatures.setLayoutManager(layoutManager);

        recyclerViewFeatures.setItemAnimator(null);
        oneAdapter = new OneAdapter(recyclerViewFeatures)
                .attachItemModule(featureItems().addEventHook(clickEventHook()));

        oneAdapter.setItems(featureList);


        RequestData requestData = new RequestData();
        requestData.setCompanyId(BuildConfig.COMPANY_ID);
        HomeCarouselRequest homeCarouselRequest = new HomeCarouselRequest();
        homeCarouselRequest.setToken(mHomeViewModel.tinyDB.getString(Constants.AUTH_TOKEN));
        homeCarouselRequest.setData(requestData);

//        observeHomeAPI();
        new StartSnapHelper().attachToRecyclerView(recyclerViewUpcoming);
        mHomeViewModel.callFetchHomeCarousel(homeCarouselRequest);

        CurrentAffairsRequestData currentAffairsRequestData = new CurrentAffairsRequestData();
        currentAffairsRequestData.setCompanyId(BuildConfig.COMPANY_ID);

        CurrentAffairsRequest currentAffairsRequest = new CurrentAffairsRequest();
        currentAffairsRequest.setToken(mHomeViewModel.tinyDB.getString(Constants.AUTH_TOKEN));
        currentAffairsRequest.setCurrentAffairsRequestData(currentAffairsRequestData);
//        mHomeViewModel.callFetchCourseVideo(currentAffairsRequest);

        HomeLayoutRequest homeLayoutRequest = new HomeLayoutRequest(new ArrayList<>(), mHomeViewModel.tinyDB.getString(Constants.AUTH_TOKEN));
        mHomeViewModel.callHomeCoursesApi(homeLayoutRequest);
    }

    public void observeViewModel() {
        mHomeViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });

        mHomeViewModel.getError().observe(getViewLifecycleOwner(), isError -> {
            if (isError != null) {
                if (isError) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        mHomeViewModel.getHomeCarousel().observe(getViewLifecycleOwner(), homeCarouselResponse -> {
            if (homeCarouselResponse.body().getCode() == HttpsURLConnection.HTTP_OK) {
                if (homeCarouselResponse.body().getData().size() != 0) {
                    List<HomeDataMain> dataMainList = new ArrayList<>();
                    List<HomeDataMain> questionArrayList = new ArrayList<>();
                    for (int i = 0; i < homeCarouselResponse.body().getData().size(); i++) {
                        if (homeCarouselResponse.body().getData().get(i).getType().equalsIgnoreCase("question")) {
                            questionArrayList.add(homeCarouselResponse.body().getData().get(i));
                        } else if (homeCarouselResponse.body().getData().get(i).getType().equalsIgnoreCase("word")) {
                            questionArrayList.add(homeCarouselResponse.body().getData().get(i));
                        } else {
                            dataMainList.add(homeCarouselResponse.body().getData().get(i));
                        }
                    }
                    setCarouselViewHome(dataMainList);
//                    setCarouselViewQuestion(questionArrayList);
                }
            }
        });

        mLoginViewModel.checkLogin().observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                if (response.body().getCode() == HttpsURLConnection.HTTP_OK) {
                    mLoginViewModel.tinyDB.putString(Constants.AUTH_TOKEN, response.body().getCandidateLoginData().getAuthToken());
                    mLoginViewModel.tinyDB.putString(Constants.NAME, response.body().getCandidateLoginData().getName());
                    mLoginViewModel.tinyDB.putString(Constants.EMAIL, response.body().getCandidateLoginData().getEmail());
                    mLoginViewModel.tinyDB.putBoolean(Constants.IS_LOGGED_IN, true);
                    Intent intent = new Intent(getContext(), HomeActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else if (response.body().getCode() == 5002) {
                    Toasty.error(getContext(), getString(R.string.err_incorrect_use_pass_login_again), Toast.LENGTH_SHORT, false).show();
//                    Utils.deleteDatabaseFile(getContext(), DatabaseConstant.DATABASE_NAME);
                    mLoginViewModel.tinyDB.putString(Constants.NAME, "");
                    mLoginViewModel.tinyDB.putString(Constants.EMAIL, "");
                    mLoginViewModel.tinyDB.putBoolean(Constants.IS_LOGGED_IN, false);
                    Intent intent = new Intent(getContext(), SplashActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else if (response.body().getCode() == 5017) {
                    Toasty.error(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT, false).show();
//                    Utils.deleteDatabaseFile(getContext(), DatabaseConstant.DATABASE_NAME);
                    mLoginViewModel.tinyDB.putString(Constants.NAME, "");
                    mLoginViewModel.tinyDB.putString(Constants.EMAIL, "");
                    mLoginViewModel.tinyDB.putBoolean(Constants.IS_LOGGED_IN, false);
                    Intent intent = new Intent(getContext(), SplashActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toasty.error(getContext(), getString(R.string.err_incorrect_use_pass_login_again), Toast.LENGTH_SHORT, false).show();
                }
            } else {
                Toasty.error(getContext(), getString(R.string.title_no_internet), Toast.LENGTH_SHORT, false).show();
            }
        });

        mHomeViewModel.getHomeLayoutResponse().observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.body() != null) {
                if (response.body().getCode() == HttpsURLConnection.HTTP_OK) {
                    List<LayoutItem> layoutItems = new ArrayList<>();
                    for (int i = 0; i < response.body().getData().getLayout().size(); i++) {
                        if (response.body().getData().getLayout().get(i).getContent() != null && response.body().getData().getLayout().get(i).getContent().size() > 0) {
                            layoutItems.add(response.body().getData().getLayout().get(i));
                        }
                    }

                    recyclerViewCourses.setLayoutManager(new LinearLayoutManager(getContext(),
                            RecyclerView.VERTICAL, false));

                    /*OneAdapter oneAdapter = new OneAdapter(recyclerViewCourses)
                            .attachItemModule(layoutItems());
                    oneAdapter.setItems(layoutItems);*/

                    recyclerViewFeatures.setVisibility(View.GONE);
                } else if (response.body().getCode() == 5005 || response.body().getCode() == 510) {
                    CandidateLoginRequestData data = new CandidateLoginRequestData();
                    data.setPassword(mHomeViewModel.tinyDB.getString(Constants.USER_PASSWORD));
                    data.setMobile(mHomeViewModel.tinyDB.getString(Constants.USER_NAME));
                    data.setNotificationToken(mHomeViewModel.tinyDB.getString(Constants.FCM_TOKEN));
                    data.setAppVersion(BuildConfig.VERSION_CODE);
                    data.setAdditionalInfo(Utils.getPhoneInfo());
                    data.setLoginVia(1);
                    data.setCompanyId(BuildConfig.COMPANY_ID);

                    CandidateLoginRequest request = new CandidateLoginRequest();
                    request.setCandidateLoginRequestData(data);
                    mLoginViewModel.callLoginApi(request);
                } else if (response.body().getCode() == 5030) {
                    Toasty.error(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT, false).show();
//                    Utils.deleteDatabaseFile(getContext(), DatabaseConstant.DATABASE_NAME);
                    mLoginViewModel.tinyDB.putString(Constants.NAME, "");
                    mLoginViewModel.tinyDB.putString(Constants.EMAIL, "");
                    mLoginViewModel.tinyDB.putBoolean(Constants.IS_LOGGED_IN, false);
                    Intent intent = new Intent(getContext(), SplashActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
    }

    private void setCarouselViewHome(List<HomeDataMain> response) {
        OneAdapter oneAdapter = new OneAdapter(recyclerViewCourses)
                .attachItemModule(carouselDataMain());
        oneAdapter.setItems(response);
        carouselViewHome.setSlideInterval(5000);
        carouselViewHome.setAnimateOnBoundary(true);
        carouselViewHome.setViewListener(position -> {
            View customView = getLayoutInflater().inflate(R.layout.item_carousel_dash, null);
            CardView cardViewHome = customView.findViewById(R.id.cardViewHome);
            AppCompatImageView imageViewCarousel = customView.findViewById(R.id.imageViewCarousel);
//            AppCompatTextView textViewHome = customView.findViewById(R.id.textViewHome);
            HomeDataMain homeDataMain = response.get(position);

            GlideApp.with(getActivity().getApplicationContext())
                    .load(homeDataMain.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(imageViewCarousel);

            if (homeDataMain.getType().equalsIgnoreCase("question")) {
                cardViewHome.setOnClickListener(l -> {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .addToBackStack("wordFragment")
                            .replace(R.id.screenContainer, QuestionOfDayFragment.newInstance(homeDataMain.getDisplayData().getQuestionData()))
                            .commit();
                });
//                textViewHome.setText(homeDataMain.getTitle());
            } else if (homeDataMain.getType().equalsIgnoreCase("word")) {
//                textViewHome.setText(homeDataMain.getTitle());
                cardViewHome.setOnClickListener(l -> {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .addToBackStack("wordFragment")
                            .replace(R.id.screenContainer, WordFragment.newInstance(homeDataMain.getDisplayData().getWordOfTheDay()))
                            .commit();
                });
            } else if (homeDataMain.getType().equalsIgnoreCase("liveClass")) {
                cardViewHome.setOnClickListener(l -> {
                    try {

                    } catch (Exception e) {

                    }
                });
            } else if (homeDataMain.getType().equalsIgnoreCase("customBanner")) {
                cardViewHome.setOnClickListener(l -> {
                    DisplayData displayData = homeDataMain.getDisplayData();
                    if (displayData.getBannerType() != null && !displayData.getBannerType().isEmpty()) {
                        if (displayData.getBannerType().equalsIgnoreCase("course")) {
                            if (displayData.getCourseData().size() > 0) {
                                Intent intent = new Intent(getContext(), CourseDetailActivity.class);
                                intent.putExtra("course", displayData.getCourseData().get(0));
                                startActivity(intent);
                                /*if (displayData.getCourseData().get(0).getPurchased().equalsIgnoreCase("1")) {
                                    Intent intent = new Intent(getContext(), CourseDetailActivity.class);
                                    intent.putExtra("course", displayData.getCourseData().get(0));
                                    startActivity(intent);
                                    Toasty.success(getContext(), "You have already purchased the course. You will benefit from all the features", Toasty.LENGTH_LONG).show();
                                } else {
                                    if(!displayData.getCourseData().get(0).getPrice().equalsIgnoreCase("0")) {
                                        *//*Intent intent = new Intent(getActivity(), PaymentActivity.class);
                                        ItemListItem itemListItem = new ItemListItem();
                                        itemListItem.setCourseId(displayData.getCourseData().get(0).getCourseId());
                                        itemListItem.setItemType("course");

                                        ArrayList<ItemListItem> paymentArrayList = new ArrayList<>();
                                        paymentArrayList.add(itemListItem);
                                        intent.putExtra("payments", paymentArrayList);
                                        intent.putExtra("price", displayData.getCourseData().get(0).getPrice());
                                        startActivity(intent);*//*
                                        Intent intent = new Intent(getContext(), CourseDetailActivity.class);
                                        intent.putExtra("course", displayData.getCourseData().get(0));
                                        startActivity(intent);
                                    }*/
//                                }
                            }
                        }
                    }
                });
            }

            return customView;
        });
        carouselViewHome.setPageCount(response.size());
        carouselViewHome.setVisibility(View.GONE);
    }

    private void setCarouselViewQuestion(List<HomeDataMain> response) {
        carouselViewQuestion.setSlideInterval(5000);
        carouselViewQuestion.setAnimateOnBoundary(true);
        carouselViewQuestion.setViewListener(position -> {
            View customView = getLayoutInflater().inflate(R.layout.item_carousel_dash, null);
            CardView cardViewHome = customView.findViewById(R.id.cardViewHome);
            AppCompatImageView imageViewCarousel = customView.findViewById(R.id.imageViewCarousel);
//            AppCompatTextView textViewHome = customView.findViewById(R.id.textViewHome);
            HomeDataMain homeDataMain = response.get(position);

            GlideApp.with(getActivity().getApplicationContext())
                    .load(homeDataMain.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(imageViewCarousel);

            if (homeDataMain.getType().equalsIgnoreCase("question")) {
                cardViewHome.setOnClickListener(l -> {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .addToBackStack("wordFragment")
                            .replace(R.id.screenContainer, QuestionOfDayFragment.newInstance(homeDataMain.getDisplayData().getQuestionData()))
                            .commit();
                });
//                textViewHome.setText(homeDataMain.getTitle());
            } else if (homeDataMain.getType().equalsIgnoreCase("word")) {
//                textViewHome.setText(homeDataMain.getTitle());
                cardViewHome.setOnClickListener(l -> {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .addToBackStack("wordFragment")
                            .replace(R.id.screenContainer, WordFragment.newInstance(homeDataMain.getDisplayData().getWordOfTheDay()))
                            .commit();
                });
            }
            return customView;
        });
        carouselViewQuestion.setPageCount(response.size());
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
                        return R.layout.item_feature_list_new;
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
//                LinearLayout linearLayoutBackground = viewBinder.findViewById(R.id.linearLayoutBackground);
//                AppCompatImageView featureIcon = viewBinder.findViewById(R.id.featureIcon);
                AppCompatTextView featureName = viewBinder.findViewById(R.id.featureName);
                FloatingActionButton floatingActionButton = viewBinder.findViewById(R.id.fABProfile);

//                linearLayoutBackground.setBackgroundResource(item.getModel().getBackgroundDrawable());
//                featureName.setTextColor(getResources().getColor(R.color.md_white_1000));
                floatingActionButton.setImageResource(item.getModel().getFeatureIcon());
//                featureIcon.setImageResource(item.getModel().getFeatureIcon());
                featureName.setText(item.getModel().getFeatureName());

                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setOnClickListener(item.getModel());
                    }
                });
            }
        };
    }

    @NotNull
    private ItemModule<HomeDataMain> carouselDataMain() {
        return new ItemModule<HomeDataMain>() {
            @NotNull
            @Override
            public ItemModuleConfig provideModuleConfig() {
                return new ItemModuleConfig() {
                    @Override
                    public int withLayoutResource() {
                        return R.layout.item_new_courses_layout;
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
            public void onBind(@NotNull Item<HomeDataMain> item, @NotNull ViewBinder viewBinder) {
                AppCompatImageView courseIcon = viewBinder.findViewById(R.id.imageView);
                FloatingActionButton fabShare = viewBinder.findViewById(R.id.fabShare);

                GlideApp.with(requireContext())
                        .load(item.getModel().getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .centerInside()
                        .into(courseIcon);

                fabShare.setOnClickListener(v -> {
                    /*Create an ACTION_SEND Intent*/
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    /*This will be the actual content you wish you share.*/
                    String shareBody = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                    /*The type of the content is text, obviously.*/
                    intent.setType("text/plain");
                    /*Applying information Subject and Body.*/
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Download the App from Play Store: ");
                    intent.putExtra(Intent.EXTRA_TEXT, "Download the App from Play Store: \n\n\n"
                            + shareBody);
                    /*Fire!*/
                    startActivity(Intent.createChooser(intent, "Share using..."));
                });

                courseIcon.setOnClickListener(v -> {
                    DisplayData displayData = item.getModel().getDisplayData();
                    if (displayData.getBannerType() != null && !displayData.getBannerType().isEmpty()) {
                        if (displayData.getBannerType().equalsIgnoreCase("course")) {
                            if (displayData.getCourseData().size() > 0) {
                                Intent intent = new Intent(getContext(), CourseDetailActivity.class);
                                intent.putExtra("course", displayData.getCourseData().get(0));
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        };
    }

    @NotNull
    private ItemModule<LayoutItem> layoutItems() {
        return new ItemModule<LayoutItem>() {
            @NotNull
            @Override
            public ItemModuleConfig provideModuleConfig() {
                return new ItemModuleConfig() {
                    @Override
                    public int withLayoutResource() {
                        return R.layout.item_courses;
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
            public void onBind(@NotNull Item<LayoutItem> item, @NotNull ViewBinder viewBinder) {
                AppCompatTextView courseTextView = viewBinder.findViewById(R.id.courseTextView);
                RecyclerView recyclerViewCourses = viewBinder.findViewById(R.id.recyclerViewCourses);

                courseTextView.setText(item.getModel().getTitle());
                recyclerViewCourses.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                OneAdapter oneAdapter = new OneAdapter(recyclerViewCourses)
                        .attachItemModule(courseListItemModule().addEventHook(clickCourseItemHook()));
                oneAdapter.setItems(Objects.requireNonNull(item.getModel().getContent()));
            }
        };
    }

    @NotNull
    private ItemModule<CourseListItem> courseListItemModule() {
        return new ItemModule<CourseListItem>() {
            @NotNull
            @Override
            public ItemModuleConfig provideModuleConfig() {
                return new ItemModuleConfig() {
                    @Override
                    public int withLayoutResource() {
                        return R.layout.item_video_course_home;
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
            public void onBind(@NotNull Item<CourseListItem> item, @NotNull ViewBinder viewBinder) {
                viewBinder.getRootView().getLayoutParams().width = computeCardWidth(requireContext());
                AppCompatTextView textViewNewsHeading = viewBinder.findViewById(R.id.textViewNewsHeading);
                AppCompatTextView textViewNew = viewBinder.findViewById(R.id.textViewNew);
                AppCompatTextView textViewSubDescription = viewBinder.findViewById(R.id.textViewSubDescription);
                AppCompatImageView imageViewNews = viewBinder.findViewById(R.id.imageViewNews);
                AppCompatTextView textViewTotalLecture = viewBinder.findViewById(R.id.textViewTotalLecture);
                AppCompatTextView textViewTotalDuration = viewBinder.findViewById(R.id.textViewTotalDuration);
                LinearLayout linearLayoutTotalLecture = viewBinder.findViewById(R.id.linearLayoutVideoLecture);
                AppCompatTextView textViewPrice = viewBinder.findViewById(R.id.textViewPrice);
                AppCompatTextView textViewMRP = viewBinder.findViewById(R.id.textViewMRP);
                AppCompatTextView textViewDiscount = viewBinder.findViewById(R.id.textViewDiscount);
                linearLayoutTotalLecture.setVisibility(View.VISIBLE);

                textViewNewsHeading.setText(item.getModel().getCourseName());
                if (item.getModel().getDescription() != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        textViewSubDescription.setText(Html.fromHtml(item.getModel().getDescription(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        textViewSubDescription.setText(Html.fromHtml(item.getModel().getDescription()));
                    }
                }
                GlideApp.with(getContext()).load(item.getModel().getThumbnail())
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(imageViewNews);
                textViewTotalDuration.setText(item.getModel().getDuration());
                textViewTotalLecture.setText(item.getModel().getLectureCount());

                if (item.getModel().getIsLive() != null) {
                    if (item.getModel().getIsLive().equalsIgnoreCase("1")) {
                        textViewNew.setText("LIVE");
                        textViewNew.setVisibility(View.VISIBLE);
                        ObjectAnimator anim = ObjectAnimator.ofInt(textViewNew, "backgroundColor", Color.RED, Color.BLUE, Color.RED);
                        anim.setDuration(3000);
                        anim.setEvaluator(new ArgbEvaluator());
                        anim.setRepeatCount(Animation.INFINITE);
                        anim.start();
                    } else {
                        if (item.getModel().getIsNew() != null) {
                            if (item.getModel().getIsNew().equalsIgnoreCase("1")) {
                                textViewNew.setText("NEW");
                                textViewNew.setVisibility(View.VISIBLE);
                                ObjectAnimator anim = ObjectAnimator.ofInt(textViewNew, "backgroundColor", Color.GREEN, Color.RED, Color.GREEN);
                                anim.setDuration(3000);
                                anim.setEvaluator(new ArgbEvaluator());
                                anim.setRepeatCount(Animation.INFINITE);
                                anim.start();
                            } else {
                                textViewNew.setVisibility(View.GONE);
                            }
                        } else {
                            textViewNew.setVisibility(View.GONE);
                        }
                    }
                } else {
                    if (item.getModel().getIsNew() != null) {
                        if (item.getModel().getIsNew().equalsIgnoreCase("1")) {
                            textViewNew.setText("NEW");
                            textViewNew.setVisibility(View.VISIBLE);
                            ObjectAnimator anim = ObjectAnimator.ofInt(textViewNew, "backgroundColor", Color.GREEN, Color.RED, Color.GREEN);
                            anim.setDuration(3000);
                            anim.setEvaluator(new ArgbEvaluator());
                            anim.setRepeatCount(Animation.INFINITE);
                            anim.start();
                        } else {
                            textViewNew.setVisibility(View.GONE);
                        }
                    } else {
                        textViewNew.setVisibility(View.GONE);
                    }
                }
                if (item.getModel().getMrp() != null) {
                    textViewMRP.setText(getString(R.string.txt_rs_symbol) + " " + item.getModel().getMrp());
                    if (!textViewMRP.getPaint().isStrikeThruText()) {
                        textViewMRP.setPaintFlags(textViewMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        textViewMRP.setPaintFlags(textViewMRP.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                }

                if (item.getModel().getPrice() != null) {
                    textViewPrice.setText(getString(R.string.txt_rs_symbol) + " " + item.getModel().getPrice());

                }

                if (item.getModel().getDiscountPercent() != null) {
                    textViewDiscount.setText(item.getModel().getDiscountPercent() + " %");
                }
                linearLayoutTotalLecture.setVisibility(View.GONE);
            }

        };
    }

    private int computeCardWidth(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().widthPixels
                - context.getResources().getDimensionPixelSize(R.dimen.peek_width_large)
                - context.getResources().getDimensionPixelSize(R.dimen.item_spacing_large);
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
        }

    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private class PagingModuleImpl extends PagingModule {
        @NotNull
        @Override
        public PagingModuleConfig provideModuleConfig() {
            return new PagingModuleConfig() {
                @Override
                public int withLayoutResource() {
                    return R.layout.item_series_list_placeholder;
                }

                @Override
                public int withVisibleThreshold() {
                    return 3;
                }
            };
        }

        @Override
        public void onLoadMore(int currentPage) {

        }
    }

    private void observeHomeAPI() {
        mHomeViewModel.observeCourseAPI().observe(getViewLifecycleOwner(), upcomingCourseResponseResponse -> {
            if (upcomingCourseResponseResponse.code() == HttpsURLConnection.HTTP_OK) {
                if (upcomingCourseResponseResponse.body().getCode() == HttpsURLConnection.HTTP_OK) {
                    if (upcomingCourseResponseResponse.body().getData().getTodayLiveClass().size() > 0) {
                        upcomingVideoListItem.clear();
                        /*if(upcomingCourseResponseResponse.body().getData().getTodayLiveClass().get(i).isLive() == 1) {
                                currentLiveClass = upcomingCourseResponseResponse.body().getData().getTodayLiveClass().get(i);
                            } else {*/
                        /*}*/
                        upcomingVideoListItem.addAll(upcomingCourseResponseResponse.body().getData().getTodayLiveClass());
                    } else {
//                        currentLiveClass = null;
                        upcomingVideoListItem.clear();
                    }

                    setView();
                }
            }
        });
    }

    private void setView() {
        recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        courseHorizontalAdapterUpcoming = new CourseHorizontalAdapter(getActivity(), upcomingVideoListItem, null, null);
        recyclerViewUpcoming.setAdapter(courseHorizontalAdapterUpcoming);
        courseHorizontalAdapterUpcoming.notifyDataSetChanged();
    }

    @NotNull
    private ClickEventHook<CourseListItem> clickCourseItemHook() {
        return new ClickEventHook<CourseListItem>() {
            @Override
            public void onClick(@NotNull CourseListItem model, @NotNull ViewBinder viewBinder) {
                if (model.getPurchased() != null && !model.getPurchased().isEmpty()) {
                    if (model.getPurchased().equalsIgnoreCase("0")) {
                        // redirect to purchase link
//                        showPurchasePopup(model.getCourseId(), model.getPurchased());
                        Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
                        intent.putExtra("course", model);
                        startActivity(intent);
                    } else if (model.getPurchased().equalsIgnoreCase("1")) {
                        // else
                        Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
                        intent.putExtra("course", model);
                        startActivity(intent);
                        /*getActivity().getSupportFragmentManager().beginTransaction()
                                .addToBackStack("TestListFragment")
                                .replace(R.id.screenContainer, VideoListingbyCourseFragment.newInstance(model.getCourseId(),
                                        model.getPurchased()))
                                .commit();*/
                    }
                } else {
//                    showPurchasePopup(model.getCourseId(), "0");
                    Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
                    startActivity(intent);
                }
            }
        };
    }
}
