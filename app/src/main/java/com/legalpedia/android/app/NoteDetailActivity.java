package com.legalpedia.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Notes;
import com.legalpedia.android.app.models.NotesDao;

import java.util.List;

/**
 * Created by adebayoolabode on 2/4/17.
 */

public class NoteDetailActivity extends AppCompatActivity {
    private DaoSession daoSession;
    private NotesDao notesDao;
    private Toolbar toolbar;
    private TextView bodyTxt;
    public int noteid=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notedetails_activity);
        daoSession = ((App)  getApplication()).getDaoSession();
        notesDao = daoSession.getNotesDao();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        daoSession = ((App)  getApplication()).getDaoSession();
        notesDao = daoSession.getNotesDao();

        bodyTxt = (TextView)findViewById(R.id.body);
        Intent intent= getIntent();
        if(intent!=null) {
            String noteval = intent.getStringExtra("noteid");
            noteid = Integer.parseInt(noteval);
            Log.d("NoteDetail"," values "+  " Note ID "+String.valueOf(noteid));
        }
        List<Notes> notes =notesDao.queryBuilder().where(NotesDao.Properties.Id.eq(noteid)).list();
        if(notes.size()>0) {
            Notes note = notes.get(0);
            setTitle(note.getTitle());
            bodyTxt.setText(note.getContent());
        }

    }


    @Override
    public void onPause(){
        super.onPause();
        onBackPressed();

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notedetail, menu);

        MenuItem cartItem  = menu.findItem(R.id.edit);
        //View cartView = (View) MenuItemCompat.getActionView(cartItem);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();

        }

        if (id == R.id.edit) {

            Intent intent=new Intent(NoteDetailActivity.this,AddNoteActivity.class);
            intent.putExtra("noteid",noteid);
            intent.putExtra("isedit",true);
            startActivity(intent);
        }



        return super.onOptionsItemSelected(item);
    }


}
