package com.conicskill.app.ui.courseDetail;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.conicskill.app.BuildConfig;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.playlist.CategoryListItem;
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsRequest;
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsRequestData;
import com.conicskill.app.data.model.videoCouses.CourseListItem;
import com.conicskill.app.data.model.videoCouses.VideoListItem;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.ui.courseDetail.interfaces.CourseClickListener;
import com.conicskill.app.util.Constants;
import com.conicskill.app.util.GlideApp;
import com.conicskill.app.util.StartSnapHelper;
import com.conicskill.app.util.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.idanatz.oneadapter.external.interfaces.Item;
import com.idanatz.oneadapter.external.modules.ItemModule;
import com.idanatz.oneadapter.external.modules.ItemModuleConfig;
import com.idanatz.oneadapter.internal.holders.ViewBinder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;

public class UpcomingFragment extends BaseFragment  implements CourseClickListener {

    @Inject
    ViewModelFactory viewModelFactory;
    private CourseDetailViewModel mCourseViewModel;

    @BindView(R.id.textViewNoLiveClass)
    AppCompatTextView textViewNoLiveClass;

    @BindView(R.id.imageViewCourseBanner)
    AppCompatImageView imageViewCourseBanner;

    @BindView(R.id.recyclerViewUpcoming)
    RecyclerView recyclerViewUpcoming;

    @BindView(R.id.recyclerViewPrevious)
    RecyclerView recyclerViewPrevious;

    @BindView(R.id.textViewLive)
    TextView textViewLive;

    @BindView(R.id.cardViewLive)
    CardView cardViewLive;

    @BindView(R.id.linearLayoutEmpty)
    LinearLayout linearLayoutEmpty;

    @BindView(R.id.linearLayoutContent)
    LinearLayout linearLayoutContent;

    @BindView(R.id.linearLayoutLoading)
    RelativeLayout linearLayoutLoading;

    @BindView(R.id.buttonViewMore)
    MaterialButton buttonViewMore;

    @BindView(R.id.textViewNoUpcomingClasses)
    AppCompatTextView textViewNoUpcomingClasses;

    @BindView(R.id.pager)
    ViewPager2 pager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.recyclerviewPlayList)
    RecyclerView recyclerViewPlayList;

    @BindView(R.id.textViewCurrentLive)
    TextView textViewCurrentLive;

    @BindView(R.id.textViewUpcomingClasses)
    TextView textViewUpcomingClasses;


    private ArrayList<CategoryListItem> categoryListItems = new ArrayList<>();
