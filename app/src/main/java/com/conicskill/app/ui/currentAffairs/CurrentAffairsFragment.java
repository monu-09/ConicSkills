package com.conicskill.app.ui.currentAffairs;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsItem;
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsRequest;
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsRequestData;
import com.conicskill.app.data.model.exam.Languages;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.ui.exam.LanguagesArrayAdapter;
import com.conicskill.app.util.Constants;
import com.conicskill.app.util.GlideApp;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputLayout;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class CurrentAffairsFragment extends BaseFragment {

    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.recyclerViewNews)
    RecyclerView recyclerViewNews;
    @BindView(R.id.shimmerFrameLayout)
    ShimmerFrameLayout shimmerFrameLayout;
    @BindView(R.id.textLayoutLanguages)
    TextInputLayout textLayoutLanguages;

    @BindView(R.id.languagesTextView)
    AutoCompleteTextView languagesTextView;
    private List<Languages> languagesList = new ArrayList<>();

    private CurrentAffairsViewModel mViewModel;
    private OneAdapter oneAdapter;
    private ProgressDialog progressOverlay;
    private String language = "en";
    private LanguagesArrayAdapter languagesArrayAdapter;


    public static CurrentAffairsFragment newInstance() {
        return new CurrentAffairsFragment();
    }

    @Override
    protected int layoutRes() {
        return R.layout.news_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(CurrentAffairsViewModel.class);
        observeViewModel();

        languagesList.clear();
        Languages languages = new Languages();
        languages.setLanguageId("en");
        languages.setLanguage("English");

        Languages languages1 = new Languages();
        languages1.setLanguageId("hi");
        languages1.setLanguage("Hindi");

        languagesList.add(languages);
        languagesList.add(languages1);

        textLayoutLanguages.setVisibility(View.VISIBLE);
        languagesArrayAdapter = new LanguagesArrayAdapter(getContext(), R.layout.dropdown_menu_popup_item, languagesList);
        languagesTextView.setAdapter(languagesArrayAdapter);
        languagesTextView.setText(languagesList.get(0).getLanguage());
        languagesTextView.setSelection(0);
        languagesTextView.setOnItemClickListener((AdapterView<?> parent, View view2, int position, long id) -> {
            languagesTextView.setText(languagesList.get(position).getLanguage());
            language = languagesList.get(position).getLanguageId();
            textLayoutLanguages.clearFocus();
            CurrentAffairsRequestData currentAffairsData = new CurrentAffairsRequestData();
            currentAffairsData.setLangCode(language);
            CurrentAffairsRequest currentAffairsRequest = new CurrentAffairsRequest();
            currentAffairsRequest.setToken(mViewModel.tinyDB.getString(Constants.AUTH_TOKEN));
            currentAffairsRequest.setCurrentAffairsRequestData(currentAffairsData);
            mViewModel.callFetchCurrentAffairsAPI(currentAffairsRequest);
        });

        recyclerViewNews.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewNews.setItemAnimator(null);
        oneAdapter = new OneAdapter(recyclerViewNews)
                .attachItemModule(featureItems().addEventHook(clickEventHook()));

        CurrentAffairsRequestData currentAffairsData = new CurrentAffairsRequestData();
        currentAffairsData.setLangCode(language);
        CurrentAffairsRequest currentAffairsRequest = new CurrentAffairsRequest();
        currentAffairsRequest.setToken(mViewModel.tinyDB.getString(Constants.AUTH_TOKEN));
        currentAffairsRequest.setCurrentAffairsRequestData(currentAffairsData);
        mViewModel.callFetchCurrentAffairsAPI(currentAffairsRequest);
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

        mViewModel.checkFeed().observe(getViewLifecycleOwner(), feedResponse -> {
            if (feedResponse.code() == HttpsURLConnection.HTTP_OK) {
                if (feedResponse.body() != null) {
                    if (feedResponse.body().getCurrentAffairsData().getCurrentAffairs().size() != 0) {
                        oneAdapter.setItems(feedResponse.body().getCurrentAffairsData().getCurrentAffairs());
                        recyclerViewNews.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @NotNull
    private ItemModule<CurrentAffairsItem> featureItems() {
        return new ItemModule<CurrentAffairsItem>() {
            @NotNull
            @Override
            public ItemModuleConfig provideModuleConfig() {
                return new ItemModuleConfig() {
                    @Override
                    public int withLayoutResource() {
                        return R.layout.item_current_affairs;
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
            public void onBind(@NotNull Item<CurrentAffairsItem> item, @NotNull ViewBinder viewBinder) {
                AppCompatTextView textViewCurrentHeading = viewBinder.findViewById(R.id.textViewCurrentHeading);
                AppCompatTextView textViewAffairDescription = viewBinder.findViewById(R.id.textViewAffairDescription);
                AppCompatImageView imageViewCurrentAffairs = viewBinder.findViewById(R.id.imageViewCurrentAffairs);

                textViewCurrentHeading.setText(item.getModel().getTitle());
                textViewAffairDescription.setText(item.getModel().getSmallBody());
                GlideApp.with(getContext()).load(item.getModel().getImageUrl()).into(imageViewCurrentAffairs);
            }

        };
    }

    @NotNull
    private ClickEventHook<CurrentAffairsItem> clickEventHook() {
        return new ClickEventHook<CurrentAffairsItem>() {
            @Override
            public void onClick(@NotNull CurrentAffairsItem model, @NotNull ViewBinder viewBinder) {
//                Log.e("Item clicked",""+model);
                mViewModel.setSelectedCurrentAffair(model);
                CurrentAffairsDetailFragment currentAffairsDetailFragment = new CurrentAffairsDetailFragment(model);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack("currentDetailFragment")
                        .replace(R.id.screenContainer,currentAffairsDetailFragment).commit();
                /*Intent intent = new Intent(getActivity(), CADetailActivity.class);
                intent.putExtra("currentAffairId",model.getCurrentAffairId());
                startActivity(intent);*/
            }
        };
    }

}
