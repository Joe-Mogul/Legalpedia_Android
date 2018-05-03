package com.legalpedia.android.app.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.legalpedia.android.app.App;
import com.legalpedia.android.app.LoginActivity;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
	public static final String DIR_FMW= Environment.DIRECTORY_DOWNLOADS + "/legalpedia/";
	private static boolean HIDDEN=false;
	private static String ERROR="-1";
	private static String BrandName = Build.MANUFACTURER;      // Manufacturer will come I think, Correct me if I am wrong :)  Brand name like Samsung or Mircomax
	private static String os = "android";
	private static String myDeviceModel = android.os.Build.MODEL;
	private static String SDKName = String.valueOf(Build.VERSION.SDK_INT);
	private static String DeviceName = android.os.Build.DEVICE;           // Device
	private static String DeviceModel = android.os.Build.MODEL;            // Model
	private static String Productname = android.os.Build.PRODUCT;          // Product
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
        	
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              //Read byte from input stream
            	
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              
              //Write byte from output stream
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    
    public static boolean isSDCardPresent(){


Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

if(isSDPresent)
{
  return true;
}
else
{
	 return false;
}


    }



	public static String getDeviceId(){

		Context context = App.getContext();
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId;
		if (telephonyManager.getDeviceId() != null)
			deviceId = telephonyManager.getDeviceId(); //*** use for mobiles
		else {
			deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
		}
		return deviceId;


	}
	public static String getSecret(){

		String secret="a4526da1-eddd-48da-8b16-d6cb06424381";
		return secret;
	}

	public static String getAppVersion(){

		String version="0.9.7";
		return version;
	}


	public static String getDeviceType(){
		if(BrandName==""){
			BrandName="Unknown";
		}
		String devicetype=" "+BrandName+"-"+DeviceName;
		return devicetype;
	}

	public static String getOperatingSystem(){

		String osname=" "+os+"-"+SDKName;
		return osname;
	}

	public static boolean isServiceRunning(Context ctx,String srvc) {
		ActivityManager manager = (ActivityManager) ctx.getSystemService(ctx.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
			Log.d("Running Services",service.service.getClassName());
			if(srvc.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}


	public static void hasFinishedRegistering(){
		/**
		 int i=0;

		 while(!RegisterTask.isregistered && !RegisterTask.iscompleted){
		 System.out.println("Waiting for registration to finish");

		 if(i>=RegisterTask.maxwait){
		 break;
		 }
		 i++;
		 try {
		 Thread.sleep(1000);
		 } catch (InterruptedException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }
		 }
		 */
	}

	public static File getBaseStorageDirectory() {
		return Environment.getExternalStoragePublicDirectory(DIR_FMW);
	}

	public static File getFilepath(String filename) {
		return Environment.getExternalStoragePublicDirectory(DIR_FMW + filename);
	}

	public static void cleanDownloadDirectory() {
		File downloadDir = getBaseStorageDirectory();

		for (File file: downloadDir.listFiles())
			file.delete();
	}


	public static boolean isWifiEnabled(Context ctx){
		boolean status=false;
		 ConnectivityManager connMgr = (ConnectivityManager)
				ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info =  connMgr.getActiveNetworkInfo();
		if (info.isConnectedOrConnecting() && info.getType()==ConnectivityManager.TYPE_WIFI) {
			status=true;
		}

		return status;
	}

	public static boolean is3GNetworkEnabled(Context ctx){
		boolean status=false;
		ConnectivityManager connMgr = (ConnectivityManager)
				ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info =  connMgr.getActiveNetworkInfo();


		if (info.isConnectedOrConnecting() && info.getType()==ConnectivityManager.TYPE_MOBILE) {
			status = true;
		}
		return status;
	}





	public static boolean hasNetwork(Context ctx){
		ConnectivityManager connManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (mWifi.isConnected() == false && mMobile.isConnected() == false) {
			return false;
		}else{
			return true;
		}
	}
    
    public static boolean isNetworkAvailable(Context ctx) {
    	ConnectivityManager cm =
    	        (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
    	    NetworkInfo netInfo = cm.getActiveNetworkInfo();

    	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
    	        return true;
    	    }
    	    return false;
	}


	public static void deleteDir(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				deleteDir(child);

		fileOrDirectory.delete();
	}

	public static String getROOTDIR(){
		String rootdir="";
		if(externalMemoryAvailable()) {
			if (HIDDEN) {
				rootdir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.legalpedia";
			} else {
				rootdir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/legalpedia";
			}

		}else{
			if (HIDDEN) {
				rootdir = Environment.getDataDirectory().getAbsolutePath() + "/.legalpedia";
			} else {
				rootdir = Environment.getDataDirectory().getAbsolutePath() + "/legalpedia";
			}
		}
		File f=new File(rootdir);
		if(!f.exists()){
			f.mkdir();
		}
		return rootdir;
	}

    public static int getLoadingDelayInterval(){
    	
    	
    	return 30;
    }

	public static boolean externalMemoryAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	public static String getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return formatSize(availableBlocks * blockSize);
	}

	public static String getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return formatSize(totalBlocks * blockSize);
	}

	public static String getAvailableExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return formatSize(availableBlocks * blockSize);
		} else {
			return ERROR;
		}
	}

	public static String getTotalExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			return formatSize(totalBlocks * blockSize);
		} else {
			return ERROR;
		}
	}

	public static String formatSize(long size) {
		String suffix = null;

		if (size >= 1024) {
			suffix = "KB";
			size /= 1024;
			if (size >= 1024) {
				suffix = "MB";
				size /= 1024;
			}
		}

		StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

		int commaOffset = resultBuffer.length() - 3;
		while (commaOffset > 0) {
			resultBuffer.insert(commaOffset, ',');
			commaOffset -= 3;
		}

		if (suffix != null) resultBuffer.append(suffix);
		return resultBuffer.toString();
	}

	@SuppressLint("NewApi")
	public static String getRealPathFromURI_API19(Context context, Uri uri){
		String filePath = "";
		String wholeID = DocumentsContract.getDocumentId(uri);

		// Split at colon, use second item in the array
		String id = wholeID.split(":")[1];

		String[] column = { MediaStore.Images.Media.DATA };

		// where id is equal to
		String sel = MediaStore.Images.Media._ID + "=?";

		Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				column, sel, new String[]{ id }, null);

		int columnIndex = cursor.getColumnIndex(column[0]);

		if (cursor.moveToFirst()) {
			filePath = cursor.getString(columnIndex);
		}
		cursor.close();
		return filePath;
	}


	@SuppressLint("NewApi")
	public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		String result = null;

		CursorLoader cursorLoader = new CursorLoader(
				context,
				contentUri, proj, null, null, null);
		Cursor cursor = cursorLoader.loadInBackground();

		if(cursor != null){
			int column_index =
					cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			result = cursor.getString(column_index);
		}
		return result;
	}

	public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
		int column_index
				= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}


	public static String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}
}