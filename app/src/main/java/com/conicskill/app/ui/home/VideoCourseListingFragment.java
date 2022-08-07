package com.conicskill.app.ui.home;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.conicskill.app.BuildConfig;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsRequest;
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsRequestData;
import com.conicskill.app.data.model.videoCouses.CourseListItem;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.ui.CourseDetailActivity;
import com.conicskill.app.util.Constants;
import com.conicskill.app.util.GlideApp;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.idanatz.oneadapter.OneAdapter;
import com.idanatz.oneadapter.external.event_hooks.ClickEventHook;
import com.idanatz.oneadapter.external.interfaces.Item;
import com.idanatz.oneadapter.external.modules.ItemModule;
import com.idanatz.oneadapter.external.modules.ItemModuleConfig;
import com.idanatz.oneadapter.internal.holders.ViewBinder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import co.ceryle.segmentedbutton.SegmentedButtonGroup;
import es.dmoral.toasty.Toasty;

public class VideoCourseListingFragment extends BaseFragment {

    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.recyclerViewTestList)
    RecyclerView recyclerViewTestList;
    @BindView(R.id.shimmerFrameLayout)
    ShimmerFrameLayout shimmerFrameLayout;
    @BindView(R.id.segmentButtonGroup)
    SegmentedButtonGroup segmentButtonGroup;
    ArrayList<String> moduleId = new ArrayList<>();
    List<CourseListItem> courseFullList = new ArrayList<>();
    List<CourseListItem> coursePurchasedList = new ArrayList<>();
    private MockTestViewModel mockTestViewModel;
    private OneAdapter oneAdapter;
    private OneAdapter oneAdapterPurchased;

    public static VideoCourseListingFragment newInstance() {

        return new VideoCourseListingFragment();
    }

    @Override
    protected int layoutRes() {
        return R.layout.mock_test_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mockTestViewModel = new ViewModelProvider(this, viewModelFactory).get(MockTestViewModel.class);
        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerViewTestList.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewTestList.setItemAnimator(null);

        segmentButtonGroup.setVisibility(View.GONE);

        segmentButtonGroup.setOnPositionChangedListener(position -> {
            if (position == 0) {
                oneAdapter.clear();
                oneAdapter.setItems(courseFullList);
                recyclerViewTestList.smoothScrollToPosition(0);
            } else {
                oneAdapter.clear();
                oneAdapter.setItems(coursePurchasedList);
                recyclerViewTestList.smoothScrollToPosition(0);
            }
        });
        oneAdapter = new OneAdapter(recyclerViewTestList)
                .attachItemModule(featureItems().addEventHook(clickEventHook()));

        CurrentAffairsRequestData currentAffairsRequestData = new CurrentAffairsRequestData();
        currentAffairsRequestData.setCompanyId(BuildConfig.COMPANY_ID);

        CurrentAffairsRequest currentAffairsRequest = new CurrentAffairsRequest();
        currentAffairsRequest.setToken(mockTestViewModel.tinyDB.getString(Constants.AUTH_TOKEN));
        currentAffairsRequest.setCurrentAffairsRequestData(currentAffairsRequestData);
        mockTestViewModel.fetchVideoCourses(currentAffairsRequest);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        observeViewModel();
    }

    private void observeViewModel() {
        mockTestViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                shimmerFrameLayout.setVisibility(View.VISIBLE);
            } else {
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });

        mockTestViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                if (error) {
                    shimmerFrameLayout.setVisibility(View.GONE);
                    Toasty.error(getContext(), "Failed to load test. Please try again.", Toasty.LENGTH_LONG, false).show();
                }
            }
        });

        mockTestViewModel.observeFetchVideoCourses().observe(getViewLifecycleOwner(), videoCoursesResponseResponse -> {
            if (videoCoursesResponseResponse.body().getCode() == HttpsURLConnection.HTTP_OK) {
                oneAdapter.clear();
                if (videoCoursesResponseResponse.body().getData() != null) {
                    if (videoCoursesResponseResponse.body().getData().getCourseList().size() > 0) {
                        recyclerViewTestList.setVisibility(View.VISIBLE);
                        courseFullList.addAll(videoCoursesResponseResponse.body().getData().getCourseList());
                        for (int i = 0; i < courseFullList.size(); i++) {
                            if (courseFullList.get(i).getPurchased().equalsIgnoreCase("1")) {
                                coursePurchasedList.add(courseFullList.get(i));
                            }
                        }
                        oneAdapter.setItems(courseFullList);
                        segmentButtonGroup.setPosition(1);
                    }
                }
            } else {
                Toasty.error(getContext(), "Something went wrong. Please try again", Toasty.LENGTH_LONG, false).show();
            }
        });

    }

    @NotNull
    private ItemModule<CourseListItem> featureItems() {
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
                LinearLayout priceHolder = viewBinder.findViewById(R.id.priceHolder);
                MaterialButton buttonViewCourse = viewBinder.findViewById(R.id.buttonViewCourse);
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

                if(item.getModel().getMrp() != null) {
                    textViewMRP.setText(getString(R.string.txt_rs_symbol) + " " + item.getModel().getMrp());
                    if (!textViewMRP.getPaint().isStrikeThruText()) {
                        textViewMRP.setPaintFlags(textViewMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        textViewMRP.setPaintFlags(textViewMRP.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                }

                if(item.getModel().getPrice() != null) {
                    textViewPrice.setText(getString(R.string.txt_rs_symbol) + " " + item.getModel().getPrice());

                }

                if(item.getModel().getDiscountPercent() != null) {
                    textViewDiscount.setText(item.getModel().getDiscountPercent() + " %");
                }
                buttonViewCourse.setVisibility(View.VISIBLE);
                linearLayoutTotalLecture.setVisibility(View.GONE);
                buttonViewCourse.setOnClickListener(l-> {
                    courseRedirection(item.getModel());
                });
                priceHolder.setVisibility(View.GONE);
            }

        };
    }

    @NotNull
    private ClickEventHook<CourseListItem> clickEventHook() {
        return new ClickEventHook<CourseListItem>() {
            @Override
            public void onClick(@NotNull CourseListItem model, @NotNull ViewBinder viewBinder) {
                courseRedirection(model);
            }
        };
    }

    private void courseRedirection(@NotNull CourseListItem model) {
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
}
