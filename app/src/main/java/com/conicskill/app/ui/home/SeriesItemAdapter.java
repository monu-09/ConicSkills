package com.conicskill.app.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.conicskill.app.R;
import com.conicskill.app.data.model.examlisting.TestListItem;
import com.conicskill.app.data.model.examlisting.TestSeriesItem;
import com.conicskill.app.data.model.payments.ItemListItem;
import com.conicskill.app.ui.PaymentActivity;
import com.conicskill.app.util.CommonView;
import com.conicskill.app.util.GlideApp;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SeriesItemAdapter extends RecyclerView.Adapter<SeriesItemAdapter.MovieVH> {

    private static final String TAG = "MovieAdapter";
    List<TestSeriesItem> testListItems;
    private AppCompatActivity mContext;

    public SeriesItemAdapter(AppCompatActivity context, List<TestSeriesItem> movieList) {
        this.testListItems = movieList;
        this.mContext =context;
    }

    @NonNull
    @Override
    public MovieVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feature_list, parent, false);
        return new MovieVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieVH holder, int position) {
        TestSeriesItem movie = testListItems.get(position);
        GlideApp.with(mContext).load(movie.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.featureIcon);
        holder.featureName.setText(movie.getTitle());
        holder.itemView.setOnClickListener(l->{
            if (movie.getPurchased().equalsIgnoreCase("1") || movie.getPrice().equalsIgnoreCase("0")) {
                mContext.getSupportFragmentManager().beginTransaction()
                        .addToBackStack("TestListFragment")
                        .replace(R.id.screenContainer, TestListingFragment.newInstance(movie.getTestSeriesId(), movie.getImageUrl()))
                        .commit();
            } else {
                showPurchasePopup(movie.getTestSeriesId(), "0", movie.getTitle() , movie.getPrice());
            }
        });
    }

    @Override
    public int getItemCount() {
        return testListItems.size();
    }

    class MovieVH extends RecyclerView.ViewHolder {
        private static final String TAG = "MovieVH";

        @BindView(R.id.featureIcon)
        AppCompatImageView featureIcon;

        @BindView(R.id.featureName)
        AppCompatTextView featureName;

        public MovieVH(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    private void showPurchasePopup(String itemId, String isPurchased, String title, String price) {
        CommonView.INSTANCE.showRoundedAlertDialog(mContext, "Not Purchased Test Series",
                "You have not purchased the test series package.\n\nPlease purchase it first in order to continue.\n\nIt's price is ₹ " + price,
                mContext.getString(R.string.text_purchase_package), mContext.getString(R.string.text_cancel),
                result -> {
                    if((Boolean) result) {
                        Intent intent = new Intent(mContext, PaymentActivity.class);
                        ItemListItem itemListItem = new ItemListItem();
                        itemListItem.setTestSeriesId(itemId);
                        itemListItem.setItemType("testSeries");

                        ArrayList<ItemListItem> paymentArrayList = new ArrayList<>();
                        paymentArrayList.add(itemListItem);
                        intent.putExtra("payments", paymentArrayList);
                        intent.putExtra("price", price);
                        intent.putExtra("name", title + " || " + itemId);
                        mContext.startActivity(intent);
                    }
                });
    }
}