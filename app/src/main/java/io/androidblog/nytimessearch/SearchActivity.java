package io.androidblog.nytimessearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import io.androidblog.nytimessearch.adapters.ArticleRecyclerViewAdapter;
import io.androidblog.nytimessearch.models.Article;
import io.androidblog.nytimessearch.utils.Constants;


public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.etQuery)
    TextView etQuery;
    @BindView(R.id.btnSearch)
    Button btnSearch;
    @BindView(R.id.rvNews)
    RecyclerView rvNews;

    ArrayList<Article> articles;
    ArticleRecyclerViewAdapter articleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        articles = new ArrayList<>();

        articleAdapter = new ArticleRecyclerViewAdapter(this, articles);
        rvNews.setAdapter(articleAdapter);
        rvNews.setLayoutManager(new LinearLayoutManager(this));

        onArticleSearch();

    }

    @OnClick(R.id.btnSearch) void onArticleSearch() {
        String query = etQuery.getText().toString();

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("api-key", "5448fa3d0ac24f27aec17d4afeb2ee42");
        params.put("page", 0);
        params.put("q", query);

        client.get(Constants.BASE_URL, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
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

        Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
    }
}
