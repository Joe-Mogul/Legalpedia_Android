package com.legalpedia.android.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.legalpedia.android.app.DocumentViewActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.models.Articles;
import com.legalpedia.android.app.models.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<News> newslist;
    private int rowLayout;
    private Context context;



    ProgressBar progress;
    class NewsViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        TextView year;
        public NewsViewHolder(View v) {
            super(v);
            //moviesLayout = (LinearLayout) v.findViewById(R.id.m);
            title = (TextView) v.findViewById(R.id.title);
            year = (TextView) v.findViewById(R.id.year);

        }
    }

    public NewsAdapter(Context context) {
        this.newslist = new ArrayList<News>();

        this.context = context;
    }


    public void setNews(List<News> newslist){
        for(News a: newslist) {
            this.newslist.add(a);
        }
    }
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_case_item, parent, false);
        return new NewsAdapter.NewsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(NewsAdapter.NewsViewHolder holder, final int position) {
        holder.title.setText(newslist.get(position).getTitle());
        holder.year.setText("2016");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id=newslist.get(position).getId();
                Intent intent = new Intent(context, DocumentViewActivity.class);

                intent.putExtra("_id", id);

                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return newslist.size();
    }
}

