package com.conicskill.app.ui.courseDetail;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.conicskill.app.BuildConfig;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsRequest;
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsRequestData;
import com.conicskill.app.data.model.playlist.CategoryListItem;
import com.conicskill.app.data.model.videoCouses.CourseListItem;
import com.conicskill.app.data.model.videoCouses.VideoListItem;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.ui.courseDetail.interfaces.CourseClickListener;
import com.conicskill.app.util.Constants;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;

public class RecordedFragment extends BaseFragment implements CourseClickListener {

    @Inject
    ViewModelFactory viewModelFactory;
    private CourseDetailViewModel mCourseViewModel;

    @BindView(R.id.recyclerviewPlayList)
    RecyclerView recyclerViewPlayList;

    @BindView(R.id.linearLayoutLoading)
    RelativeLayout linearLayoutLoading;

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

    public static RecordedFragment newInstance(CourseListItem courseListItem, String type) {
        RecordedFragment courseDetailFragment = new RecordedFragment();
        Bundle args = new Bundle();
        args.putSerializable("course", courseListItem);
        args.putSerializable("type", type);
        courseDetailFragment.setArguments(args);
        return courseDetailFragment;
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_recorded;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mCourseViewModel = new ViewModelProvider(this, viewModelFactory).get(CourseDetailViewModel.class);
        observeViewModel();
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getSerializable("course") != null) {
                course = (CourseListItem) bundle.getSerializable("course");
                type = (String) bundle.getString("type");
            }
        }

        isPurchased = course.getPurchased();
        categoryListItems = new ArrayList<>();
        recyclerViewPlayList.setLayoutManager(new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false));
        playListAdapter = new PlayListAdapter(categoryListItems, getContext(), course, this::onPlayListItemClicked);
        recyclerViewPlayList.setAdapter(playListAdapter);
        CurrentAffairsRequestData currentAffairsRequestData = new CurrentAffairsRequestData();
        currentAffairsRequestData.setCompanyId(BuildConfig.COMPANY_ID);
        currentAffairsRequestData.setCourseId(course.getCourseId());

        CurrentAffairsRequest currentAffairsRequest = new CurrentAffairsRequest();
        currentAffairsRequest.setToken(mCourseViewModel.tinyDB.getString(Constants.AUTH_TOKEN));
        currentAffairsRequest.setCurrentAffairsRequestData(currentAffairsRequestData);
        mCourseViewModel.callFetchCourseCategories(currentAffairsRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void observeViewModel() {
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
                }
            }
        });

        mCourseViewModel.observeCourseCategoriesAPI().observe(getViewLifecycleOwner(), playlistResponseResponse -> {
            if (playlistResponseResponse.code() == HttpsURLConnection.HTTP_OK) {
                if (playlistResponseResponse.body().getCode() == HttpsURLConnection.HTTP_OK) {
                    if (playlistResponseResponse.body().getPlayListData().getCategoryList().size() != 1) {
                        recyclerViewPlayList.setVisibility(View.VISIBLE);
                        categoryListItems.clear();
                        categoryListItems.addAll(playlistResponseResponse.body().getPlayListData().getCategoryList());
                        playListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
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
}
