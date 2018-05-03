package com.legalpedia.android.app.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.legalpedia.android.app.App;
import com.legalpedia.android.app.LoginActivity;
import com.legalpedia.android.app.models.Category;
import com.legalpedia.android.app.models.CategoryDao;
import com.legalpedia.android.app.models.Courts;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Articles;
import com.legalpedia.android.app.models.ArticlesDao;
import com.legalpedia.android.app.models.CategoryDao;
import com.legalpedia.android.app.models.CourtsDao;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Dictionary;
import com.legalpedia.android.app.models.DictionaryDao;
import com.legalpedia.android.app.models.Judgements;
import com.legalpedia.android.app.models.JudgementsDao;
import com.legalpedia.android.app.models.Laws;
import com.legalpedia.android.app.models.LawsDao;
import com.legalpedia.android.app.models.Maxims;
import com.legalpedia.android.app.models.MaximsDao;
import com.legalpedia.android.app.models.Precedence;
import com.legalpedia.android.app.models.PrecedenceDao;
import com.legalpedia.android.app.models.Principles;
import com.legalpedia.android.app.models.PrinciplesDao;
import com.legalpedia.android.app.models.Publications;
import com.legalpedia.android.app.models.PublicationsDao;
import com.legalpedia.android.app.models.Ratio;
import com.legalpedia.android.app.models.RatioDao;
import com.legalpedia.android.app.models.Rules;
import com.legalpedia.android.app.models.RulesDao;
import com.legalpedia.android.app.models.Sections;
import com.legalpedia.android.app.models.SectionsDao;
import com.legalpedia.android.app.models.Subcategory;
import com.legalpedia.android.app.models.SubcategoryDao;
import com.legalpedia.android.app.models.Subjects;
import com.legalpedia.android.app.models.SubjectsDao;
import com.legalpedia.android.app.models.Summary;
import com.legalpedia.android.app.models.SummaryDao;
import com.legalpedia.android.app.models.UpdateRequest;
import com.legalpedia.android.app.models.UpdateRequestDao;
import com.legalpedia.android.app.models.User;
import com.legalpedia.android.app.models.UserDao;
import com.legalpedia.android.app.util.DatabaseUtil;
import com.legalpedia.android.app.util.LGPClient;
import com.legalpedia.android.app.util.Utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipFile;

/**
 * Created by adebayoolabode on 5/12/17.
 */

