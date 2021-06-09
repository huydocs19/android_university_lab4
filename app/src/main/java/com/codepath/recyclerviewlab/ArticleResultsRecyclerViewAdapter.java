package com.codepath.recyclerviewlab;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.recyclerviewlab.models.Article;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ArticleResultsRecyclerViewAdapter extends RecyclerView.Adapter<ArticleResultsRecyclerViewAdapter.ArticleViewHolder>{
    private final List<Article> articleList = new ArrayList<>();
    // Define a static int for each view type, loading = showing the loading spinner at the end of the list
    public static final int VIEW_TYPE_LOADING = 0;
    // article = each article that shows up
    public static final int VIEW_TYPE_ARTICLE = 1;


    public ArticleResultsRecyclerViewAdapter() {

    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        // Inflate the custom layout
        View articleView = inflater.inflate(R.layout.fragment_article_result, parent, false);
        // Return a new holder instance
        ArticleViewHolder viewHolder = new ArticleViewHolder(articleView);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull ArticleResultsRecyclerViewAdapter.ArticleViewHolder holder, int position) {
        // Get the data model based on position
        Article article = articleList.get(position);

        // Set item views based on views and data model
        holder.headlineView.setText(article.headline.main);
        holder.snippetView.setText(article.snippet);

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date date = new Date();
        try {
            date = (Date)formatter.parse(article.publishDate);
        } catch (ParseException e) {
            Log.e("ArticleResultsRVAdapter", "onBindViewHolder", e);

        }
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy/MM/dd");
        String publishedDate = newFormat.format(date);
        holder.dateView.setText(publishedDate);
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        public TextView headlineView;
        public TextView snippetView;
        public TextView dateView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ArticleViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            dateView = itemView.findViewById(R.id.article_pub_date);
            headlineView = itemView.findViewById(R.id.article_headline);
            snippetView = itemView.findViewById(R.id.article_snippet);
        }
    }

    // This method clears the existing dataset and adds new articles
    void setNewArticles(List<Article> articles) {
        articleList.clear();
        articleList.addAll(articles);
    }

    // This method adds all articles to the existing dataset
    void addArticles(List<Article> articles) {
        articleList.addAll(articles);
    }


}
