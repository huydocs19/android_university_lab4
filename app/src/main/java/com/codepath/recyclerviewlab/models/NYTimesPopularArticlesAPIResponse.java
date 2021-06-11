package com.codepath.recyclerviewlab.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NYTimesPopularArticlesAPIResponse {
    @SerializedName("status")
    public String status;

    @SerializedName("results")
    public List<PopularArticle> results;
}
