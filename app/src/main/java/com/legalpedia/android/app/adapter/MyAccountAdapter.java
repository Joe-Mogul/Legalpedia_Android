package com.legalpedia.android.app.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.legalpedia.android.app.App;
import com.legalpedia.android.app.ChangePasswordActivity;
import com.legalpedia.android.app.LoginActivity;
import com.legalpedia.android.app.MainActivity;
import com.legalpedia.android.app.MyAccountActivity;
import com.legalpedia.android.app.ProfileViewActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.models.ArticlesDao;
import com.legalpedia.android.app.models.CategoryDao;
import com.legalpedia.android.app.models.CourtsDao;
import com.legalpedia.android.app.models.DaoMaster;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.DictionaryDao;
import com.legalpedia.android.app.models.JudgementsDao;
import com.legalpedia.android.app.models.LawsDao;
import com.legalpedia.android.app.models.MaximsDao;
import com.legalpedia.android.app.models.MenuItem;
import com.legalpedia.android.app.models.PrecedenceDao;
import com.legalpedia.android.app.models.PrinciplesDao;
import com.legalpedia.android.app.models.PublicationsDao;
import com.legalpedia.android.app.models.RatioDao;
import com.legalpedia.android.app.models.RulesDao;
import com.legalpedia.android.app.models.SectionsDao;
import com.legalpedia.android.app.models.SubcategoryDao;
import com.legalpedia.android.app.models.SubjectsDao;
import com.legalpedia.android.app.models.SummaryDao;
import com.legalpedia.android.app.models.UpdateRequestDao;
import com.legalpedia.android.app.models.UserDao;
import com.legalpedia.android.app.util.LGPClient;
import com.legalpedia.android.app.util.Utils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 9/27/17.
 */

public class MyAccountAdapter extends RecyclerView.Adapter<MyAccountAdapter.MyAccountViewHolder> {
    private String LOGINPREFERENCES="login_data";
    private SharedPreferences sharedpreferences = null;
    private SharedPreferences.Editor editor = null;
    private List<MenuItem> menuItems;
    private int rowLayout;
    private Context context;

    class MyAccountViewHolder extends RecyclerView.ViewHolder {


        private TextView title;
        private ImageView icon;

        public MyAccountViewHolder(View v) {
            super(v);
             title = (TextView) v.findViewById(R.id.title);
            icon = (ImageView) v.findViewById(R.id.menu);
        }
    }