public class UpdateService extends Service {
    private final IBinder updateServiceBind = new UpdateServiceBinder();
    private JSONObject jsonobj;
    private DaoSession daoSession;
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
    private Handler mHandler = new Handler();
    private String username;
    private String password;
    public static boolean isrunning=false;
    private int progress=0;
    private String progresstext;
    private static int filesdownloaded=0;
    private static int totalfilestodownload=0;
    private static int totalfilesprocesseddownload=0;
    private List<Articles> articlesList = new ArrayList<Articles>();
    private List<Judgements> judgementsList = new ArrayList<Judgements>();
    private List<Publications> publicationsList = new ArrayList<Publications>();
    private List<Summary> summaryList = new ArrayList<Summary>();
    private List<Category> categoryList = new ArrayList<Category>();
    private List<Subcategory> subcategoryList = new ArrayList<Subcategory>();
    private List<Dictionary> dictionaryList = new ArrayList<Dictionary>();
    private List<Maxims> maximsList = new ArrayList<Maxims>();
    private List<Rules> rulesList = new ArrayList<Rules>();
    private List<Subjects> subjectsList = new ArrayList<Subjects>();
    private List<Principles> principlesList = new ArrayList<Principles>();
    private List<Precedence> precedenceList = new ArrayList<Precedence>();
    private List<Laws> lawsList = new ArrayList<Laws>();
    private List<Courts> courtsList = new ArrayList<Courts>();
    private List<Ratio> ratioList = new ArrayList<Ratio>();
    private List<Sections> sectionsList = new ArrayList<Sections>();
    private List<UpdateRequest> updateRequests = new ArrayList<UpdateRequest>();
    private String LOGINPREFERENCES="login_data";
    private SharedPreferences sharedpreferences = null;
    private long updatecheckinterval=86400000L;
    //private long updatecheckinterval=120000L;
    @Override
    public void onCreate(){
        //create the service
        //create the http client here
        super.onCreate();
        daoSession = ((App) getApplication()).getDaoSession();
        sharedpreferences=getSharedPreferences(LOGINPREFERENCES, Context.MODE_PRIVATE);
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
        new ClearDirectory().execute();

    }
    public int onStartCommand(Intent intent, int flags, int startId){
        try {

            if(sharedpreferences.getString("username","")!="" && sharedpreferences.getString("password","")!="") {
                String username = sharedpreferences.getString("username", "");
                String password = sharedpreferences.getString("password","");
                Log.d("UpdateService", "service started");
                new LoginExecute().execute(username, password);
                scheduleNext();
            }else{
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            return super.onStartCommand(intent, flags, startId);
        }
        catch(Exception ex){
            //send user to login page
            ex.printStackTrace();

            intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return Service.START_NOT_STICKY;
        }

    }


    private void updateProgress(String text,int p){
        progresstext=text;
        progress = p;
    }


    public String getProgressText(){
        return progresstext;
    }


    public int getProgress(){
        return progress;
    }


    private void scheduleNext() {
        //System.out.print("Running update again");
        mHandler.postDelayed(new Runnable() {
            public void run() {

                List<User> users = userdao.loadAll();
                if(users.size()>0) {
                    User user = users.get(0);

                    username = user.getUsername();
                    password = user.getPassword();

                    new LoginExecute().execute(username, password);
                    scheduleNext();

                }

            }
        }, updatecheckinterval);
    }

    public void processData(String data){

        //Log.d("App","copying database file now");
        copyDatabaseFromAssets(this.getApplicationContext(),data,true);
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return updateServiceBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        //release all resources here

        return false;
    }

    public class UpdateServiceBinder extends Binder {
        public UpdateService getService() {
            return UpdateService.this;
        }
    }

    class ClearDirectory extends AsyncTask<String,Void,Boolean> {
        private  Boolean status =false;
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Utils.deleteDir(new File(Utils.getROOTDIR()));

            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            return status;
        }
        @Override
        protected void onPostExecute(Boolean status) {

        }


    }



