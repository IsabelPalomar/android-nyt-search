package io.androidblog.nytimessearch.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.androidblog.nytimessearch.R;
import io.androidblog.nytimessearch.models.Article;

public class ArticleActivity extends AppCompatActivity {

    @BindView(R.id.wvArticle)
    WebView wvArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();
        final Article article = b.getParcelable("article");

        wvArticle.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        wvArticle.loadUrl(article.getWebUrl());
    }
}
