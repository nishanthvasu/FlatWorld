package com.flatworld.NewsDetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.flatworld.ActivityUtils;
import com.flatworld.R;
import com.flatworld.ViewModelHolder;

public class NewsDetailActivity extends AppCompatActivity implements NewsDetailNavigator{
    private NewsDetailViewModel mRecommendationViewModel;
    public static final String TASKDETAIL_VIEWMODEL_TAG = "TASKDETAIL_VIEWMODEL_TAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cont_layout);

        NewsDetailFragment taskDetailFragment = findOrCreateViewFragment();
        mRecommendationViewModel = findOrCreateViewModel();
        mRecommendationViewModel.setNavigator(this);
        // Link View and ViewModel
        taskDetailFragment.setViewModel(mRecommendationViewModel);
    }


    @NonNull
    private NewsDetailFragment findOrCreateViewFragment() {
        // Get the requested task id
        NewsDetailFragment taskDetailFragment = (NewsDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.lay_fr_container);

        if (taskDetailFragment == null) {
            taskDetailFragment = NewsDetailFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    taskDetailFragment, R.id.lay_fr_container);
        }
        return taskDetailFragment;
    }

    @NonNull
    private NewsDetailViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<NewsDetailViewModel> retainedViewModel =
                (ViewModelHolder<NewsDetailViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(TASKDETAIL_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            NewsDetailViewModel viewModel = new NewsDetailViewModel(
                    getApplicationContext());

            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    TASKDETAIL_VIEWMODEL_TAG);
            return viewModel;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
