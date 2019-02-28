package com.flatworld.HomeScreen;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.flatworld.Constants;
import com.flatworld.HomeScreen.DataModel.NewsData;
import com.flatworld.HomeScreen.DataModel.WeatherData;
import com.flatworld.R;
import com.flatworld.SessionManager;

import java.util.ArrayList;
import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeScreenViewModel extends Observable {

    private Context mContext;
    private ArrayList<NewsData.News> newsList = new ArrayList<>();

    public HomeScreenViewModel(Context context) {
        mContext = context;
    }
    private final MutableLiveData<String> summary = new MutableLiveData<String>();
    private final MutableLiveData<String> temperature = new MutableLiveData<String>();


    public void summary(String summarye) {
        summary.setValue(summarye);
    }

    public MutableLiveData<String> getsummary() {
        return summary;
    }


    public void temperature(String temperatur) {
        temperature.setValue(temperatur);
    }

    public MutableLiveData<String> gettemperature() {
        return temperature;
    }


    public final ObservableField<String> snackbarText = new ObservableField<>();

    public void startTask() {
        getNewsList();
        getWeatherData();
    }

    public ArrayList<NewsData.News> getUserList() {
        return newsList;
    }

    public void getNewsList() {
        Call<NewsData> call = Constants.getClient().getNewsList("XMLHttpRequest");
        call.enqueue(new Callback<NewsData>() {
            @Override
            public void onResponse(@NonNull Call<NewsData> call, @NonNull Response<NewsData> response) {
                if (response.body() != null) {
                    newsList.addAll(response.body().articles);
                    setChanged();
                    notifyObservers();
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsData> call, @NonNull Throwable t) {
                snackbarText.set(mContext.getResources().getString(R.string.txt_servernotreach));
            }
        });
    }

    public void getWeatherData() {
        Call<WeatherData> call = Constants.getClientWeather().getWeather("XMLHttpRequest");
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(@NonNull Call<WeatherData> call, @NonNull Response<WeatherData> response) {
                if (response.body() != null) {
                    WeatherData model = response.body();
                    summary(model.currently.summary);
                    temperature(model.currently.temperature);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherData> call, @NonNull Throwable t) {
                snackbarText.set(mContext.getResources().getString(R.string.txt_servernotreach));
            }
        });
    }
}
