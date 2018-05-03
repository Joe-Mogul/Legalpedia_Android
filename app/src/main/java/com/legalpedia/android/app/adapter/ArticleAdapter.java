package com.legalpedia.android.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.legalpedia.android.app.DocumentViewActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.models.Articles;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Articles> articles;
    private int rowLayout;
    private Context context;



    ProgressBar progress;
    class ArticleViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        TextView year;
        public ArticleViewHolder(View v) {
            super(v);
            //moviesLayout = (LinearLayout) v.findViewById(R.id.m);
            title = (TextView) v.findViewById(R.id.title);
            year = (TextView) v.findViewById(R.id.year);

        }
    }

    public ArticleAdapter(Context context) {
        this.articles = new ArrayList<Articles>();

        this.context = context;
    }


    public void setArticles(List<Articles> articles){
        for(Articles a: articles) {
            this.articles.add(a);
        }
    }
    @Override
    public ArticleAdapter.ArticleViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_case_item, parent, false);
        return new ArticleAdapter.ArticleViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ArticleAdapter.ArticleViewHolder holder, final int position) {
        holder.title.setText(articles.get(position).getTitle());
        holder.year.setText("2016");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id=articles.get(position).getId();
                Intent intent = new Intent(context, DocumentViewActivity.class);

                intent.putExtra("_id", id);

                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}

