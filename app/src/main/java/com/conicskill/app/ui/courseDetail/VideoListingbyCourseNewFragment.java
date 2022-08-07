package com.conicskill.app.ui.courseDetail;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.notes.PdfListItem;
import com.conicskill.app.data.model.playlist.CategoryListItem;
import com.conicskill.app.data.model.playlist.VideoCourseListingRequest;
import com.conicskill.app.data.model.playlist.VideoFilters;
import com.conicskill.app.data.model.playlist.VideoListingData;
import com.conicskill.app.data.model.videoCouses.CourseListItem;
import com.conicskill.app.data.model.videoCouses.SubCategoryItem;
import com.conicskill.app.data.model.videoCouses.VideoListItem;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.ui.courseDetail.interfaces.NotesClickListner;
import com.conicskill.app.ui.home.MockTestViewModel;
import com.conicskill.app.ui.pdf.PdfFragment;
import com.conicskill.app.util.Constants;
import com.conicskill.app.util.GlideApp;
import com.conicskill.app.util.Utils;
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

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import co.ceryle.segmentedbutton.SegmentedButtonGroup;
import es.dmoral.toasty.Toasty;

public class VideoListingbyCourseNewFragment extends BaseFragment implements NotesClickListner {

    private static String courseId = "1";
    private static String isPurchased = "0";
    private static String categoryId = null;
    private static String subCategoryId = null;
    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.recyclerViewTestList)
    RecyclerView recyclerViewTestList;
    @BindView(R.id.shimmerFrameLayout)
    ShimmerFrameLayout shimmerFrameLayout;
    @BindView(R.id.segmentButtonGroup)
    SegmentedButtonGroup segmentedButtonGroup;
    @BindView(R.id.recyclerViewNotes)
    RecyclerView recyclerViewNotes;
    ArrayList<String> moduleId = new ArrayList<>();
    ArrayList<PdfListItem> pdfListItemArrayList = new ArrayList<>();
    private MockTestViewModel mockTestViewModel;
    private OneAdapter oneAdapter;
    private NotesAdapter notesAdapter;
    private static String freeOnly = "";
    private static CourseListItem course;
    private static CategoryListItem category;
    private static SubCategoryItem subCategory;
    private static Boolean isDemo = false;

    public static VideoListingbyCourseNewFragment
    newInstance(String id, String isPurchasedFromConstant,
                String categoryIdfromConstant,
                String subCategoryIdConstant,
                CourseListItem courseListItem,
                CategoryListItem categoryListItem, SubCategoryItem subCategoryItem, boolean isFromDemo) {
        courseId = id;
        isPurchased = isPurchasedFromConstant;
        if (categoryIdfromConstant != null) {
            categoryId = categoryIdfromConstant;
            freeOnly = "";
        } else {
            freeOnly = "1";
        }
        if(subCategoryIdConstant != null) {
            subCategoryId = subCategoryIdConstant;
        }
        category = categoryListItem;
        course = courseListItem;
        subCategory = subCategoryItem;
        isDemo = isFromDemo;
        return new VideoListingbyCourseNewFragment();
    }

    @Override
    protected int layoutRes() {
        return R.layout.video_course_listing_new_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mockTestViewModel = new ViewModelProvider(this, viewModelFactory).get(MockTestViewModel.class);
        observeViewModel();

        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerViewTestList.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewTestList.setItemAnimator(null);

        oneAdapter = new OneAdapter(recyclerViewTestList)
                .attachItemModule(featureItems().addEventHook(clickEventHook()));
        notesAdapter = new NotesAdapter(pdfListItemArrayList, getContext(), this::onNotesItemClicked);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewNotes.setLayoutManager(linearLayoutManager);
        recyclerViewNotes.setAdapter(notesAdapter);


        VideoFilters filters = new VideoFilters();
        filters.setVideoCategory(categoryId);
        filters.setVideoSubCategory(subCategoryId);
        if(!freeOnly.isEmpty()) {
            filters.setFreeOnly(freeOnly);
        }
        VideoListingData videoListingData = new VideoListingData();
        videoListingData.setFilters(filters);
        videoListingData.setCourseId(courseId);
        VideoCourseListingRequest videoCourseListingRequest = new VideoCourseListingRequest();
        videoCourseListingRequest.setAuthToken(mockTestViewModel.tinyDB.getString(Constants.AUTH_TOKEN));
        videoCourseListingRequest.setVideoListingData(videoListingData);
        if (oneAdapter.getItemCount() == 0) {
            mockTestViewModel.fetchVideoListByCourse(videoCourseListingRequest);
        }
        if (pdfListItemArrayList.size() == 0) {
            if(!isDemo) {
                mockTestViewModel.fetchNotesByCourse(videoCourseListingRequest);
            }
        }

        segmentedButtonGroup.setOnPositionChangedListener(position -> {
            if (position == 0) {
                recyclerViewTestList.setVisibility(View.VISIBLE);
                recyclerViewNotes.setVisibility(View.GONE);
            } else {
                recyclerViewNotes.setVisibility(View.VISIBLE);
                recyclerViewTestList.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (segmentedButtonGroup.getPosition() == 0) {
            recyclerViewTestList.setVisibility(View.VISIBLE);
            recyclerViewNotes.setVisibility(View.GONE);
        } else {
            recyclerViewNotes.setVisibility(View.VISIBLE);
            recyclerViewTestList.setVisibility(View.GONE);
        }
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

        mockTestViewModel.observeGetNotes().observe(getViewLifecycleOwner(), notesResponseResponse -> {
            if (notesResponseResponse.code() == HttpsURLConnection.HTTP_OK) {
                if (notesResponseResponse.body().getCode() == HttpsURLConnection.HTTP_OK) {
                    if (notesResponseResponse.body().getNotesData() != null) {
                        pdfListItemArrayList.clear();
                        pdfListItemArrayList.addAll(notesResponseResponse.body().getNotesData().getPdfList());
                    }
                    if (segmentedButtonGroup.getPosition() == 0) {
                        recyclerViewTestList.setVisibility(View.VISIBLE);
                        recyclerViewNotes.setVisibility(View.GONE);
                    } else {
                        recyclerViewNotes.setVisibility(View.VISIBLE);
                        recyclerViewTestList.setVisibility(View.GONE);
                    }
                }
            }
        });

        mockTestViewModel.observeFetchVideoListByCourse().observe(getViewLifecycleOwner(), videoCoursesResponseResponse -> {
            if(videoCoursesResponseResponse != null) {
                if (videoCoursesResponseResponse.code() == HttpsURLConnection.HTTP_OK) {
                    if (videoCoursesResponseResponse.body().getCode() == HttpsURLConnection.HTTP_OK) {
                        if (videoCoursesResponseResponse.body().getData() != null) {
                            if (videoCoursesResponseResponse.body().getData().getVideoList().size() > 0) {
                                oneAdapter.clear();
                                oneAdapter.add(videoCoursesResponseResponse.body().getData().getVideoList());
                            }
                        }
                        if (segmentedButtonGroup.getPosition() == 0) {
                            recyclerViewTestList.setVisibility(View.VISIBLE);
                            recyclerViewNotes.setVisibility(View.GONE);
                        } else {
                            recyclerViewNotes.setVisibility(View.VISIBLE);
                            recyclerViewTestList.setVisibility(View.GONE);
                        }
                    } else {
                        Toasty.error(getContext(), "Something went wrong. Please try again", Toasty.LENGTH_LONG, false).show();
                    }
                } else {
                    Toasty.error(getContext(), "Something went wrong. Please try again", Toasty.LENGTH_LONG, false).show();
                }
            }
        });

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
                        return R.layout.item_videos;
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
                AppCompatTextView textViewNewsHeading = viewBinder.findViewById(R.id.textViewNewsHeading);
                MaterialButton buttonWatch = viewBinder.findViewById(R.id.buttonWatch);
                MaterialButton buttonViewPdf = viewBinder.findViewById(R.id.buttonViewPdf);
                AppCompatTextView textViewLive = viewBinder.findViewById(R.id.textViewLive);
                AppCompatTextView textViewSubDescription = viewBinder.findViewById(R.id.textViewSubDescription);
                AppCompatImageView imageViewNews = viewBinder.findViewById(R.id.imageViewNews);
                AppCompatTextView textViewTotalLecture = viewBinder.findViewById(R.id.textViewTotalLecture);
                AppCompatTextView textViewTotalDuration = viewBinder.findViewById(R.id.textViewTotalDuration);
                LinearLayout linearLayoutTotalLecture = viewBinder.findViewById(R.id.linearLayoutVideoLecture);
                linearLayoutTotalLecture.setVisibility(View.GONE);

                textViewNewsHeading.setText(item.getModel().getTitle());
                if (item.getModel().getDescription() != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        textViewSubDescription.setText(Html.fromHtml(item.getModel().getDescription(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        textViewSubDescription.setText(Html.fromHtml(item.getModel().getDescription()));
                    }
                } else {
                    textViewSubDescription.setVisibility(View.GONE);
                }
                GlideApp.with(getContext()).load(item.getModel().getThumbnail())
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(imageViewNews);
                if(item.getModel().isLive() == 1) {
                    textViewLive.setVisibility(View.VISIBLE);
                } else {
                    textViewLive.setVisibility(View.GONE);
                }
                buttonWatch.setOnClickListener(l-> {
                    clickToWatch(item.getModel());
                });
                if(item.getModel().getPdfUrl() != null && (!item.getModel().getPdfUrl().isEmpty())) {
                    buttonViewPdf.setVisibility(View.VISIBLE);
                } else {
                    buttonViewPdf.setVisibility(View.GONE);
                }

                buttonViewPdf.setOnClickListener(l-> {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .addToBackStack("TestListFragment")
                            .replace(R.id.screenContainer,
                                    PdfFragment.newInstance(item.getModel().getPdfUrl(), "", course, category, subCategory))
                            .commit();
                });
            }

        };
    }

    @NotNull
    private ClickEventHook<VideoListItem> clickEventHook() {
        return new ClickEventHook<VideoListItem>() {
            @Override
            public void onClick(@NotNull VideoListItem model, @NotNull ViewBinder viewBinder) {
                clickToWatch(model);
            }
        };
    }

    public void clickToWatch(VideoListItem model) {
        if(isDemo) {
            if (model.getIsPaid().equalsIgnoreCase("0")) {
                try {
                    String[] url = model.getUrl().split("=");
                    Utils.getInstance(getContext()).voidOpen(getActivity(), model.getTitle(), url[1], model
                            , course, category, subCategory, true);
                } catch (Exception e) {

                }
            } else {
                if (isPurchased.equalsIgnoreCase("0")) {
                    showPurchasePopup(courseId, isPurchased);
                } else if (isPurchased.equalsIgnoreCase("1")) {
                    try {
                        String[] url = model.getUrl().split("=");
                        Utils.getInstance(getContext()).voidOpen(getActivity(), model.getTitle(), url[1], model
                                , course, category, subCategory, true);
                    } catch (Exception e) {

                    }
                }
            }
        } else {
            if (model.getIsPaid().equalsIgnoreCase("0")) {
                try {
                    String[] url = model.getUrl().split("=");
                    Utils.getInstance(getContext()).voidOpen(getActivity(), model.getTitle(), url[1], model
                            , course, category, subCategory, model.getIsLive() == 1);
                } catch (Exception e) {

                }
            } else {
                if (isPurchased.equalsIgnoreCase("0")) {
                    showPurchasePopup(courseId, isPurchased);
                } else if (isPurchased.equalsIgnoreCase("1")) {
                    try {
                        String[] url = model.getUrl().split("=");
                        Utils.getInstance(getContext()).voidOpen(getActivity(), model.getTitle(), url[1], model
                                , course, category, subCategory, model.getIsLive() == 1);
                    } catch (Exception e) {

                    }
                }
            }
        }
    }

    private void showPurchasePopup(String courseId, String isPurchased) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(),
                R.style.ThemeOverlay_Catalog_MaterialAlertDialog_OutlinedButton)
                .setTitle("Not enrolled for Class")
                .setMessage("You have not enrolled/purchased the video class package. ")
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.text_purchase_package), ((dialog, which) -> {
                    // redirect to purchasing screen
                    dialog.dismiss();
                    getActivity().onBackPressed();
                }));

        builder.create().show();
    }

    @Override
    public void onNotesItemClicked(PdfListItem item) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack("TestListFragment")
                .replace(R.id.screenContainer,
                        PdfFragment.newInstance(item.getPdfUrl(), "", course, category, subCategory))
                .commit();
    }

    /*private void voidOpen(String title, String videoId, VideoListItem model) {
        List<VideoList> videoLists = new ArrayList<>();
        new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {

            @Override
            public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream,
                                         List<YTSubtitles> subtitles, YoutubeMeta meta) {
                for (YTMedia c : muxedStream) {
                    videoLists.add(new VideoList(c.getUrl(), c.getQualityLabel(), c.getQuality(), title, videoId));
                }

                try {
                    Uri uri = Uri.parse(videoLists.get(0).getUrl().replaceAll(" ", "%20"));
                    JSONObject key = new JSONObject();
                    key.put("name", title);
                    JSONArray array_sample = new JSONArray();
                    JSONObject samples = new JSONObject();
                    samples.put("name", title);
                    samples.put("uri", uri);
                    array_sample.put(samples);
                    key.put("samples", array_sample);
                    JSONArray arrayMain = new JSONArray();
                    arrayMain.put(key);

                    new SampleListLoader(getContext(),
                            model.getChatId(),
                            model.getChatEnabled(),
                            model.getPdfUrl(),arrayMain, videoLists).execute();
                } catch (Exception e) {

                }
            }

            @Override
            public void onExtractionGoesWrong(final ExtractorException e) {
                Toast.makeText(getContext(), "Please try after some video streaming not started", Toast.LENGTH_LONG).show();
            }
        }).useDefaultLogin().Extract(videoId);//"kw-_ZTRv8Uw"
    }*/
}
