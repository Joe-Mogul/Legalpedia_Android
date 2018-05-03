package com.legalpedia.android.app.http;


import android.text.TextUtils;
import android.util.Log;

import com.legalpedia.android.app.util.Utils;

import org.apache.http.client.HttpClient;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by adebayoolabode on 1/16/16.
 */
public class RestClient {


    private static JSONObject responseobject=null;

    private static HttpClient client=null;
    static final String COOKIES_HEADER = "Set-Cookie";
    //static CookieManager msCookieManager = new  CookieManager(null, CookiePolicy.ACCEPT_ALL);
    private static CookieStore cookieStore;
    private static HttpURLConnection connection = null;
    static java.net.CookieManager msCookieManager = new java.net.CookieManager();

    private static final String boundary = "===" + System.currentTimeMillis() + "===";;
    private static final String LINE_FEED = "\r\n";




    public static HttpURLConnection getInstance(URL url) {
        //if(connection == null) {
        try {
            connection = (HttpURLConnection) url.openConnection();

        }
        catch(Exception ex){

        }
        // }
        return connection;
    }

    public static CookieManager getCookiemanager(){
        if(msCookieManager==null){
            msCookieManager = new java.net.CookieManager();
        }

        return msCookieManager;
    }

    public static JSONObject  doGet(String url){
        URL myUrl = null;
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        String response = "";


        try {


            //System.out.println(url);
            myUrl = new URL(url);

            HttpURLConnection conn = RestClient.getInstance(myUrl);
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("LP-Device-Type",Utils.getDeviceType());
            conn.setRequestProperty("LP-Device-OS",Utils.getOperatingSystem());
            conn.setRequestProperty("LP-App-Version", Utils.getAppVersion());
            try {
                conn.setRequestProperty("LP-Device-Id", Utils.getDeviceId());
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            conn.setRequestProperty("LP-App-Secret",Utils.getSecret());

            if (msCookieManager.getCookieStore().getCookies().size() > 0) {
                // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                connection.setRequestProperty("Cookie",
                        TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
            }
            // Get the response code
            int statusCode = connection.getResponseCode();

            InputStream inputStream = null;

            if (statusCode >= 200 && statusCode < 400) {
                // Create an InputStream in order to extract the response object
                inputStream = connection.getInputStream();
            }
            else {
                inputStream = connection.getErrorStream();
            }
            dumpCookies(conn);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }
            try {
                //System.out.println(response);
                responseobject = new JSONObject(response);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            bufferedReader.close();
            inputStream.close();
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseobject;
    }




    public static File doGetFile(String url,String localPath){
        URL myUrl = null;
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        byte responsedata[]=null;
        String response="";
        File downloadedfile = null;
        try {

            //System.out.println(url);
            myUrl = new URL(url);

            HttpURLConnection conn = RestClient.getInstance(myUrl);
            conn.setReadTimeout(60000);
            conn.setConnectTimeout(60000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("LP-Device-Type",Utils.getDeviceType());
            conn.setRequestProperty("LP-Device-OS",Utils.getOperatingSystem());
            conn.setRequestProperty("LP-App-Version", Utils.getAppVersion());
            try {
                conn.setRequestProperty("LP-Device-Id", Utils.getDeviceId());
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            conn.setRequestProperty("LP-App-Secret",Utils.getSecret());
            try{
                downloadedfile = new File(localPath);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            try{
                if(downloadedfile.exists()){
                    String downloadedLength = String.valueOf(downloadedfile.length());
                    connection.setRequestProperty("Range", "bytes=" + downloadedLength + "-");


                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
            if (msCookieManager.getCookieStore().getCookies().size() > 0) {
                // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                connection.setRequestProperty("Cookie",
                        TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
            }
            // Get the response code
            int statusCode = connection.getResponseCode();

            InputStream inputStream = null;

            if (statusCode >= 200 && statusCode < 400) {
                // Create an InputStream in order to extract the response object
                inputStream = connection.getInputStream();
            }
            else {
                inputStream = connection.getErrorStream();
            }
            InputStream input = new BufferedInputStream(inputStream);
            //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = "";

            OutputStream output = new FileOutputStream(localPath);

            byte data[] = new byte[1024];

            long total = 0;

            int count;

            //if(input.available()>0) {
                while ((count = input.read(data)) != -1) {

                    total += count;

                    //int progress = (int) ((total * 100) / lengthOfFile);

                    //sendProgress(progress, resultReceiver);

                    output.write(data, 0, count);
                }
                dumpCookies(conn);
                output.flush();
                output.close();
                input.close();
                conn.disconnect();

            /**}else{
                //input.close();
                //conn.disconnect();
                doGetFile(url,localPath);
            }*/



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return downloadedfile;
    }



    public static JSONObject doPost(String url,Map<String,String> params){

        URL myUrl = null;
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        String response = "";


        try {
            myUrl = new URL(url);
            HttpURLConnection conn  = RestClient.getInstance(myUrl);
            conn.setReadTimeout(60000);
            conn.setConnectTimeout(60000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("LP-Device-Type",Utils.getDeviceType());
            conn.setRequestProperty("LP-Device-OS",Utils.getOperatingSystem());
            conn.setRequestProperty("LP-App-Version", Utils.getAppVersion());
            try {
                conn.setRequestProperty("LP-Device-Id", Utils.getDeviceId());
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            conn.setRequestProperty("LP-App-Secret",Utils.getSecret());
            if (msCookieManager.getCookieStore().getCookies().size() > 0) {
                // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                connection.setRequestProperty("Cookie",
                        TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
            }

            String postData ="";
            for ( String key : params.keySet() ) {

                //postData=postData+key+"="+params.get(key)+ "&";
                postData=postData+URLEncoder.encode(key,  "UTF-8") + "="+ URLEncoder.encode(params.get(key), "UTF-8")+ "&";
            }
            //System.out.println(postData);
            OutputStream os = conn.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bufferedWriter.write(postData);
            bufferedWriter.flush();
            bufferedWriter.close();

            int status = connection.getResponseCode();
            InputStream inputStream;
            if (status >= 200 && status < 400) {
                inputStream = conn.getInputStream();

            } else{
                inputStream = connection.getErrorStream();

            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }
            try {
                Log.d("RestClient",response);
                responseobject = new JSONObject(response);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }

            dumpCookies(conn);
            bufferedReader.close();
            inputStream.close();
            conn.disconnect();
            os.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return responseobject;

    }


    public static JSONObject doPostMultiPart(String url,Map<String,String> params,File file){

        URL myUrl = null;
        JSONObject responseobject=null;
        String response = "";
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        try {
            myUrl = new URL(url);
            HttpURLConnection conn  = RestClient.getInstance(myUrl);
            conn.setReadTimeout(120000);
            conn.setConnectTimeout(120000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);
            if (msCookieManager.getCookieStore().getCookies().size() > 0) {
                // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                connection.setRequestProperty("Cookie",
                        TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
            }

            String postData ="";
            for ( String key : params.keySet() ) {

                //postData=postData+key+"="+params.get(key)+ "&";
                postData=postData+URLEncoder.encode(key,  "UTF-8") + "="+ URLEncoder.encode(params.get(key), "UTF-8")+ "&";
            }
            //System.out.println(postData);
            OutputStream os = conn.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bufferedWriter.write(postData);


            String fileName = file.getName();
            String fieldName="picture";
            bufferedWriter.write("--" + boundary+LINE_FEED);
            bufferedWriter.write("Content-Disposition: form-data; name=\"" + fieldName+ "\"; filename=\"" + fileName + "\""+ LINE_FEED);
            bufferedWriter.write("Content-Transfer-Encoding: binary"+LINE_FEED);


            FileInputStream fStream = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = fStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            os.flush();
            fStream.close();

            bufferedWriter.write("--" + boundary+LINE_FEED);
            bufferedWriter.flush();
            bufferedWriter.close();




            try {
                int status = connection.getResponseCode();
                InputStream inputStream;
                if (status >= 200 && status < 400) {
                    inputStream = conn.getInputStream();

                } else{
                    inputStream = connection.getErrorStream();

                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }

                dumpCookies(conn);


                //System.out.println(response);
                responseobject = new JSONObject(response);

                bufferedReader.close();
                inputStream.close();
                conn.disconnect();
                os.close();

            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseobject;

    }



    public static JSONObject doMultipartUpload(String urlTo, Map<String, String> params, String filepath, String filefield, String fileMimeType)  {
        HttpURLConnection conn;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;
        JSONObject responseobject=null;
        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        String[] q = filepath.split("/");
        int idx = q.length - 1;
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        try {
            File file = new File(filepath);
            FileInputStream fileInputStream = new FileInputStream(file);

            URL myUrl = new URL(urlTo);
            conn  = RestClient.getInstance(myUrl);
            conn.setReadTimeout(1200000);
            conn.setConnectTimeout(1200000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //conn.setUseCaches(false);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            if (msCookieManager.getCookieStore().getCookies().size() > 0) {
                // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                connection.setRequestProperty("Cookie",
                        TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
            }
            outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);

            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);

            // Upload POST Data
            Iterator<String> keys = params.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = params.get(key);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(value);
                outputStream.writeBytes(lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


            int status = connection.getResponseCode();

            if (status >= 200 && status < 400) {
                inputStream = conn.getInputStream();

            } else{
                inputStream = connection.getErrorStream();

            }


            result = convertStreamToString(inputStream);
            dumpCookies(conn);
            responseobject = new JSONObject(result);
            fileInputStream.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();

            return responseobject;
        } catch (Exception e) {
            e.printStackTrace();

        }

        return responseobject;
    }


    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static void dumpCookies(HttpURLConnection connection){
        Map<String, List<String>> headerFields = connection.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

        if (cookiesHeader != null) {
            for (String cookie : cookiesHeader) {
                Log.d("Cookie Values",cookie);
                msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
            }
        }
    }

    public static java.net.CookieManager getMsCookieManager(){
        return msCookieManager;
    }



}
