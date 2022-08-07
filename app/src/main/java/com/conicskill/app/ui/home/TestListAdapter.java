package com.conicskill.app.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.conicskill.app.R;
import com.conicskill.app.data.model.examlisting.TestListItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.MovieVH> {

    private static final String TAG = "MovieAdapter";
    List<TestListItem> testListItems;
    AppCompatActivity mContext;

    public TestListAdapter(AppCompatActivity context, List<TestListItem> movieList) {
        this.testListItems = movieList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MovieVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu, parent, false);
        return new MovieVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieVH holder, int position) {
        TestListItem movie = testListItems.get(position);
        holder.textViewSeriesName.setText(movie.getTopic());
        SeriesItemAdapter adapter = new SeriesItemAdapter(mContext, movie.getTestSeries());
        holder.recyclerViewChild.setAdapter(adapter);
        boolean isExpanded = testListItems.get(position).isExpanded();
        holder.recyclerViewChild.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.textViewSeriesName.setOnClickListener(l->{
            Log.e(TAG, "onBindViewHolder: item clicked " + position );
            testListItems.get(position).setExpanded(!testListItems.get(position).isExpanded());
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return testListItems.size();
    }

    class MovieVH extends RecyclerView.ViewHolder {
        private static final String TAG = "MovieVH";
        @BindView(R.id.recyclerViewChild)
        RecyclerView recyclerViewChild;

        @BindView(R.id.textViewSeriesName)
        AppCompatTextView textViewSeriesName;

        public MovieVH(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(mContext, 2);
            staggeredGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
//                if (adapterRecycler.getItemViewType(position) == 1) {
//                    return 2;
//                } else {
//                    return 1;
//                }
                    return 1;
                }
            });
            recyclerViewChild.setLayoutManager(staggeredGridLayoutManager);
            recyclerViewChild.setHasFixedSize(true);
        }
    }
}