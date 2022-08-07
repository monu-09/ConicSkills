package com.conicskill.app.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.examlisting.Data;
import com.conicskill.app.data.model.examlisting.ExamListRequest;
import com.conicskill.app.data.model.examlisting.Filter;
import com.conicskill.app.data.model.examlisting.TestSeriesItem;
import com.conicskill.app.data.model.payments.ItemListItem;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.ui.PaymentActivity;
import com.conicskill.app.util.CommonView;
import com.conicskill.app.util.Constants;
import com.conicskill.app.util.ProgressOverlay;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

public class MockTestFragment extends BaseFragment implements HomeClickListener {

    private static String testSeriesType = "";
    private static String fromWhere = "";
    //    AdapterSectionRecycler adapterRecycler;
    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.recyclerViewTestList)
    RecyclerView recyclerViewTestList;
    private MockTestViewModel mockTestViewModel;
    private ProgressDialog progressOverlay;
    private TestListAdapter adapterRecycler;



    public static MockTestFragment newInstance(String type, String where) {
        testSeriesType = "";
        if (type != null && !(type.isEmpty())) {
            testSeriesType = type;
        }
        if (where != null && !(where.isEmpty())) {
            fromWhere = where;
        }
        return new MockTestFragment();
    }

    @Override
    protected int layoutRes() {
        return R.layout.mock_test_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mockTestViewModel = new ViewModelProvider(this, viewModelFactory).get(MockTestViewModel.class);
        
        observeViewModel();

        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewTestList.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewTestList.setHasFixedSize(true);

        ExamListRequest examListRequest = new ExamListRequest();
        Filter filter = new Filter();
        filter.setTestSeriesType(testSeriesType);
        Data data = new Data();
        if (!testSeriesType.isEmpty()) {
            data.setFilter(filter);
        }
        examListRequest.setData(data);
        examListRequest.setToken(mockTestViewModel.tinyDB.getString(Constants.AUTH_TOKEN));
        mockTestViewModel.fetchExamList(examListRequest);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void observeViewModel() {
        mockTestViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                progressOverlay = ProgressOverlay.show(getActivity(), "Fetching Exam List");
            } else {
                progressOverlay.dismiss();
            }
        });

        mockTestViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                if (error) {
                    progressOverlay.dismiss();
                    Toasty.error(getContext(), "Failed to register. Please try again.", Toasty.LENGTH_LONG, false).show();
                }
            }
        });

        mockTestViewModel.getExamList().observe(getViewLifecycleOwner(), examListResponseResponse -> {
            try {
                if (examListResponseResponse != null) {
                    if (examListResponseResponse.code() == HttpsURLConnection.HTTP_OK) {
                        if (examListResponseResponse.body().isError()) {

                        } else {
                            if (examListResponseResponse.body().getCode() == HttpsURLConnection.HTTP_OK) {
                                if (examListResponseResponse.body().getData().getTestList().size() != 0) {
//                                    adapterRecycler = new AdapterSectionRecycler(getContext(), examListResponseResponse.body().getData().getTestList(), this::homeClicked);
//                                    recyclerViewTestList.setAdapter(adapterRecycler);
//                                    recyclerViewTestList.setVisibility(View.VISIBLE);

                                    if (fromWhere.equalsIgnoreCase("quiz")) {
                                        SeriesItemAdapter adapterRecycler = new SeriesItemAdapter(getBaseActivity(), examListResponseResponse.body().getData().getTestList().get(0).getTestSeries());
                                        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(getContext(), 2);
                                        recyclerViewTestList.setLayoutManager(staggeredGridLayoutManager);
                                        recyclerViewTestList.setHasFixedSize(true);
                                        recyclerViewTestList.setAdapter(adapterRecycler);
                                    } else {
                                        adapterRecycler = new TestListAdapter(getBaseActivity(), examListResponseResponse.body().getData().getTestList());
                                        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(getContext(), 1);
                                        recyclerViewTestList.setLayoutManager(staggeredGridLayoutManager);
                                        recyclerViewTestList.setHasFixedSize(true);
                                        recyclerViewTestList.setAdapter(adapterRecycler);
                                    }
                                    recyclerViewTestList.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("Exam list", "observeViewModel: " + e.getLocalizedMessage());
            }

        });
    }

    @Override
    public void homeClicked(TestSeriesItem testSeriesItem) {
        if (testSeriesItem.getPurchased().equalsIgnoreCase("1")) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack("TestListFragment")
                    .replace(R.id.screenContainer, TestListingFragment.newInstance(testSeriesItem.getTestSeriesId(), testSeriesItem.getImageUrl()))
                    .commit();
        } else {
            showPurchasePopup(testSeriesItem.getTestSeriesId(), "0", testSeriesItem.getTitle(), testSeriesItem.getPrice());
        }
    }

    private void showPurchasePopup(String itemId, String isPurchased, String title, String price) {
        CommonView.INSTANCE.showRoundedAlertDialog(requireActivity(), "Not Purchased Test Series",
                "You have not purchased the test series package.\n\nPlease purchase it first in order to continue.\n\nIt's price is ₹ " + price,
                getString(R.string.text_purchase_package), getString(R.string.text_cancel),
                result -> {
                    if((Boolean) result) {
                        Intent intent = new Intent(getActivity(), PaymentActivity.class);
                        ItemListItem itemListItem = new ItemListItem();
                        itemListItem.setTestSeriesId(itemId);
                        itemListItem.setItemType("testSeries");

                        ArrayList<ItemListItem> paymentArrayList = new ArrayList<>();
                        paymentArrayList.add(itemListItem);
                        intent.putExtra("payments", paymentArrayList);
                        intent.putExtra("price", price);
                        intent.putExtra("name", title + " || " + itemId);
                        startActivity(intent);
                    }
                });
    }
}
