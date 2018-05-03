package com.legalpedia.android.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legalpedia.android.app.DictionaryActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.models.Dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.DictionaryViewHolder> {

    private List<Dictionary> dictionary;
    private int rowLayout;
    private Context context;

    LinearLayout moviesLayout;

    class DictionaryViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;

        public DictionaryViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);


        }
    }

    public DictionaryAdapter(Context context) {
        this.dictionary = new ArrayList<Dictionary>();

        this.context = context;
    }


    public void setDictionary(List<Dictionary> dictionary){
        for(Dictionary d: dictionary) {
            this.dictionary.add(d);
        }
    }
    @Override
    public DictionaryAdapter.DictionaryViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_dictionary_item, parent, false);
        return new DictionaryAdapter.DictionaryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(DictionaryAdapter.DictionaryViewHolder holder, final int position) {

        holder.title.setText(dictionary.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id=dictionary.get(position).getId();
                Intent intent = new Intent(context, DictionaryActivity.class);

                intent.putExtra("dictionaryid", String.valueOf(id));

                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return dictionary.size();
    }
}


