package com.flatworld.HomeScreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.flatworld.ActivityUtils;
import com.flatworld.R;
import com.flatworld.ViewModelHolder;

public class HomeScreenActivity extends AppCompatActivity {

    private HomeScreenViewModel mRecommendationViewModel;
    public static final String TASKDETAIL_VIEWMODEL_TAG = "TASKDETAIL_VIEWMODEL_TAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cont_layout);
        HomeScreenFragment taskDetailFragment = findOrCreateViewFragment();
        mRecommendationViewModel = findOrCreateViewModel();
        // Link View and ViewModel
        taskDetailFragment.setViewModel(mRecommendationViewModel);
    }

    @NonNull
    private HomeScreenFragment findOrCreateViewFragment() {
        // Get the requested task id
        HomeScreenFragment taskDetailFragment = (HomeScreenFragment) getSupportFragmentManager()
                .findFragmentById(R.id.lay_fr_container);

        if (taskDetailFragment == null) {
            taskDetailFragment = HomeScreenFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    taskDetailFragment, R.id.lay_fr_container);
        }
        return taskDetailFragment;
    }

    @NonNull
    private HomeScreenViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<HomeScreenViewModel> retainedViewModel =
                (ViewModelHolder<HomeScreenViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(TASKDETAIL_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            HomeScreenViewModel viewModel = new HomeScreenViewModel(
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
        this.finishAffinity();
        System.exit(0);
    }
}



