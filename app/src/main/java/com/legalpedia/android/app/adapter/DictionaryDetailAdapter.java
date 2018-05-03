package com.legalpedia.android.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legalpedia.android.app.R;
import com.legalpedia.android.app.models.Dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */

public class DictionaryDetailAdapter extends RecyclerView.Adapter<DictionaryDetailAdapter.DictionaryDetailViewHolder> {

    private List<Dictionary> dictionary;
    private int rowLayout;
    private Context context;

    LinearLayout moviesLayout;

    class DictionaryDetailViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView content;

        public DictionaryDetailViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            content = (TextView) v.findViewById(R.id.content);


        }
    }

    public DictionaryDetailAdapter(Context context) {
        this.dictionary = new ArrayList<Dictionary>();

        this.context = context;
    }


    public void setDictionary(List<Dictionary> dictionary){
        for(Dictionary d: dictionary) {
            this.dictionary.add(d);
        }
    }
    @Override
    public DictionaryDetailAdapter.DictionaryDetailViewHolder onCreateViewHolder(ViewGroup parent,
                                                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_dictionarydetail_item, parent, false);
        return new DictionaryDetailAdapter.DictionaryDetailViewHolder(view);
    }


    @Override
    public void onBindViewHolder(DictionaryDetailAdapter.DictionaryDetailViewHolder holder, final int position) {
        holder.title.setText(dictionary.get(position).getTitle());
        holder.content.setText(Html.fromHtml(dictionary.get(position).getContent()));
    }

    @Override
    public int getItemCount() {
        return dictionary.size();
    }
}


