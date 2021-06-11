package com.codepath.recyclerviewlab.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PopularArticle {
    @SerializedName("id")
    public String id;

    @SerializedName("title")
    public String title;

    @SerializedName("url")
    public String webUrl;

    @SerializedName("media")
    public List<ArticleMedia> multimedia;

    @SerializedName("abstract")
    public String articleAbstract;

    @SerializedName("published_date")
    public String publishDate;
}
