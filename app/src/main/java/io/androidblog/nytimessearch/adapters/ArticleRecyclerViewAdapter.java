package io.androidblog.nytimessearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.androidblog.nytimessearch.R;
import io.androidblog.nytimessearch.models.Article;

public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<ArticleRecyclerViewAdapter.ViewHolder>{

    private Context mContext;
    private List<Article> mArticles;

    public ArticleRecyclerViewAdapter(Context context, ArrayList<Article> articles) {
        mArticles = articles;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ArticleRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.article_row, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = mArticles.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.tvHeadline;
        ImageView ivThumbnail = holder.ivThumbnail;
        textView.setText(article.getHeadline());

        if(!article.getThumbNail().equals("")){
            Picasso.with(getContext())
                    .load(article.getThumbNail())
                    .into(ivThumbnail);
        }



    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvHeadline;
        public ImageView ivThumbnail;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvHeadline = (TextView) itemView.findViewById(R.id.tvHeadline);
            ivThumbnail = (ImageView) itemView.findViewById(R.id.ivThumbnail);
        }
    }
}