    class LoginExecute extends AsyncTask<String,Void,JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params){


            jsonobj= LGPClient.doLogin(params[0],params[1]);


            return jsonobj;
        }
        //this is fired after the doInBackground completes
        @Override
        protected void onPostExecute(JSONObject jsonobj) {
            try {

                Log.d("UpdateService","Pre updates");
                Log.d("UpdateService",jsonobj.getString("status"));
                if (jsonobj.getString("status").equals("ok")) {
                    isrunning=true;
                    updateProgress("Logged in",3);
                    if(jsonobj.getJSONObject("user")!=null && jsonobj.getString("packageid")!=null ) {
                        String uid = jsonobj.getJSONObject("user").getString("uid");
                        String packageid = jsonobj.getString("packageid");
                        Log.d("UpdateService", "Post updates");

                        new ListUpdates().execute(uid, packageid);
                    }
                    else{
                        isrunning=false;
                        updateProgress("No updates available",100);
                        Toast.makeText(getApplicationContext(),"No updates found now.Will check again later.",Toast.LENGTH_LONG).show();
                    }
                }else{

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try{
                        String message=jsonobj.getString("message");
                        intent.putExtra("message",message);
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }

                    startActivity(intent);
                }

            }
            catch(Exception ex) {

            }
        }
    }



    public String  getUpdates(){
        updateRequests = updateRequestDao.loadAll();
        JSONObject updatesobj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (UpdateRequest ur : updateRequests) {
                jsonArray.put(ur.getFilename());
            }
            updatesobj.put("data", jsonArray.toString());

        }
        catch(Exception ex){
            ex.printStackTrace();
        }


        return updatesobj.toString();
    }

    class ListUpdates extends AsyncTask<String,Void,JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params){

            Log.d("UpdateService","Fetching updates");
            String installedupdates = getUpdates();
            Log.d("Installed Updates",installedupdates);

            jsonobj= LGPClient.getUpdates(params[0],params[1],installedupdates);

            return jsonobj;
        }
        //this is fired after the doInBackground completes
        @Override
        protected void onPostExecute(JSONObject jsonobj) {
            try {


                if (jsonobj.getString("status").equals("ok")) {
                    updateProgress("Checking for updates",5);
                    JSONArray datax=new JSONArray();
                    try {
                        datax = jsonobj.getJSONArray("data");
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                    if(datax.length()<=0){
                        UpdateService.isrunning=false;

                        return;
                    }
                    JSONObject jj=datax.getJSONObject(0);
                    JSONArray urls=jj.getJSONArray("urls");
                    totalfilestodownload=urls.length();
                    for(int i=0;i<=urls.length()-1; i++) {
                        JSONObject urldata= urls.getJSONObject(i);
                        String url=urldata.getString("url");
                        String size=urldata.getString("size");
                        //Log.d("TestDownloadActivity","Fetching url from "+url);
                        String externalmemoryize=Utils.getAvailableExternalMemorySize();
                        String internalmemoryize=Utils.getAvailableInternalMemorySize();
                        //Log.d("TestDownloadActivity","External Memory Size "+externalmemoryize);
                        //Log.d("TestDownloadActivity","Internal Memory Size "+internalmemoryize);
                        updateProgress("Downloading files..........",6);
                        new DownloadData().execute(url,size);

                    }

                    /** for(DownloadData data: mTasks){
                     while(data.getStatus()!= AsyncTask.Status.FINISHED){

                     }
                     }*/
                }

            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }





    class DownloadData extends AsyncTask<String,Void,File> {
        private String url;
        private String localPath ="";
        private File file=null;
        private String fileName;
        private long filesize=0L;
        @Override
        protected File doInBackground(String... params){

            String path = Utils.getROOTDIR();

            try {
                filesize = Long.valueOf(params[1]);
                URL uri = new URL(params[0]);
                fileName = uri.getFile();
                //Log.d("File Name 1", fileName);
                file = new File(path, fileName);
                //Log.d("File Name 2", file.getCanonicalFile().getName());
                //localPath = file.getAbsolutePath();
                localPath=path+"/"+file.getCanonicalFile().getName();
                //file.mkdir();
                //Log.d("File Path", localPath);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            try {
                url = params[0];
                file =LGPClient.downloadData(params[0], localPath);

            }
            catch(Exception ex){
                ex.printStackTrace();

            }


            return file;
        }
        //this is fired after the doInBackground completes
        @Override
        protected void onPostExecute(File result) {
            try {

                if(result!=null) {

                    // we need to check the file size here
                    if(result.length()<filesize){

                        Log.d("DownloadData","File size does not match");
                        Log.d("DownloadData","Current size "+String.valueOf(result.length()));
                        Log.d("DownloadData","Expected size "+String.valueOf(filesize));
                        String size = String.valueOf(filesize);
                        new DownloadData().execute(url,size);
                        return ;
                    }else{

                        filesdownloaded =  filesdownloaded +1;
                        Log.i("FileProgress","Total files downloaded "+String.valueOf(filesdownloaded));
                        updateProgress("Downloaded "+String.valueOf(filesdownloaded)+" of "+String.valueOf(totalfilestodownload)+" file(s)",6);


                        //Log.d("FileName",result.getAbsolutePath());
                        String unzipPath = Utils.getROOTDIR() + "/data";
                        //Log.d("Unzip ",unzipPath);
                        //unzip(result.getAbsolutePath(), unzipPath);
                        String csize = String.valueOf(filesize);
                        UpdateRequest ur = new UpdateRequest();
                        ur.setFilename(fileName.substring(fileName.lastIndexOf("/")+1));
                        ur.setFilesize(csize);
                        ur.setUpdatedate(new SimpleDateFormat("yyyy-mm-dd").format(new Date()));
                        updateRequestDao.insert(ur);
                        String attempts="0";
                        new UnZipTask().execute(result.getAbsolutePath(), unzipPath,url,String.valueOf(filesize),attempts);

                    }


                }else{
                    Log.i("FileProgress","Failed to download file "+url);
                    //System.out.println("Failed to download file "+url);
                    Log.d("FailedDownload",url);
                    String size = String.valueOf(filesize);
                    new DownloadData().execute(url,size);
                    return ;
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
                Log.d("FailedDownload",url);
                String size = String.valueOf(filesize);
                new DownloadData().execute(url,size);
                return ;
            }
        }



    }


    public void redownloadCorruptFile(String filename,long filesize,String url){
        File file = new File(filename);
        updateProgress("Redownloading corrupted file "+file.getName(),6);
        try {
            file.delete();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        String size = String.valueOf(filesize);
        new DownloadData().execute(url, size);
    }


    class UnZipTask extends AsyncTask<String,Void,Boolean> {
        private boolean unzipsuccess=false;
        private String filename = "";
        private String outputfile = "";
        private String url = "";
        private long filesize=0L;
        private int attempts=0;

        @Override
        protected Boolean doInBackground(String... params){
            try {
                filename = params[0];
                outputfile = params[1];
                url = params[2];
                filesize = Long.parseLong(params[3]);
                int paramattempt=Integer.parseInt(params[4]);
                if(paramattempt<=0){
                    attempts=1;
                }else{
                    attempts=paramattempt+1;
                }
                Log.d("Unzipping",filename);
                unzipsuccess = unzip(filename, outputfile);
            }
            catch(Exception ex) {
                ex.printStackTrace();
                unzipsuccess = false;
            }

            return unzipsuccess;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            try {
                if(result){
                    Log.d("Unzip","Successfully unzipped "+filename+" to "+outputfile);

                }else{
                    //if(attempts<=3) {
                    Log.d("Unzip", "Failed unzip " + filename + " to " + outputfile);
                    //new UnZipTask().execute(filename, outputfile);
                    try {
                        filesdownloaded =  filesdownloaded -1;
                        redownloadCorruptFile(filename, filesize, url);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        redownloadCorruptFile(filename, filesize, url);
                    }
                    // }
                }

            }
            catch(Exception ex) {
                ex.printStackTrace();

            }
        }

    }

    private boolean unzip(String zipFilePath, String unzipAtLocation) throws Exception {
        boolean zipsuccess=false;

        try {
            Log.d("ZipFile",zipFilePath);
            File src = new File(zipFilePath);
            ZipFile zipFile = new ZipFile(src);

            if (zipFile.isEncrypted()) {
                String password="UZS8k9mywDC6aYaFs";
                //Log.d("UnzipMD5",password);
                zipFile.setPassword(password);
            }
            zipFile.extractAll(unzipAtLocation);

            List fileHeaderList = zipFile.getFileHeaders();
            File outputFile =null;
            for(int i=0;i<=fileHeaderList.size()-1;i++){
                FileHeader fileHeader = (FileHeader)fileHeaderList.get(i);
                String filename=fileHeader.getFileName().replace("..","");
                filename = unzipAtLocation+"/"+filename;
                Log.d("Files",filename);
                if(filename.contains(".json")){
                    outputFile = new File(filename);
                }
            }
            updateProgress("Processing "+outputFile.getName()+" ........",10);
            Log.d("OuputFile",outputFile.getAbsolutePath());
            new DumpData(outputFile.getAbsolutePath()).execute();
            zipsuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }



        return zipsuccess;
    }



    private void createDir(File dir) {

        if (dir.exists()) {
            return;
        }

        //Log.v("ZIP E", "Creating dir " + dir.getName());

        if (!dir.mkdirs()) {

            throw new RuntimeException("Can not create dir " + dir);
        }
    }


    private boolean copyDatabaseFromAssets(Context context, String databaseName , boolean overwrite)  {

        File outputFile = context.getDatabasePath(databaseName);
        if (outputFile.exists() && !overwrite) {
            return true;
        }

        outputFile = context.getDatabasePath(databaseName);
        outputFile.getParentFile().mkdirs();
        String [] list;
        try {
            OutputStream outputStream = new FileOutputStream(outputFile);
            //System.out.println("Context path "+ context.getAssets().toString());
            list = getAssets().list("");
            if (list.length > 0) {
                // This is a folder
                for (String file : list) {
                    if(file.equalsIgnoreCase(databaseName)) {
                        try {
                            InputStream inputStream = context.getAssets().open(file);
                            writeToOuput(outputFile, context, databaseName, inputStream, outputStream);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }


            try {
                String unzipPath = Utils.getROOTDIR() + "/data";
                Log.d("UpdateService Unzip ", unzipPath);
                unzip(outputFile.getAbsolutePath(), unzipPath);

            }
            catch(Exception ex){
                ex.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
            if (outputFile.exists()) {
                outputFile.delete();
            }
            return false;
        }

        return true;
    }


    private void writeToOuput(File outputFile, Context context, String databaseName, InputStream inputStream, OutputStream outputStream){
        try {

            //System.out.println("Context path "+ context.getAssets().toString());



            // transfer bytes from the input stream into the output stream
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Close the streams
            outputStream.flush();
            outputStream.close();
            inputStream.close();

            outputFile.renameTo(getDatabasePath(databaseName));
            //System.out.println("Out file "+outputFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
            if (outputFile.exists()) {
                outputFile.delete();
            }
            //return false;
        }
    }





    class DumpData extends AsyncTask<String,Void,String> {
        private String filepath;

        public DumpData(String fp){
            filepath= fp;
        }


        @Override
        protected String doInBackground(String... params){
            String response="";

            //filepath = params[0];
            try {



                try {


                    InputStream is=new FileInputStream(filepath);
                    //InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(params[0]));
                    //BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    //   jsonReader = new JSONParserWS(inputStreamReader);
                    List<HashMap> list = LoganSquare.parse(is,  ArrayList.class);

                    Date starttime= new Date();
                    for(int i=0;i<list.size();i++){
                        String tablename=list.get(i).get("name").toString();
                        //Log.d("Json",tablename);
                        List dataobj= new ArrayList();
                        try {
                            dataobj = (ArrayList) list.get(i).get("data");

                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                        for(int x=0;x<=dataobj.size()-1;x++){
                            List hashlist=(ArrayList)dataobj.get(x);


                            if(tablename.equals("articles")){
                                //updateProgress("Updating articles",10);
                                DatabaseUtil.dumpArticles(hashlist,articlesList);
                            }
                            if(tablename.equals("publications")){
                                //updateProgress("Updating publications",10);
                                DatabaseUtil.dumpPublication(hashlist,publicationsList);
                            }

                            if(tablename.equals("category")){
                                updateProgress("Updating category",10);
                                DatabaseUtil.dumpCategory(hashlist,categoryList);
                            }


                            if(tablename.equals("courts")){
                                //updateProgress("Updating courts",10);
                                DatabaseUtil.dumpCourts(hashlist,courtsList);
                            }


                            if(tablename.equals("dictionary")){
                                //updateProgress("Updating dictionary",10);
                                DatabaseUtil.dumpDictionary(hashlist,dictionaryList);
                            }


                            if(tablename.equals("judgements")){
                                //updateProgress("Updating judgements",10);
                                DatabaseUtil.dumpJudgements(hashlist,judgementsList);
                            }


                            if(tablename.equals("laws")){
                                //updateProgress("Updating laws",10);
                                DatabaseUtil.dumpLaws(hashlist,lawsList);
                            }



                            if(tablename.equals("maxims")){
                                //updateProgress("Updating maxims",10);
                                DatabaseUtil.dumpMaxims(hashlist,maximsList);
                            }


                            if(tablename.equals("precedence")){
                                //updateProgress("Updating precedence",10);
                                DatabaseUtil.dumpPrecedence(hashlist,precedenceList);
                            }

                            if(tablename.equals("principles")){
                                //updateProgress("Updating principles",10);
                                DatabaseUtil.dumpPrinciples(hashlist,principlesList);
                            }



                            if(tablename.equals("ratio")){
                                //updateProgress("Updating ratio",10);
                                DatabaseUtil.dumpRatio(hashlist,ratioList);
                            }

                            if(tablename.equals("rules")){
                                //updateProgress("Updating rules",10);
                                DatabaseUtil.dumpRules(hashlist,rulesList);
                            }

                            if(tablename.equals("sections")){
                                //updateProgress("Updating sections",10);
                                DatabaseUtil.dumpSection(hashlist,sectionsList);
                            }


                            if(tablename.equals("subcategory")){
                                //updateProgress("Updating subcategory",10);
                                DatabaseUtil.dumpSubCategory(hashlist,subcategoryList);
                            }

                            if(tablename.equals("subjects")){
                                //updateProgress("Updating subjects",10);
                                DatabaseUtil.dumpSubjects(hashlist,subjectsList);
                            }

                            if(tablename.equals("summary")){
                                //updateProgress("Updating summary",10);
                                DatabaseUtil.dumpSummary(hashlist,summaryList);

                            }




                            Date currentime = new Date();
                            long timediff = currentime.getTime() - starttime.getTime();
                            // Log.d("TimeUpdate","Difference in time: "+String.valueOf(timediff));


                        }


                        //insert into database here
                        daoSession.runInTx(new Runnable() {
                            @Override
                            public void run() {

                                for(Articles article: articlesList) {

                                    articlesDao.insertOrReplace(article);
                                }
                                articlesList = new ArrayList<Articles>();

                                for(Judgements judgements: judgementsList) {
                                    judgementsDao.insertOrReplace(judgements);
                                }
                                judgementsList = new ArrayList<Judgements>();

                                for(Publications publication: publicationsList) {
                                    publicationsDao.insertOrReplace(publication);
                                }
                                publicationsList = new ArrayList<Publications>();

                                for(Summary summary: summaryList) {
                                    summaryDao.insertOrReplace(summary);
                                }
                                summaryList = new ArrayList<Summary>();

                                for(Category category: categoryList) {
                                    categoryDao.insertOrReplace(category);
                                }
                                categoryList = new ArrayList<Category>();

                                for(Subcategory subcategory: subcategoryList) {
                                    subcategoryDao.insertOrReplace(subcategory);
                                }
                                subcategoryList = new ArrayList<Subcategory>();

                                for(Dictionary dictionary: dictionaryList) {
                                    dictionaryDao.insertOrReplace(dictionary);
                                }
                                dictionaryList = new ArrayList<Dictionary>();

                                for(Maxims maxim: maximsList) {
                                    maximsDao.insertOrReplace(maxim);
                                }
                                maximsList = new ArrayList<Maxims>();

                                for(Rules rules: rulesList) {
                                    rulesDao.insertOrReplace(rules);
                                }
                                rulesList = new ArrayList<Rules>();

                                for(Subjects subjects: subjectsList) {
                                    subjectsDao.insertOrReplace(subjects);
                                }
                                subjectsList = new ArrayList<Subjects>();

                                for(Principles principles: principlesList) {
                                    principlesDao.insertOrReplace(principles);
                                }
                                principlesList = new ArrayList<Principles>();

                                for(Precedence precedence: precedenceList) {
                                    precedenceDao.insertOrReplace(precedence);
                                }
                                precedenceList = new ArrayList<Precedence>();

                                for(Laws laws: lawsList) {
                                    lawsDao.insertOrReplace(laws);
                                }
                                lawsList = new ArrayList<Laws>();

                                for(Courts courts: courtsList) {
                                    courtsDao.insertOrReplace(courts);
                                }
                                courtsList = new ArrayList<Courts>();

                                for(Ratio ratio: ratioList) {
                                    ratioDao.insertOrReplace(ratio);
                                }
                                ratioList = new ArrayList<Ratio>();

                                for(Sections sections: sectionsList) {
                                    sectionsDao.insertOrReplace(sections);
                                }
                                sectionsList = new ArrayList<Sections>();

                            }
                        });


                    }







                    Date currentime = new Date();
                    long timediff = currentime.getTime() - starttime.getTime();
                    Log.d("Finish Time","Difference in time: "+String.valueOf(timediff));

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }




            }
            catch(Exception ex){
                ex.printStackTrace();
            }

            return response;
        }





        @Override
        protected void onPostExecute(String data) {


            totalfilesprocesseddownload=totalfilesprocesseddownload+1;
            updateProgress("Processed "+String.valueOf(totalfilesprocesseddownload)+" of "+String.valueOf(totalfilestodownload),10);
            Log.d("UpdateService","Files downloaded "+String.valueOf(filesdownloaded)+" Total Files processed "+String.valueOf(totalfilesprocesseddownload));
            if(filesdownloaded==totalfilestodownload) {
                updateProgress("Completed....", 10);
                isrunning=false;
                Log.d("FileProcess","Finished processing "+filepath);
                Handler cHandler = new Handler();
                cHandler.postDelayed(new Runnable() {
                    public void run() {

                        new ClearDirectory().execute();

                    }
                }, 300000);

            }




        }



    }
}