    public MyAccountAdapter(Context context) {
        this.menuItems = new ArrayList<MenuItem>();

        this.context = context;
        sharedpreferences=context.getSharedPreferences(LOGINPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }


    public void setMenu(List<MenuItem> menulist){
        this.menuItems = menulist;

    }
    @Override
    public MyAccountAdapter.MyAccountViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_menu_item, parent, false);
        return new MyAccountAdapter.MyAccountViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyAccountAdapter.MyAccountViewHolder holder, final int position) {


        holder.title.setText(menuItems.get(position).getName());
        Log.d("MenuItem",String.valueOf(menuItems.get(position).getResourceid()));
        try {
            Drawable myDrawable=null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                myDrawable = context.getDrawable(menuItems.get(position).getResourceid());
            } else {
               myDrawable = context.getResources().getDrawable(menuItems.get(position).getResourceid());
            }


            holder.icon.setImageDrawable(myDrawable);
        }catch(Exception ex){
            ex.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=menuItems.get(position).getId();
                Intent intent;
                switch(id){
                    case 0:
                     intent = new Intent(context, ProfileViewActivity.class);
                     context.startActivity(intent);
                     break;
                    case 1:
                     intent = new Intent(context, ChangePasswordActivity.class);
                     context.startActivity(intent);
                     break;
                    case 2:
                    showReset();
                     break;
                    case 3:
                    doLogout();
                    break;

                }


            }
        });
    }

    public void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.app_name);
        builder.setMessage(message);
        final int checkedvalue=0;
        //list of items


        String positiveText = context.getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic


                    }


                });



        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showReset(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Reset Account");
        builder.setMessage("You are about to reset your account on this device.You will be able to login and use another device to access Legalpedia Plus or reload the Legalpedia database on this device.Only the Legalpedia database on this device would be reset you account and subscriptions will remain intact.Do you wish to continue?");
        builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doReset();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    @Override
    public int getItemCount() {
        return menuItems.size();
    }
    public void doReset(){
        new ResetAccount().execute();
    }
    public void doLogout(){
        Log.d("Logout","Login out");
        SharedPreferences preferences = context.getSharedPreferences(LOGINPREFERENCES, 0);
        preferences.edit().remove("username").commit();
        preferences.edit().remove("password").commit();

        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
    class ResetAccount extends AsyncTask<String,Void,JSONObject> {
        private JSONObject jobj;
        private ProgressDialog progressDialog = new ProgressDialog(context,R.style.customDialog);
        @Override
        protected JSONObject doInBackground(String... params) {
            jobj = LGPClient.ResetAccount();

            return jobj;
        }
        @Override
        protected void onPostExecute(JSONObject jobj){
            String message="Unable to reset account at the moment.Please try again later";
            try{
                if(jobj.getString("status").equals("ok")){
                    try {

                        File sharedPreferenceFile = new File("/data/data/"+ context.getPackageName()+ "/shared_prefs/");
                        File[] listFiles = sharedPreferenceFile.listFiles();
                        for (File file : listFiles) {
                            file.delete();
                        }
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                    try {
                        new DeleteDatabase().execute();

                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }

                }else{
                    message=jobj.getString("message");
                    try{
                        showMessage(message);
                    }
                    catch(Exception ex){
                        showMessage(message);
                    }
                }
            }
            catch(Exception ex){
                showMessage(message);
            }


            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Legalpedia");
            progressDialog.setMessage("Processing...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

    }


    class DeleteDatabase extends AsyncTask<String,Void,Boolean> {
        private boolean status;
        private DaoSession daoSession;
        private DaoMaster daoMaster;
        private ArticlesDao articlesDao;
        private PublicationsDao publicationsDao;
        private JudgementsDao judgementsDao;
        private SummaryDao summaryDao;
        private CategoryDao categoryDao;
        private SubcategoryDao subcategoryDao;
        private DictionaryDao dictionaryDao;
        private MaximsDao maximsDao;
        private RulesDao rulesDao;
        private SubjectsDao subjectsDao;
        private PrinciplesDao principlesDao;
        private PrecedenceDao precedenceDao;
        private LawsDao lawsDao;
        private CourtsDao courtsDao;
        private RatioDao ratioDao;
        private UserDao userdao;
        private SectionsDao sectionsDao;
        private UpdateRequestDao updateRequestDao;
        MyAccountActivity activity =(MyAccountActivity)context;
        public DeleteDatabase(){
            try{

                daoSession = ((App)  activity.getApplication()).getDaoSession();
                daoMaster = ((App) activity.getApplication()).getDaoMaster();
                articlesDao=daoSession.getArticlesDao();
                publicationsDao=daoSession.getPublicationsDao();
                judgementsDao=daoSession.getJudgementsDao();
                summaryDao=daoSession.getSummaryDao();
                categoryDao = daoSession.getCategoryDao();
                subcategoryDao = daoSession.getSubcategoryDao();
                dictionaryDao = daoSession.getDictionaryDao();
                maximsDao = daoSession.getMaximsDao();
                rulesDao = daoSession.getRulesDao();
                subjectsDao = daoSession.getSubjectsDao();
                principlesDao = daoSession.getPrinciplesDao();
                precedenceDao = daoSession.getPrecedenceDao();
                lawsDao = daoSession.getLawsDao();
                courtsDao = daoSession.getCourtsDao();
                ratioDao = daoSession.getRatioDao();
                sectionsDao = daoSession.getSectionsDao();
                updateRequestDao = daoSession.getUpdateRequestDao();
                userdao = daoSession.getUserDao();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }

        }


        @Override
        protected Boolean doInBackground(String... params) {




            Log.d("Deleting","Article");
            articlesDao.deleteAll();
            Log.d("Deleting","articlesDao");
            publicationsDao.deleteAll();
            Log.d("Deleting","publicationsDao");
            judgementsDao.deleteAll();
            Log.d("Deleting","judgementsDao");
            summaryDao.deleteAll();
            Log.d("Deleting","summaryDao");
            categoryDao.deleteAll();
            Log.d("Deleting","categoryDao");
            subcategoryDao.deleteAll();
            Log.d("Deleting","subcategoryDao");
            dictionaryDao.deleteAll();
            Log.d("Deleting","dictionaryDao");
            maximsDao.deleteAll();
            Log.d("Deleting","maximsDao");
            rulesDao.deleteAll();
            subjectsDao.deleteAll();
            principlesDao.deleteAll();
            precedenceDao.deleteAll();
            lawsDao.deleteAll();
            courtsDao.deleteAll();
            ratioDao.deleteAll();
            sectionsDao.deleteAll();
            updateRequestDao.deleteAll();
            userdao.deleteAll();
            Log.d("Deleting","userdao");

            daoMaster.createAllTables(daoSession.getDatabase(), true);

            return status;
        }
        @Override
        protected void onPostExecute(Boolean status) {
            try {
                try {
                    activity.getApplicationContext().getSharedPreferences(LOGINPREFERENCES, 0).edit().clear().commit();
                    editor.clear();
                    editor.commit();
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.commit();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
                Utils.deleteDir(new File(Utils.getROOTDIR()));
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }

    }


}

