package com.legalpedia.android.app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.IoniconsModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.joanzapata.iconify.fonts.MeteoconsModule;
import com.joanzapata.iconify.fonts.SimpleLineIconsModule;
import com.joanzapata.iconify.fonts.TypiconsModule;
import com.joanzapata.iconify.fonts.WeathericonsModule;
import com.legalpedia.android.app.models.DaoMaster;
import com.legalpedia.android.app.models.DaoSession;

import org.greenrobot.greendao.database.Database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by adebayoolabode on 11/23/16.
 */

public class App  extends Application {
    public static final boolean ENCRYPTED = false;


    private DaoSession daoSession;
    private DaoMaster daoMaster;
    private SQLiteDatabase sqlitedb;
    private static Context mContext;
    private static Database db;
    private static String path="lp_android.db";
    private static String path_encrypted="lp_android_enrypted.db";
    private String key="8uyTfd54vCs@";



    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        try {

            Log.d("App","copying database file now");
          //  copyDatabaseFromAssets(this.getApplicationContext(),path,true);
            File outputFile = new File("");
            if(ENCRYPTED) {
                 outputFile = getApplicationContext().getDatabasePath(path_encrypted);
            }
            else{
                 outputFile = getApplicationContext().getDatabasePath(path);
            }

        if (!outputFile.exists()) {
            Log.d("Getting 1",outputFile.getAbsolutePath());
            if(ENCRYPTED){
                copyDatabaseFromAssets(getApplicationContext(), path_encrypted, false);
            }else {
                copyDatabaseFromAssets(getApplicationContext(), path, false);
            }
        }
        //DatabaseUpgradeHelper helper = new DatabaseUpgradeHelper(this, ENCRYPTED ? path_encrypted : path);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ?  path_encrypted : path);
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb(key) : helper.getWritableDb();

        daoMaster  = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        adjustDatabase(db);

        //DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, path);
        //db = helper.getWritableDb();
        //daoSession = new DaoMaster(db).newSession();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try{
            Iconify
                    .with(new FontAwesomeModule())
                    .with(new EntypoModule())
                    .with(new TypiconsModule())
                    .with(new MaterialModule())
                    .with(new MaterialCommunityModule())
                    .with(new MeteoconsModule())
                    .with(new WeathericonsModule())
                    .with(new SimpleLineIconsModule())
                    .with(new IoniconsModule());
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }




    public static Context getContext() {
        return mContext;
    }

    private void writeToOuput(File outputFile,Context context,String databaseName,InputStream inputStream,OutputStream outputStream){
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

            outputFile.renameTo(context.getDatabasePath(databaseName));
            System.out.println("Out file "+outputFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
            if (outputFile.exists()) {
                outputFile.delete();
            }
            //return false;
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
            System.out.println("Context path "+ context.getAssets().toString());
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





        } catch (IOException e) {
            e.printStackTrace();
            if (outputFile.exists()) {
                outputFile.delete();
            }
            return false;
        }

        return true;
    }

    public void adjustDatabase(Database db){
        try{
            String query="CREATE TABLE IF NOT EXISTS annotations(_id integer primary key autoincrement,uid integer,resource integer,title varchar(500),content text,resourceid integer,comment text);";
            db.execSQL(query);
            Log.d("Query",query);

            //db.execSQL("PRAGMA automatic_index = off;");


        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public DaoMaster getDaoMaster() {
        return daoMaster;
    }
    public DaoSession getDaoSession() {
        return daoSession;
    }
}
