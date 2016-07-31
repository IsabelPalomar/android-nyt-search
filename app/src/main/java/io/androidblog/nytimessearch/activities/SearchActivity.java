package io.androidblog.nytimessearch.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import io.androidblog.nytimessearch.R;
import io.androidblog.nytimessearch.adapters.ArticleRecyclerViewAdapter;
import io.androidblog.nytimessearch.dialogs.FiltersDialogFragment;
import io.androidblog.nytimessearch.models.Article;
import io.androidblog.nytimessearch.utils.Constants;


public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.rvNews)
    RecyclerView rvNews;

    ArrayList<Article> articles;
    ArticleRecyclerViewAdapter articleAdapter;
    StaggeredGridLayoutManager sglmNews;
    private String DEFAULT_QUERY = "new york";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        articles = new ArrayList<>();

        sglmNews = new StaggeredGridLayoutManager(2, 1);
        rvNews.setLayoutManager(sglmNews);
        rvNews.setHasFixedSize(true);

        articleAdapter = new ArticleRecyclerViewAdapter(this, articles);
        rvNews.setAdapter(articleAdapter);

        fetchArticles(DEFAULT_QUERY);


    }

    private void fetchArticles(String query) {

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("api-key", "5448fa3d0ac24f27aec17d4afeb2ee42");
        params.put("page", 0);
        params.put("q", query);

        articleAdapter.clear();

        client.get(Constants.BASE_URL, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    articleAdapter.addAll(articles);

                    articleAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchArticles(query);
                searchView.clearFocus();
                SearchActivity.this.setTitle(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        MenuItem filterItem = menu.findItem(R.id.iActionFilter);
        filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FragmentManager fm = getSupportFragmentManager();
                FiltersDialogFragment editNameDialogFragment = FiltersDialogFragment.newInstance("Some Title");
                editNameDialogFragment.show(fm, "fragment_edit_name");
                return true;
            }
        });


        return super.onCreateOptionsMenu(menu);

    }
}
