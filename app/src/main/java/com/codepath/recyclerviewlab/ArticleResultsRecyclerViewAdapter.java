package com.codepath.recyclerviewlab;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codepath.recyclerviewlab.models.Article;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ArticleResultsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final List<Article> articleList = new ArrayList<>();
    // Define a static int for each view type, loading = showing the loading spinner at the end of the list
    public static final int VIEW_TYPE_LOADING = 0;
    // article = each article that shows up
    public static final int VIEW_TYPE_ARTICLE = 1;

    public static final int VIEW_TYPE_FIRST_PAGE_ARTICLE = 2;
    public ContentLoadingProgressBar progressSpinner;
    public Context context;



    public ArticleResultsRecyclerViewAdapter(Context context) {
        this.context = context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_ARTICLE) {
            // Inflate the custom layout
            View articleView = inflater.inflate(R.layout.fragment_article_result, parent, false);
            // Return a new holder instance
            return new ArticleViewHolder(articleView);

        } else if (viewType == VIEW_TYPE_FIRST_PAGE_ARTICLE) {

            // Inflate the custom layout
            View articleView = inflater.inflate(R.layout.fragment_article_result_first_page, parent, false);
            // Return a new holder instance
            return new FirstPageArticleViewHolder(articleView);
        } else {
            // Inflate the custom layout
            View articleView = inflater.inflate(R.layout.article_progress, parent, false);
            // Return a new holder instance
            return new LoadingViewHolder(articleView);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof FirstPageArticleViewHolder) {
            FirstPageArticleViewHolder firstPageArticleViewHolder = (FirstPageArticleViewHolder) holder;
            firstPageArticleViewHolder.firstPageHeader.setText(holder.itemView.getContext().getString(R.string.first_page, articleList.get(position).sectionName));

        }
        if (holder instanceof ArticleViewHolder) {
            // Get the data model based on position
            Article article = articleList.get(position);

            final ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;

            // Set item views based on views and data model
            articleViewHolder.headlineView.setText(article.headline.main);
            articleViewHolder.snippetView.setText(article.snippet);


            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
            Date date = new Date();
            try {
                date = (Date)formatter.parse(article.publishDate);
            } catch (ParseException e) {
                Log.e("ArticleResultsRVAdapter", "onBindViewHolder", e);

            }
            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
            String publishedDate = newFormat.format(date);
            articleViewHolder.dateView.setText(publishedDate);


            if (article.multimedia.size() > 0) {
                Glide.with(context).load("http://static01.nytimes.com/" + article.multimedia.get(0).url)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.imagenotfound)
                        .listener(new RequestListener<Drawable>() {
                                      ArticleViewHolder aViewHolder = articleViewHolder;
                                      @Override
                                      public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                          // log exception
                                          Log.e("ArticleResultsRVAdapter", "Error loading image", e);
                                          aViewHolder.articleImageView.setVisibility(View.GONE);
                                          return false; // important to return false so the error placeholder can be placed
                                      }

                                      @Override
                                      public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                          return false;
                                      }
                                  }
                        )
                        .into(articleViewHolder.articleImageView);
            } else {
                articleViewHolder.articleImageView.setVisibility(View.GONE);
            }




        } else {
            progressSpinner = holder.itemView.findViewById(R.id.progress);
            progressSpinner.show();
        }

    }

    @Override
    public int getItemCount() {
        return (articleList.size() == 0) ? 0 : articleList.size() + 1;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        public TextView headlineView;
        public TextView snippetView;
        public TextView dateView;
        public ImageView articleImageView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ArticleViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            dateView = itemView.findViewById(R.id.article_pub_date);
            headlineView = itemView.findViewById(R.id.article_headline);
            snippetView = itemView.findViewById(R.id.article_snippet);
            articleImageView  = itemView.findViewById(R.id.article_photo);
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

    // rather than creating a completely new ViewHolder, we can extend the existing ArticleViewHolder
    public class FirstPageArticleViewHolder extends ArticleViewHolder {
        public TextView firstPageHeader;

        FirstPageArticleViewHolder(View view) {
            super(view);
            firstPageHeader = view.findViewById(R.id.first_page_header);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        LoadingViewHolder(@NonNull View view) {
            super(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= articleList.size()) {
            return VIEW_TYPE_LOADING;
        } else if ("1".equals(articleList.get(position).printPage)) {
            return VIEW_TYPE_FIRST_PAGE_ARTICLE;
        } else {
            return VIEW_TYPE_ARTICLE;
        }


    }


}
