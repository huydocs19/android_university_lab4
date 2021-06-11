package com.codepath.recyclerviewlab;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.recyclerviewlab.models.PopularArticle;
import com.codepath.recyclerviewlab.networking.CallbackResponse;
import com.codepath.recyclerviewlab.networking.NYTimesApiClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static RecyclerView rvPopularArticles;
    List<PopularArticle> popularArticles;
    PopularArticleRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // calling the action bar
        //ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        //actionBar.setDisplayHomeAsUpEnabled(true);


        rvPopularArticles = findViewById(R.id.rvPopularArticles);
        popularArticles = new ArrayList<>();
        adapter = new PopularArticleRecyclerViewAdapter(this, popularArticles);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPopularArticles.setLayoutManager(linearLayoutManager);
        rvPopularArticles.setAdapter(adapter);

        NYTimesApiClient client = new NYTimesApiClient();
        client.getPopularArticlesByQuery(new CallbackResponse<List<PopularArticle>>() {
            @Override
            public void onSuccess(List<PopularArticle> models) {
                popularArticles.addAll(models);
                // notify dataset changed will tell your adapter that it's data has changed and refresh the view layout
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable error) {
                Log.d("MainActivity", "Failure loading articles " + error.getMessage());

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            rvPopularArticles.setVisibility(View.INVISIBLE);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
