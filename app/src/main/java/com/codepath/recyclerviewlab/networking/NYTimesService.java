package com.codepath.recyclerviewlab.networking;

import com.codepath.recyclerviewlab.models.NYTimesArticlesAPIResponse;
import com.codepath.recyclerviewlab.models.NYTimesPopularArticlesAPIResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NYTimesService {

    @GET("svc/search/v2/articlesearch.json")
    Call<NYTimesArticlesAPIResponse> getArticlesByQuery(
            @Query("q") String query,
            @Query("page") int page,
            @Query("sort") String sortBy,
            @Query("fl") String filter,
            @Query("begin_date") String beginDate,
            @Query("api-key") String apikey);
    @GET("svc/mostpopular/v2/{category}/{period}.json")
    Call<NYTimesPopularArticlesAPIResponse> getPopularArticlesByQuery(
            @Path("category") String category,
            @Path("period") String period,
            @Query("api-key") String apikey);
}
