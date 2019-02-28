package com.flatworld.HomeScreen.DataModel;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsData {
    public List<News> articles;

    public static class News {
        public String title, urlToImage,author,publishedAt,id;

        @BindingAdapter("urlToImage")
        public static void loadImage(ImageView view, String imageUrl) {
            Glide.with(view.getContext())
                    .load(imageUrl)
                    .into(view);
        }
    }
}
