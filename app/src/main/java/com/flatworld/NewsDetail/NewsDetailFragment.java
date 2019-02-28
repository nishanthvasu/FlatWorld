package com.flatworld.NewsDetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flatworld.NewsDetail.RealmModel.NewsDetail;
import com.flatworld.R;
import com.flatworld.SessionManager;
import com.flatworld.databinding.NewsdetailfragmentBinding;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class NewsDetailFragment extends Fragment {

    NewsDetailViewModel mViewModel;
    NewsdetailfragmentBinding viewDataBinding;
    Realm realm = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newsdetailfragment, container, false);
        viewDataBinding = NewsdetailfragmentBinding.bind(view);
        viewDataBinding.setViewmodel(mViewModel);
        setHasOptionsMenu(true);
        return view;
    }

    public static NewsDetailFragment newInstance() {
        return new NewsDetailFragment();
    }

    public void setViewModel(NewsDetailViewModel taskViewModel) {
        mViewModel = taskViewModel;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        realm = Realm.getDefaultInstance();
        viewDataBinding.tvTitle.setText(SessionManager.getSession("NAME", getActivity()));
        viewDataBinding.tvAuthor.setText(SessionManager.getSession("AUTHOR", getActivity()));
        viewDataBinding.tvPublished.setText(SessionManager.getSession("PUBLISHED", getActivity()));
        Glide.with(getActivity())
                .load(SessionManager.getSession("URL", getActivity()))
                .into(viewDataBinding.ivDetailimg);

        if (getBook(SessionManager.getSession("ID", getActivity())).equals("true"))
            viewDataBinding.ivBookmark.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_bookmark_highlight));
        else
            viewDataBinding.ivBookmark.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_bookmark));

        viewDataBinding.ivBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookMark(SessionManager.getSession("NAME", getActivity()), SessionManager.getSession("ID", getActivity()));
            }
        });
    }

    private void bookMark(final String title, final String id) {

        try {

            realm.beginTransaction();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {
                        NewsDetail employee = new NewsDetail();
                        employee.title = title;
                        employee.id = Integer.parseInt(id);
                        realm.copyToRealm(employee);
                        realm.commitTransaction();
                    } catch (RealmPrimaryKeyConstraintException e) {
                        Toast.makeText(getActivity(), "Primary Key already exists", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public NewsDetail getBook(String id) {
        return realm.where(NewsDetail.class).equalTo("id", id).findFirst();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }
}
