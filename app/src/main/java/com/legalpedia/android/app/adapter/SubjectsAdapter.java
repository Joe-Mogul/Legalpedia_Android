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
import com.legalpedia.android.app.PrinciplesViewActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.models.Articles;
import com.legalpedia.android.app.models.Subjects;
import com.legalpedia.android.app.models.SubjectsStat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 2/10/17.
 */

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.SubjectViewHolder> {

    private List<SubjectsStat> subjects;
    private int rowLayout;
    private Context context;




    ProgressBar progress;


    public SubjectsAdapter(Context context) {
        this.subjects = new ArrayList<SubjectsStat>();

        this.context = context;
    }



    public void setSubjects(List<SubjectsStat> subjects){
        this.subjects = subjects;
    }


    public void addSubjectStats(List<SubjectsStat> subjects){
        for(SubjectsStat s: subjects) {
            if(!this.subjects.contains(s)) {
                this.subjects.add(s);
            }
        }
    }

    public void setSubjectStats(List<SubjectsStat> subjects){
        this.subjects = subjects;

    }


    /**
    public void addSubjects(List<Subjects> subjects){
        for(Subjects s: subjects) {
            if(!this.subjects.contains(s)) {
                this.subjects.add(s);
            }
        }
    }
    */


    @Override
    public SubjectsAdapter.SubjectViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_article_basic, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_subjects_item, parent, false);
        return new SubjectsAdapter.SubjectViewHolder(view);
    }


    @Override
    public void onBindViewHolder(SubjectsAdapter.SubjectViewHolder holder, final int position) {
        String principlesText = "Principles: "+subjects.get(position).getCount();
        principlesText = "";
        holder.title.setText(subjects.get(position).getTitle());
        holder.principles.setText(principlesText);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id=subjects.get(position).getId();
                Intent intent = new Intent(context, PrinciplesViewActivity.class);
                intent.putExtra("subjectid", String.valueOf(id));
                intent.putExtra("title", subjects.get(position).getTitle());
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    class SubjectViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView principles;

        public SubjectViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            principles = (TextView) v.findViewById(R.id.principles);

        }
    }
}

