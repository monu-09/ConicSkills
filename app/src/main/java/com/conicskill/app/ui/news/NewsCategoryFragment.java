package com.conicskill.app.ui.news;

import android.animation.Animator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.news.Article;
import com.conicskill.app.data.model.news.CategoryItem;
import com.conicskill.app.data.model.news.NewsCategory;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.util.GlideApp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idanatz.oneadapter.OneAdapter;
import com.idanatz.oneadapter.external.event_hooks.ClickEventHook;
import com.idanatz.oneadapter.external.interfaces.Item;
import com.idanatz.oneadapter.external.modules.ItemModule;
import com.idanatz.oneadapter.external.modules.ItemModuleConfig;
import com.idanatz.oneadapter.internal.holders.ViewBinder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class NewsCategoryFragment extends BaseFragment {

    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.recyclerViewNewsCategory)
    RecyclerView recyclerViewNews;
    private OneAdapter oneAdapter;

    @Override
    protected int layoutRes() {
        return R.layout.layout_news_category;
    }

    public static NewsCategoryFragment newInstance() {
        return new NewsCategoryFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerViewNews.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerViewNews.setItemAnimator(null);
        oneAdapter = new OneAdapter(recyclerViewNews)
                .attachItemModule(featureItems().addEventHook(clickEventHook()));

        try {
            String json = loadJSONFromAsset(getContext(), "json/api_response.json");
            JSONArray jsonArray = new JSONObject(json).getJSONArray("data");
            Type listType = new TypeToken<List<CategoryItem>>(){}.getType();
            List<CategoryItem> myModelList = new Gson().fromJson(jsonArray.toString(), listType);

            oneAdapter.setItems(myModelList);
        }catch (Exception e) {

        }
    }

    @NotNull
    private ItemModule<CategoryItem> featureItems() {
        return new ItemModule<CategoryItem>() {
            @NotNull
            @Override
            public ItemModuleConfig provideModuleConfig() {
                return new ItemModuleConfig() {
                    @Override
                    public int withLayoutResource() {
                        return R.layout.layout_news;
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
            public void onBind(@NotNull Item<CategoryItem> item, @NotNull ViewBinder viewBinder) {
                AppCompatTextView textViewTitle = viewBinder.findViewById(R.id.textViewTitle);
                AppCompatImageView imageViewCategory = viewBinder.findViewById(R.id.imageViewCategory);

                textViewTitle.setText(item.getModel().getName());
                GlideApp.with(getContext()).load(item.getModel().getImgUrl())
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .centerCrop().into(imageViewCategory);
            }

        };
    }

    @NotNull
    private ClickEventHook<CategoryItem> clickEventHook() {
        return new ClickEventHook<CategoryItem>() {
            @Override
            public void onClick(@NotNull CategoryItem model, @NotNull ViewBinder viewBinder) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack("newsFragment")
                        .replace(R.id.screenContainer, NewsFragment.newInstance(model.getUrl()))
                        .commit();
            }
        };
    }


    public static String loadJSONFromAsset(Context mContext, String fontsJsonFileName) {

        String json = null;
        try {
            InputStream is = mContext.getAssets().open(fontsJsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
}
