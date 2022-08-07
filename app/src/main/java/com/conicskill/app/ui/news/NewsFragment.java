package com.conicskill.app.ui.news;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.news.Article;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.util.GlideApp;
import com.conicskill.app.util.ProgressOverlay;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.idanatz.oneadapter.OneAdapter;
import com.idanatz.oneadapter.external.event_hooks.ClickEventHook;
import com.idanatz.oneadapter.external.interfaces.Item;
import com.idanatz.oneadapter.external.modules.ItemModule;
import com.idanatz.oneadapter.external.modules.ItemModuleConfig;
import com.idanatz.oneadapter.internal.holders.ViewBinder;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class NewsFragment extends BaseFragment {

    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.recyclerViewNews)
    RecyclerView recyclerViewNews;
    @BindView(R.id.shimmerFrameLayout)
    ShimmerFrameLayout shimmerFrameLayout;
    private NewsViewModel mViewModel;
    private OneAdapter oneAdapter;
    private ProgressDialog progressOverlay;
    private static String newsUrlMain = "";


    public static NewsFragment newInstance(String newsUrl) {
        newsUrlMain = "";
        if (newsUrl != null && !(newsUrl.isEmpty())) {
            newsUrlMain = newsUrl;
        }
        return new NewsFragment();
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
                .attachItemModule(featureItems().addEventHook(clickEventHook()));
        mViewModel.callNewsFeedApi(newsUrlMain);
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
                }
            }
        });

        mViewModel.checkFeed().observe(getViewLifecycleOwner(), feedResponse -> {
            if (feedResponse.code() == HttpsURLConnection.HTTP_OK) {
                if (feedResponse.body() != null) {
                    if (feedResponse.body().getArticleList().size() != 0) {
                        oneAdapter.setItems(feedResponse.body().getArticleList());
                        recyclerViewNews.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @NotNull
    private ItemModule<Article> featureItems() {
        return new ItemModule<Article>() {
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
            public void onBind(@NotNull Item<Article> item, @NotNull ViewBinder viewBinder) {
                AppCompatTextView textViewNewsHeading = viewBinder.findViewById(R.id.textViewNewsHeading);
                AppCompatImageView imageViewNews = viewBinder.findViewById(R.id.imageViewNews);

                textViewNewsHeading.setText(item.getModel().getTitle());
                GlideApp.with(getContext()).load(item.getModel().getContent().getUrl())
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(imageViewNews);
            }

        };
    }

    @NotNull
    private ClickEventHook<Article> clickEventHook() {
        return new ClickEventHook<Article>() {
            @Override
            public void onClick(@NotNull Article model, @NotNull ViewBinder viewBinder) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(getActivity(), Uri.parse(model.getLink()));
            }
        };
    }

}
