package com.legalpedia.android.app.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.legalpedia.android.app.App;
import com.legalpedia.android.app.NoteDetailActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Notes;
import com.legalpedia.android.app.models.NotesDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 4/9/17.
 */

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NotesViewHolder>{
    private DaoSession daoSession;
    private NotesDao notesDao;
    private Context context;
    List<Notes> notes=new ArrayList<Notes>();
    boolean undoOn; // is undo on, you can turn it on from the toolbar menu
    public NoteListAdapter(Context context) {
        this.notes = new ArrayList<Notes>();
        Activity activity =(Activity)context;
        daoSession = ((App)  activity.getApplication()).getDaoSession();
        notesDao = daoSession.getNotesDao();
        this.context = context;
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list, parent, false);
        return new NoteListAdapter.NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
        final String noteid=String.valueOf(notes.get(position).getId());

        holder.title.setText(notes.get(position).getTitle());
        String formatteddate=new SimpleDateFormat("dd MMMM yyyy").format(notes.get(position).getEditdate());
        holder.date.setText(formatteddate);
        final Notes note = notes.get(position);
        holder.delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //notesDao.delete(note);
            }

        });
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d("NotesViewHolder","Note Clicked "+note.getId());
                Intent intent = new Intent(context, NoteDetailActivity.class);
                intent.putExtra("noteid",String.valueOf(note.getId()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void pendingRemoval(int position) {
        Notes item = notes.get(position);
        if (!notes.contains(item)) {
            notes.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            /** Runnable pendingRemovalRunnable = new Runnable() {
            @Override
            public void run() {
            remove(researchRequests.indexOf(item));
            }
            };
             handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
             pendingRunnables.put(item, pendingRemovalRunnable);
             */
        }
    }

    public void remove(int position) {
        Notes item = notes.get(position);
        Log.d("Remove","Removing "+item.getTitle());
        notesDao.delete(item);
        notes.remove(position);
        //recycler.removeViewAt(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, notes.size());


    }

    public boolean isPendingRemoval(int position) {
        Notes item = notes.get(position);
        return notes.contains(item);
    }


    public void addNotes(List<Notes> l){
        this.notes=l;
    }


    class NotesViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        TextView date;
        ImageView delete;
        public NotesViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            date = (TextView) v.findViewById(R.id.date);
            delete = (ImageView) v.findViewById(R.id.delete);

        }
    }

}
