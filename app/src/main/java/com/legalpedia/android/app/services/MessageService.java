package com.legalpedia.android.app.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.widget.Toast;

import com.legalpedia.android.app.App;
import com.legalpedia.android.app.ArticlesActivity;
import com.legalpedia.android.app.LoginActivity;
import com.legalpedia.android.app.MainActivity;
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
import com.legalpedia.android.app.util.Utils;
import com.legalpedia.android.app.websocket.WebSocketClient;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import br.com.goncalves.pugnotification.interfaces.ImageLoader;
import br.com.goncalves.pugnotification.interfaces.OnImageLoadingCompleted;
import br.com.goncalves.pugnotification.notification.Load;
import br.com.goncalves.pugnotification.notification.PugNotification;
/**
 * Created by adebayoolabode on 10/31/16.
 */

public class MessageService extends Service implements WebSocketClient.Listener{
    private boolean ISDISCONNECTED=true;
    private String TAG="MessageService";
    private WebSocketClient client;
    private String message="";
    private final IBinder messageBind = new MessageBinder();
    private String LOGINPREFERENCES="login_data";
    private SharedPreferences sharedpreferences = null;
    private int wsinterval=30000;
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
    private String sessionkey;
    private String uid;
    private NotificationCompat.Builder notification;
    private static final int xid = 12345;
    private Intent notificationintent;
    @Override
    public void onCreate() {
        super.onCreate();
        sharedpreferences=getSharedPreferences(LOGINPREFERENCES, Context.MODE_PRIVATE);
        createClient();
        //client.disconnect();
        wsinterval = sharedpreferences.getInt("wsinterval",wsinterval);
        try{
            daoSession = ((App) getApplication()).getDaoSession();
            daoMaster = ((App) getApplication()).getDaoMaster();
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

        try{
            notificationintent = new Intent();
            notification = new  NotificationCompat.Builder(this);
            notification.setAutoCancel(true);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }

    public void createClient(){
        try {
            List<BasicNameValuePair> extraHeaders = new ArrayList<BasicNameValuePair>();
            String domain = getString(R.string.domain);
            //domain="192.168.1.3:9999";
            sessionkey  = sharedpreferences.getString("sessionid","");
            uid  = sharedpreferences.getString("uid","");
            System.out.println("ws://" + domain + "/ws/"+sessionkey);
            client = new WebSocketClient(URI.create("ws://" + domain + "/ws/"+sessionkey), this, extraHeaders);
            client.connect();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent myIntent = intent;
        if (myIntent !=null && intent.getExtras()!=null){
            String username = myIntent.getExtras().getString("username");
            String password = myIntent.getExtras().getString("password");
            Log.d("MessageService",username);

        }

        if(client==null){
            createClient();
        }


        return START_STICKY;
    }

    public WebSocketClient getClient(){
        return client;
    }


    public void showMessage(String title,String message,String receiver){
        //notification.setSmallIcon(R.drawable.logo);
        notification.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notificationlogo));
        //notification.setTicker("Inbox");
        //notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle(title);
        notification.setContentText(message);
        if(receiver.equals("article")) {
             notificationintent = new Intent(this, ArticlesActivity.class);
        }else{
            notificationintent = new Intent(this, MainActivity.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this , 0 , notificationintent , PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        //notification.setContentIntent(pendingIntent);

        //NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //nm.notify(xid, notification.build());
        Load mLoad = PugNotification.with(getApplicationContext()).load()
                .smallIcon(R.drawable.logo)
                .autoCancel(true)
                .largeIcon(R.drawable.logo)
                .title(title)
                .click(pendingIntent)
                .message(message)
                .flags(Notification.DEFAULT_ALL);
    }
    public boolean sendMessage(JSONObject jobj){
        boolean issent=false;
        try {
            if (client == null) {
                createClient();
            }
            client.send(jobj.toString());
            issent = true;
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return issent;
    }


    public void setMessage(String msg){
        message=msg;
    }

    public String getMessage(){
        return message;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messageBind;
    }


    @Override
    public boolean onUnbind(Intent intent){

        if(client!=null) {
            client=null;
        }
        if(client==null){
            createClient();
        }
        try {
            client.connect();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
    public class MessageBinder extends Binder {
        public MessageService getService() {
            return MessageService.this;
        }
    }
    @Override
    public void onConnect() {
        Log.d(TAG, "Connected!");
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("message", "Hello Server");
            jobj.put("session",sessionkey);
            jobj.put("uid",uid);
            showMessage("Legalpedia",jobj.getString("message"),"article");
            client.send(jobj.toString());
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void onMessage(String message) {
        try {
            JSONObject object = new JSONObject(message);
            String event = object.getString("action");

                switch (event) {
                    case "wipe":
                        doWipe();
                        break;
                    case "logout":
                        doLogout();
                        break;
                    case "disabledevice":
                        disableDevice();
                        break;
                    case "enabledevice":
                        enableDevice();
                    case "push":
                        JSONObject payload = object.getJSONObject("payload");
                        pushMessage(payload);
                    default:
                        break;
                }

            Log.d(TAG, String.format("Got string message! %s", message));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        setMessage(message);
    }

    @Override
    public void onMessage(byte[] data) {
        String event=new String(data);
        Log.d(TAG, String.format("Got binary message! %s", new String(data)));
    }

    /**@Override
    public void onDisconnect(int code, String reason) {
        Log.d(TAG, String.format("Disconnected! Code: %d Reason: %s", code, reason));
        try{

            final Handler handler = new Handler(Looper.getMainLooper());
            final Runnable r = new Runnable() {
                public void run() {
                    if(client==null) {
                        createClient();
                    }
                    client.connect();
                }
            };
            handler.postDelayed(r, 30000);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }*/
    @Override
    public void onDisconnect(int code, String reason) {
        Log.d(TAG, String.format("Disconnected! Code: %d Reason: %s", code, reason));
        try{
            ISDISCONNECTED= true;
            stopSelf();
            //doReconnect();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onError(Exception error) {
        Log.e(TAG, "Error!", error);
        try{

            final Handler handler = new Handler(Looper.getMainLooper());
            final Runnable r = new Runnable() {
                public void run() {
                    createClient();
                    client.connect();
                }
            };
            handler.postDelayed(r, wsinterval);
        }
        catch(Exception ex){
            ex.printStackTrace();
            wsinterval = 1800000;
        }
    }

    //push message to device
    public void pushMessage(JSONObject payload) {
        try {
            String title=payload.getString("title");
            String message=payload.getString("message");
            Load mLoad = PugNotification.with(getApplicationContext()).load()
                    .smallIcon(R.drawable.logo)
                    .autoCancel(true)
                    .largeIcon(R.drawable.logo)
                    .title(title)
                    .message(message)
                    .flags(Notification.DEFAULT_ALL);
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }
    //wipe the device database
    public void doWipe(){
        try{
            sharedpreferences.edit().clear().commit();
            try {
                new DeleteDatabase().execute();

            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            //showMessage("Admin has sent a remote wipe event.Your device will logout temporarily to apply the updates.");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {

                    Toast.makeText(getApplicationContext(),"Admin has sent a remote wipe event.Your device will logout temporarily to apply the updates.",Toast.LENGTH_LONG).show();





                }
            });




        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void disableDevice(){
        sharedpreferences.edit().putBoolean("disabled",true);
        Intent intent = new Intent(MessageService.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void enableDevice(){
        sharedpreferences.edit().putBoolean("disabled",false);
        Intent intent = new Intent(MessageService.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //force the device to logout
    public void doLogout(){

        Log.d("Logout","Login out");
        try {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
             public void run() {

                 Toast.makeText(getApplicationContext(),"We are pushing a remote update.Your device will logout temporarily to apply the updates.",Toast.LENGTH_LONG).show();




             }
              });
            //showMessage("We are pushing a remote update.Your device will logout temporarily to apply the updates.");
            sharedpreferences.edit().remove("username").apply();
            sharedpreferences.edit().remove("password").apply();
            sharedpreferences.edit().remove("uid").apply();
            sharedpreferences.edit().remove("username").apply();
            sharedpreferences.edit().remove("password").apply();
            sharedpreferences.edit().remove("packageid").apply();
            sharedpreferences.edit().remove("packagename").apply();
            sharedpreferences.edit().remove("expirydate").apply();
            sharedpreferences.edit().remove("lastrenewdate").apply();
            sharedpreferences.edit().remove("sessionid").apply();
            sharedpreferences.edit().remove("lastrenewdate").apply();
            sharedpreferences.edit().remove("updateinterval").apply();
            sharedpreferences.edit().remove("wsinterval").apply();
            sharedpreferences.edit().putBoolean("disabled",false);
            Intent intent = new Intent(MessageService.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        }catch(Exception ex){
            ex.printStackTrace();
        }

    }




    class DeleteDatabase extends AsyncTask<String,Void,Boolean> {
        private boolean status;




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
                getApplicationContext().getSharedPreferences(LOGINPREFERENCES, 0).edit().clear().commit();
                Utils.deleteDir(new File(Utils.getROOTDIR()));
                Intent intent = new Intent(MessageService.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }

    }

}
