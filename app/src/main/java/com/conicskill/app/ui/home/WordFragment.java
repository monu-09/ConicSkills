package com.conicskill.app.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.conicskill.app.R;
import com.conicskill.app.base.BaseFragment;
import com.conicskill.app.data.model.wordDay.Filters;
import com.conicskill.app.data.model.wordDay.WordOfTheDay;
import com.conicskill.app.data.model.wordDay.WordRequest;
import com.conicskill.app.data.model.wordDay.WordRequestData;
import com.conicskill.app.di.util.ViewModelFactory;
import com.conicskill.app.ui.currentAffairs.CurrentAffairsViewModel;
import com.conicskill.app.util.Constants;
import com.conicskill.app.util.ProgressOverlay;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import es.dmoral.toasty.Toasty;

public class WordFragment extends BaseFragment {

    @Inject
    ViewModelFactory viewModelFactory;
    @BindView(R.id.textViewWord)
    AppCompatTextView textViewWord;
    @BindView(R.id.textViewType)
    AppCompatTextView textViewType;
    @BindView(R.id.textViewMeaning)
    AppCompatTextView textViewMeaning;
    @BindView(R.id.textViewTranslation)
    AppCompatTextView textViewTranslation;
    @BindView(R.id.chipGroupSynonyms)
    ChipGroup chipGroupSynonyms;
    @BindView(R.id.chipGroupAntonyms)
    ChipGroup chipGroupAntonyms;
    @BindView(R.id.linearLayoutMain)
    LinearLayout linearLayoutMain;
    private WordOfTheDay wordOfTheDay;
    private ProgressDialog progressOverlay;
    private CurrentAffairsViewModel mViewModel;

    public static WordFragment newInstance(WordOfTheDay wordOfTheDay) {
        if(wordOfTheDay != null) {
            WordFragment wordFragment = new WordFragment();
            Bundle args = new Bundle();
            args.putSerializable("wordOfTheDay", wordOfTheDay);
            wordFragment.setArguments(args);
            return wordFragment;
        } else {
            return new WordFragment();
        }
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_word;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(CurrentAffairsViewModel.class);

        Bundle bundle = getArguments();
        if(bundle != null) {
            if(bundle.getSerializable("wordOfTheDay") != null) {
                wordOfTheDay = (WordOfTheDay) bundle.getSerializable("wordOfTheDay");
            }
        }

        if(wordOfTheDay == null) {
            observeViewModel();
            WordRequest request = new WordRequest();
            WordRequestData requestData = new WordRequestData();
            Filters filters = new Filters();
            filters.setNotesType("1");

            requestData.setFilters(filters);
            request.setToken(mViewModel.tinyDB.getString(Constants.AUTH_TOKEN));
            request.setWordRequestData(requestData);

            mViewModel.callGetWordOfDayAPI(request);
        } else {
            setData();
        }
    }

    public void observeViewModel() {
        mViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                progressOverlay = ProgressOverlay.show(getActivity(), "");
            } else {
                progressOverlay.dismiss();
            }
        });

        mViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                if (error) {
                    linearLayoutMain.setVisibility(View.GONE);
                    progressOverlay.dismiss();
                    Toasty.error(getContext(), "Something went wrong. Please try again.", Toasty.LENGTH_LONG, false).show();
                }
            }
        });

        mViewModel.checkWordOfDay().observe(getViewLifecycleOwner(), wordResponseResponse -> {
            if (wordResponseResponse.code() == HttpsURLConnection.HTTP_OK) {
                if (wordResponseResponse.body().getCode() == HttpsURLConnection.HTTP_OK) {
                    wordOfTheDay = wordResponseResponse.body().getWordData().getWordOfTheDay();
                    setData();
                }
            }
        });
    }

    private void setData() {
        textViewWord.setText(wordOfTheDay.getWord());
        textViewType.setText(wordOfTheDay.getWordType());
        textViewMeaning.setText(wordOfTheDay.getMeaning());
        textViewTranslation.setText(wordOfTheDay.getTranslation());

        String[] synonymsList = wordOfTheDay.getSynonyms().split(",");
        String[] anonymsList = wordOfTheDay.getAntonyms().split(",");

        for (int i = 0; i < synonymsList.length; i++) {
            Chip chip = new Chip(getContext());
            chip.setText(synonymsList[i]);

            chipGroupSynonyms.addView(chip);
        }

        for (int i = 0; i < anonymsList.length; i++) {
            Chip chip = new Chip(getContext());
            chip.setText(anonymsList[i]);

            chipGroupAntonyms.addView(chip);
        }
        linearLayoutMain.setVisibility(View.VISIBLE);
    }
}
