package com.codepath.recyclerviewlab;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.recyclerviewlab.models.Article;
import com.codepath.recyclerviewlab.networking.CallbackResponse;
import com.codepath.recyclerviewlab.networking.NYTimesApiClient;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class ArticleResultFragment extends Fragment {

    private NYTimesApiClient client = new NYTimesApiClient();
    private RecyclerView rvArticles;
    private ContentLoadingProgressBar progressSpinner;
    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;
    private String savedQuery;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleResultFragment() {
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        SearchView item = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem menuItem = menu.findItem(R.id.action_search);
        item.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                loadNewArticlesByQuery(query);



                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {

                rvArticles.setVisibility(View.INVISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                ArticleResultsRecyclerViewAdapter adapter = (ArticleResultsRecyclerViewAdapter) rvArticles.getAdapter();
                adapter.setNewArticles(new ArrayList<Article>());

                // notify dataset changed will tell your adapter that it's data has changed and refresh the view layout
                adapter.notifyDataSetChanged();
                rvArticles.setVisibility(View.INVISIBLE);
                MainActivity.rvPopularArticles.setVisibility(View.VISIBLE);

                return true;
            }
        });



    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_result_list, container, false);



        rvArticles = view.findViewById(R.id.list);

        progressSpinner = view.findViewById(R.id.progress);
        Context context = view.getContext();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvArticles.setLayoutManager(linearLayoutManager);
        rvArticles.setAdapter(new ArticleResultsRecyclerViewAdapter(getContext()));



        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadArticlesByPage(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvArticles.addOnScrollListener(scrollListener);


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void loadNewArticlesByQuery(String query) {

        Log.d("ArticleResultFragment", "loading articles for query " + query);
        // Toast.makeText(getContext(), "Loading articles for \'" + query + "\'", Toast.LENGTH_SHORT).show();
        progressSpinner.show();
        savedQuery = query;
        // TODO(Checkpoint 3): Implement this method to populate articles
        client.getArticlesByQuery(new CallbackResponse<List<Article>>() {
            @Override
            public void onSuccess(List<Article> models) {
                progressSpinner.hide();
                Log.d("ArticleResultFragment", "Successfully loaded articles");
                ArticleResultsRecyclerViewAdapter adapter = (ArticleResultsRecyclerViewAdapter) rvArticles.getAdapter();
                adapter.setNewArticles(models);
                // notify dataset changed will tell your adapter that it's data has changed and refresh the view layout
                adapter.notifyDataSetChanged();
                rvArticles.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Throwable error) {
                progressSpinner.hide();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("ArticleResultFragment", "Failure loading articles " + error.getMessage());
            }
        }, query);
    }


    private void loadArticlesByPage(final int page) {
        client.getArticlesByQuery(new CallbackResponse<List<Article>>() {
            @Override
            public void onSuccess(List<Article> models) {
                ArticleResultsRecyclerViewAdapter adapter = (ArticleResultsRecyclerViewAdapter) rvArticles.getAdapter();
                adapter.addArticles(models);
                adapter.notifyDataSetChanged();
                Log.d("ArticleResultFragment", String.format("Successfully loaded articles from page %d", page));
            }

            @Override
            public void onFailure(Throwable error) {
                ArticleResultsRecyclerViewAdapter adapter = (ArticleResultsRecyclerViewAdapter) rvArticles.getAdapter();
                adapter.progressSpinner.hide();
                Log.d("ArticleResultFragment", "Failure load article " + error.getMessage());
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT);
            }
            // TODO: you'll need to create a class member variable to save each query you search
        }, savedQuery, page);
    }


}
