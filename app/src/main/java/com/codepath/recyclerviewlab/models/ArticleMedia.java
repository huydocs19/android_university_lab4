package com.codepath.recyclerviewlab.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticleMedia {
    @SerializedName("type")
    public String type;

    @SerializedName("media-metadata")
    public List<MetaData> mediaMetaData;


}