//    private CategoryPagerAdapter pagerAdapter;
    private PostsPagerAdapter pagerAdapter;

    private PlayListAdapter playListAdapter;

    private VideoListItem currentLiveClass;
    private ArrayList<VideoListItem> upcomingVideoListItem = new ArrayList<>();
    private ArrayList<VideoListItem> previousVideoListItem = new ArrayList<>();
    private CourseListItem course;
    private String type;
    private String isPurchased = "0";
    private int cardWidth;
    private int cardHeight;
    private CourseHorizontalAdapter courseHorizontalAdapterUpcoming;
    private CourseHorizontalAdapter courseHorizontalAdapterPrevious;

    public static UpcomingFragment newInstance(CourseListItem courseListItem, String type) {
        UpcomingFragment courseDetailFragment = new UpcomingFragment();
        Bundle args = new Bundle();
        args.putSerializable("course", courseListItem);
        args.putSerializable("type", type);
        courseDetailFragment.setArguments(args);
        return courseDetailFragment;
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_upcoming;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        mCourseViewModel = new ViewModelProvider(this, viewModelFactory).get(CourseDetailViewModel.class);
        observeViewModel();
        Bundle bundle = getArguments();
        if(bundle != null) {
            if(bundle.getSerializable("course") != null) {
                course = (CourseListItem) bundle.getSerializable("course");
                type = (String) bundle.getString("type");
            }
        }

        isPurchased = course.getPurchased();
//        mCourseViewModel.callFetchCourseVideo(currentAffairsRequest);


        categoryListItems = new ArrayList<>();
        recyclerViewPlayList.setLayoutManager(new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false));


        playListAdapter = new PlayListAdapter(categoryListItems, getContext(), course, this::onPlayListItemClicked);
        recyclerViewPlayList.setAdapter(playListAdapter);


        new StartSnapHelper().attachToRecyclerView(recyclerViewUpcoming);
        new StartSnapHelper().attachToRecyclerView(recyclerViewPrevious);
        buttonViewMore.setVisibility(View.GONE);

        recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerViewPrevious.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        recyclerViewUpcoming.addItemDecoration(new ItemDecoration());
        recyclerViewPrevious.addItemDecoration(new ItemDecoration());

        courseHorizontalAdapterPrevious = new CourseHorizontalAdapter(getActivity(), previousVideoListItem, isPurchased, course);
        recyclerViewPrevious.setAdapter(courseHorizontalAdapterPrevious);

        courseHorizontalAdapterUpcoming = new CourseHorizontalAdapter(getActivity(), upcomingVideoListItem, isPurchased, course);
        recyclerViewUpcoming.setAdapter(courseHorizontalAdapterUpcoming);

        recyclerViewPrevious.setVisibility(View.GONE);

        if(type.equalsIgnoreCase("Live")) {

        } else {
            linearLayoutContent.setVisibility(View.GONE);
            textViewCurrentLive.setVisibility(View.GONE);
            cardViewLive.setVisibility(View.GONE);
            textViewNoUpcomingClasses.setVisibility(View.GONE);
            textViewUpcomingClasses.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(categoryListItems.size() > 0) {
            // do nothing
        } else {
            CurrentAffairsRequestData currentAffairsRequestData = new CurrentAffairsRequestData();
            currentAffairsRequestData.setCompanyId(BuildConfig.COMPANY_ID);
            currentAffairsRequestData.setCourseId(course.getCourseId());

            CurrentAffairsRequest currentAffairsRequest = new CurrentAffairsRequest();
            currentAffairsRequest.setToken(mCourseViewModel.tinyDB.getString(Constants.AUTH_TOKEN));
            currentAffairsRequest.setCurrentAffairsRequestData(currentAffairsRequestData);
            if (type.equalsIgnoreCase("Live")) {
                mCourseViewModel.callFetchCourseVideo(currentAffairsRequest);
            } else {
                mCourseViewModel.callFetchCourseCategories(currentAffairsRequest);
            }
        }
    }

    private void observeViewModel(){
        mCourseViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                linearLayoutLoading.setVisibility(View.VISIBLE);
            } else {
                linearLayoutLoading.setVisibility(View.GONE);
            }
        });

        mCourseViewModel.getError().observe(getViewLifecycleOwner(), isError -> {
            if (isError != null) {
                if (isError) {
                    linearLayoutLoading.setVisibility(View.GONE);
                    linearLayoutEmpty.setVisibility(View.VISIBLE);
                }
            }
        });

        mCourseViewModel.observeCourseCategoriesAPI().observe(getViewLifecycleOwner(), playlistResponseResponse -> {
            if (playlistResponseResponse.code() == HttpsURLConnection.HTTP_OK) {
                if (playlistResponseResponse.body().getCode() == HttpsURLConnection.HTTP_OK) {
//                    Log.e("TAG", playlistResponseResponse.body().getMessage());

                    //  Toast.makeText(getContext(), playlistResponseResponse.body().getPlayListData().getCategoryList().get(0).getCategoryName() + "", Toast.LENGTH_SHORT).show();
                    //body().getPlayListData().getCategoryList().get(0).getId(),Toast.LENGTH_SHORT).show();
                    if (playlistResponseResponse.body().getPlayListData().getCategoryList().size() != 1) {
                        recyclerViewPlayList.setVisibility(View.VISIBLE);
                        categoryListItems.clear();
                        categoryListItems.addAll(playlistResponseResponse.body().getPlayListData().getCategoryList());
                        playListAdapter.notifyDataSetChanged();
//                        setUpViewPager();
                    } else {
//                        recyclerViewPlayList.setVisibility(View.GONE);
                    }

                    if(playlistResponseResponse.body().getPlayListData().getTodayLiveClass().size() != 0) {
                        for(int i=0; i<playlistResponseResponse.body().getPlayListData().getTodayLiveClass().size();i++) {
                            if(playlistResponseResponse.body().getPlayListData().getTodayLiveClass().get(i).isLive() == 1) {
                                currentLiveClass = playlistResponseResponse.body().getPlayListData().getTodayLiveClass().get(i);
                                break;
                            }
                        }
                    }

                    setView();
                }
            }
        });
        mCourseViewModel.observeCourseAPI().observe(getViewLifecycleOwner(), upcomingCourseResponseResponse -> {
            if(upcomingCourseResponseResponse.code() == HttpsURLConnection.HTTP_OK) {
                if(upcomingCourseResponseResponse.body().getCode() == HttpsURLConnection.HTTP_OK) {
                    if(upcomingCourseResponseResponse.body().getData().getTodayLiveClass().size() >0) {
                        upcomingVideoListItem.clear();
                        for(int i=0; i<upcomingCourseResponseResponse.body().getData().getTodayLiveClass().size();i++) {
                            if(upcomingCourseResponseResponse.body().getData().getTodayLiveClass().get(i).isLive() == 1) {
                                currentLiveClass = upcomingCourseResponseResponse.body().getData().getTodayLiveClass().get(i);
                            } else {
                                upcomingVideoListItem.add(upcomingCourseResponseResponse.body().getData().getTodayLiveClass().get(i));
                            }
                        }
                    } else {
                        currentLiveClass = null;
                        upcomingVideoListItem.clear();
                    }

                    if(upcomingCourseResponseResponse.body().getData().getPreviousLiveClass().size() >0) {
                        previousVideoListItem.clear();
                        previousVideoListItem.addAll(upcomingCourseResponseResponse.body().getData().getPreviousLiveClass());
                    }

                    setView();
                }
            }
        });
    }

    private void setView() {
        if(currentLiveClass != null) {
            GlideApp.with(getContext()).load(currentLiveClass.getThumbnail())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(imageViewCourseBanner);
            cardViewLive.setOnClickListener(l->{
                try {
                    String[] url = currentLiveClass.getUrl().split("=");
                    Utils.getInstance(getContext()).voidOpen(getActivity(),currentLiveClass.getTitle(),
                            url[1],
                            currentLiveClass, course, null, null,
                            currentLiveClass.isLive() == 1);
                } catch (Exception e) {
                    
                }
            });
            textViewNoLiveClass.setVisibility(View.VISIBLE);
            textViewLive.setVisibility(View.VISIBLE);
            textViewNoLiveClass.setText(currentLiveClass.getTitle());
        } else {
            textViewNoLiveClass.setVisibility(View.VISIBLE);
            textViewLive.setVisibility(View.GONE);
        }
//        oneAdapterPreviousVideo.setItems(previousVideoListItem);
//        oneAdapter.add(upcomingVideoListItem);

        courseHorizontalAdapterUpcoming.notifyDataSetChanged();
        courseHorizontalAdapterPrevious.notifyDataSetChanged();

        /*if(previousVideoListItem.size() > 0) {
            buttonViewMore.setVisibility(View.VISIBLE);
            buttonViewMore.setOnClickListener(l->{
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack("TestListFragment")
                        .replace(R.id.screenContainer, VideoListingbyCourseNewFragment.newInstance(course.getCourseId(), "1",null, null))
                        .commit();
            });
        }*/

        if(upcomingVideoListItem.size() == 0) {
            recyclerViewUpcoming.setVisibility(View.GONE);
            textViewNoUpcomingClasses.setVisibility(View.VISIBLE);
        } else {
            recyclerViewUpcoming.setVisibility(View.VISIBLE);
            textViewNoUpcomingClasses.setVisibility(View.GONE);
        }

        linearLayoutContent.setVisibility(View.VISIBLE);
        linearLayoutEmpty.setVisibility(View.GONE);
        linearLayoutLoading.setVisibility(View.GONE);

        if(type.equalsIgnoreCase("Live")) {

        } else {
            textViewNoUpcomingClasses.setVisibility(View.GONE);
        }
    }

    @NotNull
    private ItemModule<VideoListItem> featureItems() {
        return new ItemModule<VideoListItem>() {
            @NotNull
            @Override
            public ItemModuleConfig provideModuleConfig() {
                return new ItemModuleConfig() {
                    @Override
                    public int withLayoutResource() {
                        return R.layout.item_news;
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
            public void onBind(@NotNull Item<VideoListItem> item, @NotNull ViewBinder viewBinder) {

                viewBinder.getRootView().getLayoutParams().width = cardWidth;
                viewBinder.getRootView().getLayoutParams().height = cardHeight;

                AppCompatTextView textViewNewsHeading = viewBinder.findViewById(R.id.textViewNewsHeading);
                AppCompatTextView textViewSubDescription = viewBinder.findViewById(R.id.textViewSubDescription);
                AppCompatImageView imageViewNews = viewBinder.findViewById(R.id.imageViewNews);
                AppCompatTextView textViewTotalLecture = viewBinder.findViewById(R.id.textViewTotalLecture);
                AppCompatTextView textViewTotalDuration = viewBinder.findViewById(R.id.textViewTotalDuration);
                LinearLayout linearLayoutTotalLecture = viewBinder.findViewById(R.id.linearLayoutVideoLecture);

                linearLayoutTotalLecture.setVisibility(View.GONE);

                textViewNewsHeading.setText(item.getModel().getTitle());
                if(item.getModel().getDescription() != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        textViewSubDescription.setText(Html.fromHtml(item.getModel().getDescription(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        textViewSubDescription.setText(Html.fromHtml(item.getModel().getDescription()));
                    }
                }
                GlideApp.with(getContext()).load(item.getModel().getThumbnail())
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(imageViewNews);
            }

            @Override
            public void onCreated(@NotNull ViewBinder viewBinder) {
                super.onCreated(viewBinder);
                cardWidth = computeCardWidth(getContext());
                cardHeight = computeCardHeight(getContext(), cardWidth, viewBinder);
            }
        };
    }

    private int computeCardWidth(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().widthPixels
                - context.getResources().getDimensionPixelSize(R.dimen.peek_width)
                - context.getResources().getDimensionPixelSize(R.dimen.item_spacing);
    }

    private int computeCardHeight(@NonNull Context context, int width, ViewBinder viewBinder) {
        viewBinder.getRootView().measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), 0);
        return viewBinder.getRootView().getMeasuredHeight();
    }

    /**
     * This is for getting the click data for redirection and other purposes
     *
     * @param item
     */
    @Override
    public void onPlayListItemClicked(CategoryListItem item) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack("TestListFragment")
                .replace(R.id.screenContainer, SubCategoryFragment.newInstance(item.getSubCategoriesArrayList(), "1",
                        course.getCourseId(),
                        item.getId(), item, course))
                .commit();
    }

    private void setUpViewPager() {
        linearLayoutEmpty.setVisibility(View.GONE);
        linearLayoutLoading.setVisibility(View.GONE);
        pager.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        pagerAdapter = new PostsPagerAdapter(this, categoryListItems, course);
        pager.setAdapter(pagerAdapter);
//        pager.setOffscreenPageLimit(5);
        new TabLayoutMediator(tabLayout, pager,
                (tab, position) -> tab.setText(categoryListItems.get(position).getCategoryName())
        ).attach();
        tabLayout.setTabRippleColor(null);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(0, 10, 20, 10);
            tab.requestLayout();
        }
        buttonViewMore.setVisibility(View.GONE);
    }

}
