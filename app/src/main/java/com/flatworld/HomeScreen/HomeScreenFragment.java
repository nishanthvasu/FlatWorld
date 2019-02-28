package com.flatworld.HomeScreen;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.flatworld.HomeScreen.DataModel.NewsData;
import com.flatworld.NewsDetail.NewsDetailActivity;
import com.flatworld.R;
import com.flatworld.SessionManager;
import com.flatworld.databinding.HomescreenfragmentBinding;
import com.flatworld.databinding.ItemNewsBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Observer;

public class HomeScreenFragment extends Fragment implements Observer, View.OnClickListener {

    HomeScreenViewModel mViewModel;
    HomescreenfragmentBinding viewDataBinding;
    private AppCompatImageView iv_headerMenu;
    private LinearLayout ll_home, ll_logout, ll_root;
    AppCompatTextView tv_summary, tv_temperature, tv_day;

    private void closeDrawer() {
        if (viewDataBinding.dlNavigationDrawer != null) {
            if (viewDataBinding.dlNavigationDrawer.isDrawerVisible(Gravity.START)) {
                viewDataBinding.dlNavigationDrawer.closeDrawer(Gravity.START);
            } else {
                viewDataBinding.dlNavigationDrawer.openDrawer(Gravity.START);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_headerMenu:
                closeDrawer();
                break;
            case R.id.ll_home:
                closeDrawer();
                break;
            case R.id.ll_logout:
                closeDrawer();
                Objects.requireNonNull(getActivity()).finishAffinity();
                System.exit(0);
                break;
            case R.id.ll_root:
                break;
            default:
                break;
        }
    }

    public static HomeScreenFragment newInstance() {
        return new HomeScreenFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homescreenfragment, container, false);
        viewDataBinding = HomescreenfragmentBinding.bind(view);
        viewDataBinding.setViewmodel(mViewModel);
        setHasOptionsMenu(true);
        iv_headerMenu = view.findViewById(R.id.iv_headerMenu);
        ll_home = view.findViewById(R.id.ll_home);
        ll_logout = view.findViewById(R.id.ll_logout);
        ll_root = view.findViewById(R.id.ll_root);
        tv_summary = view.findViewById(R.id.tv_summary);
        tv_temperature = view.findViewById(R.id.tv_temperature);
        tv_day = view.findViewById(R.id.tv_day);
        return view;
    }

    public void setViewModel(HomeScreenViewModel taskViewModel) {
        mViewModel = taskViewModel;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpObserver(mViewModel);
        mViewModel.startTask();
        iv_headerMenu.setOnClickListener(this);
        ll_home.setOnClickListener(this);
        ll_logout.setOnClickListener(this);
        ll_root.setOnClickListener(this);
        setUpListOfUsersView(viewDataBinding.rvNewslist);

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mViewModel.getsummary().observe(this, new android.arch.lifecycle.Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tv_summary.setText(s);
            }
        });

        mViewModel.gettemperature().observe(this, new android.arch.lifecycle.Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tv_temperature.setText(String.valueOf(String.format("%.0f",convertFahrenheitToCelcius(Float.parseFloat(s)))));
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date);
        tv_day.setText(dayOfTheWeek + " " + formattedDate.replace("-", " "));
    }

    // Converts to celcius
    private float convertFahrenheitToCelcius(float fahrenheit) {
        return ((fahrenheit - 32) * 5 / 9);
    }

    public void setUpObserver(java.util.Observable observable) {
        observable.addObserver(this);
    }

    // set up the list of user with recycler view
    private void setUpListOfUsersView(RecyclerView listUser) {
        UserAdapter userAdapter = new UserAdapter(mViewModel.getUserList());
        listUser.setAdapter(userAdapter);
        listUser.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    }

    @Override
    public void update(java.util.Observable observable, Object o) {
        if (observable instanceof HomeScreenViewModel) {
            UserAdapter userAdapter = (UserAdapter) viewDataBinding.rvNewslist.getAdapter();
            HomeScreenViewModel userViewModel = (HomeScreenViewModel) observable;
            userAdapter.setUserList(userViewModel.getUserList());
        }
    }

    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserAdapterViewHolder> {

        private ArrayList<NewsData.News> userList = new ArrayList<>();

        UserAdapter(ArrayList<NewsData.News> userList) {
            this.userList = userList;
        }

        @Override
        public UserAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemNewsBinding itemUserBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_news, parent, false);
            return new UserAdapterViewHolder(itemUserBinding);
        }

        @Override
        public void onBindViewHolder(UserAdapterViewHolder holder, final int position) {
            holder.bindUser(userList.get(position));
            holder.mItemUserBinding.llAdaptermain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SessionManager.saveSession("NAME", userList.get(position).title, getActivity());
                    SessionManager.saveSession("URL", userList.get(position).urlToImage, getActivity());
                    SessionManager.saveSession("AUTHOR", userList.get(position).author, getActivity());
                    SessionManager.saveSession("PUBLISHED", userList.get(position).publishedAt, getActivity());
                    SessionManager.saveSession("ID", userList.get(position).id, getActivity());
                    startActivity(new Intent(getActivity(), NewsDetailActivity.class));
                }
            });
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }

        void setUserList(ArrayList<NewsData.News> userLis) {
            userList = userLis;
            notifyDataSetChanged();
        }

        class UserAdapterViewHolder extends RecyclerView.ViewHolder {

            ItemNewsBinding mItemUserBinding;

            UserAdapterViewHolder(ItemNewsBinding itemUserBinding) {
                super(itemUserBinding.getRoot());
                this.mItemUserBinding = itemUserBinding;
            }

            void bindUser(NewsData.News dishes) {
                mItemUserBinding.setDishesmodel(dishes);
            }
        }
    }
}
