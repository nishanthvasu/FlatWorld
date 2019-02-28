package com.flatworld.NewsDetail.RealmModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class NewsDetail extends RealmObject {

    @PrimaryKey
    @Required
    public int id;
    public String title;
}