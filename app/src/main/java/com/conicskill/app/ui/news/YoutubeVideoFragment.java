package com.conicskill.app.ui.news;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.conicskill.app.BuildConfig;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.youtubeVideo.DataItem;
import com.conicskill.app.data.model.youtubeVideo.YoutubeChannelData;
import com.conicskill.app.data.model.youtubeVideo.YoutubeChannelRequest;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.util.Constants;
import com.conicskill.app.util.GlideApp;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.idanatz.oneadapter.OneAdapter;
import com.idanatz.oneadapter.external.event_hooks.ClickEventHook;
import com.idanatz.oneadapter.external.holders.EmptyIndicator;
import com.idanatz.oneadapter.external.interfaces.Item;
import com.idanatz.oneadapter.external.modules.EmptinessModule;
import com.idanatz.oneadapter.external.modules.EmptinessModuleConfig;
import com.idanatz.oneadapter.external.modules.ItemModule;
import com.idanatz.oneadapter.external.modules.ItemModuleConfig;
import com.idanatz.oneadapter.external.modules.PagingModule;
import com.idanatz.oneadapter.external.modules.PagingModuleConfig;
import com.idanatz.oneadapter.internal.holders.ViewBinder;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

public class YoutubeVideoFragment extends BaseFragment {

    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.recyclerViewNews)
    RecyclerView recyclerViewNews;
    @BindView(R.id.shimmerFrameLayout)
    ShimmerFrameLayout shimmerFrameLayout;
    private NewsViewModel mViewModel;
    private OneAdapter oneAdapter;
    private ProgressDialog progressOverlay;
    private String nextPageId = "";

    public static YoutubeVideoFragment newInstance() {
        return new YoutubeVideoFragment();
    }

    @Override
    protected int layoutRes() {
        return R.layout.news_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(NewsViewModel.class);
        observeViewModel();
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewNews.setItemAnimator(null);
        oneAdapter = new OneAdapter(recyclerViewNews)
                .attachItemModule(featureItems().addEventHook(clickEventHook()))
//                .attachPagingModule(new PagingModuleImpl())
                .attachEmptinessModule(new EmptinessModuleImpl());

        YoutubeChannelData youtubeChannelData = new YoutubeChannelData();
        youtubeChannelData.setChannelId(BuildConfig.YOUTUBE_CHANNEL_ID);

        YoutubeChannelRequest request = new YoutubeChannelRequest();
        request.setData(youtubeChannelData);
        request.setToken(mViewModel.tinyDB.getString(Constants.AUTH_TOKEN));

        mViewModel.callFetchYoutubeVideosApi(request);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void observeViewModel() {

        mViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                shimmerFrameLayout.setVisibility(View.VISIBLE);
            } else {
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });

        mViewModel.getError().observe(getViewLifecycleOwner(), isError -> {
            if (isError != null) {
                if (isError) {
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recyclerViewNews.setVisibility(View.VISIBLE);
                }
            }
        });

        mViewModel.observeYoutubeResponse().observe(getViewLifecycleOwner(), feedResponse -> {
            if (feedResponse.code() == HttpsURLConnection.HTTP_OK) {
                if (feedResponse.body() != null) {
                    if (feedResponse.body().getData().size() != 0) {
                        recyclerViewNews.setVisibility(View.VISIBLE);
                        oneAdapter.add(feedResponse.body().getData());
//                        nextPageId = feedResponse.body().getNextPageToken();
                    }
                }
            }
        });
    }

    @NotNull
    private ItemModule<DataItem> featureItems() {
        return new ItemModule<DataItem>() {
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
            public void onBind(@NotNull Item<DataItem> item, @NotNull ViewBinder viewBinder) {
                AppCompatTextView textViewNewsHeading = viewBinder.findViewById(R.id.textViewNewsHeading);
                AppCompatTextView textViewSubDescription = viewBinder.findViewById(R.id.textViewSubDescription);
                AppCompatImageView imageViewNews = viewBinder.findViewById(R.id.imageViewNews);

                textViewNewsHeading.setText(item.getModel().getSnippet().getTitle());
                textViewSubDescription.setText(item.getModel().getSnippet().getDescription());
                GlideApp.with(getContext()).load(item.getModel().getSnippet().getThumbnails().getHigh().getUrl())
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(imageViewNews);
            }

        };
    }

    @NotNull
    private ClickEventHook<DataItem> clickEventHook() {
        return new ClickEventHook<DataItem>() {
            @Override
            public void onClick(@NotNull DataItem model, @NotNull ViewBinder viewBinder) {
                if(model.getId().getVideoId() != null) {
                    try {
                        /*Utils.getInstance(getContext()).voidOpen(getActivity(),model.getSnippet().getTitle(),
                                model.getId().getVideoId(),
                                null);*/
                    } catch (Exception e) {
                        Toasty.error(getContext(), "Please check youtube app", Toasty.LENGTH_LONG).show();
                    }
                }
            }
        };
    }

    private  class PagingModuleImpl extends PagingModule {
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
            if(nextPageId != null) {
//                mViewModel.callFetchYoutubeVideosApi(nextPageId);
            }
        }
    }

    static class EmptinessModuleImpl extends EmptinessModule {
        @NonNull
        @Override
        public EmptinessModuleConfig provideModuleConfig() {
            return new EmptinessModuleConfig() {
                @Override
                public int withLayoutResource() {
                    return R.layout.layout_no_internet_dialog;
                }
            };
        }

        @Override
        public void onBind(@NotNull Item<EmptyIndicator> item, @NotNull ViewBinder viewBinder) {
            super.onBind(item, viewBinder);
        }

        @Override
        public void onCreated(@NotNull ViewBinder viewBinder) {
            super.onCreated(viewBinder);
        }

        @Override
        public void onUnbind(@NotNull Item<EmptyIndicator> item, @NotNull ViewBinder viewBinder) {
            super.onUnbind(item, viewBinder);
        }
    }
}
