package com.legalpedia.android.app.util;

import android.util.Log;

import com.legalpedia.android.app.http.RestClient;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adebayoolabode on 10/31/16.
 */

public class LGPClient {
    private static String rooturl="http://resources.legalpediaonline.com/api/v1/";
    //private static String rooturl="http://192.168.1.3:9999/api/v1/";
    private static RestClient client;
    private static boolean isloggedin=false;

    public static String getURL(String endpoint){
        return rooturl+endpoint;
    }

    public static JSONObject doRegister( String username, String email, String password, String telephone){
        JSONObject response=new JSONObject();
        String url=getURL("register");
        Map<String,String> params=new HashMap<String,String>();
        params.put("username",username);
        params.put("email",email);
        params.put("password",password);
        params.put("phone",telephone);
        try{
            response=RestClient.doPost(url,params);
        }
        catch(Exception ex){

            try {
                response.put("status", "failure");
                response.put("message", "Network error. Try again later.");
            }
            catch(Exception e){

            }

        }
        return response;
    }

    public static JSONObject doLogin(String username,String password){
        JSONObject response = new JSONObject();


            String url = getURL("auth");
            Map<String, String> params = new HashMap<String, String>();
            Log.d("Username", username);
            Log.d("Password", password);
            params.put("username", username);
            params.put("password", password);
            try {
                response = RestClient.doPost(url, params);
                String status = response.getString("status");
                if (status.equals("ok")) {
                    isloggedin = true;
                }
                Log.d("LGPClient", response.toString());
            } catch (Exception ex) {

                try {
                    response.put("status", "failure");
                    response.put("message", "Network error. Try again later.");
                } catch (Exception e) {

                }

            }

        return response;
    }



    public static JSONObject doReset(String info){
        JSONObject response = new JSONObject();


        String url = getURL("reset");
        Map<String, String> params = new HashMap<String, String>();
        params.put("info", info);

        try {
            response = RestClient.doPost(url, params);
            String status = response.getString("status");
            if (status.equals("ok")) {
                isloggedin = true;
            }
            Log.d("LGPClient", response.toString());
        } catch (Exception ex) {

            try {
                response.put("status", "failure");
                response.put("message", "Network error. Try again later.");
            } catch (Exception e) {

            }

        }

        return response;
    }



    public static JSONObject doChangePassword(String oldpassword,String newpassword){
        JSONObject response = new JSONObject();


        String url = getURL("changepassword");
        Map<String, String> params = new HashMap<String, String>();
        params.put("oldpassword", oldpassword);
        params.put("newpassword", newpassword);
        try {
            response = RestClient.doPost(url, params);
            String status = response.getString("status");
            if (status.equals("ok")) {
                isloggedin = true;
            }
            Log.d("LGPClient", response.toString());
        } catch (Exception ex) {

            try {
                response.put("status", "failure");
                response.put("message", "Network error. Try again later.");
            } catch (Exception e) {

            }

        }

        return response;
    }



    public static JSONObject doLogDevice(String deviceName,String deviceId,String registrationId){
        JSONObject response=new JSONObject();
        String url=getURL("logdevice");
        Map<String,String> params=new HashMap<String,String>();
        params.put("devicename",deviceName);
        params.put("deviceid",deviceId);
        params.put("registrationid",registrationId);
        try {
            response = RestClient.doPost(url, params);
            Log.d("FMWClient", response.toString());
        }
        catch(Exception ex){

            try {
                response.put("status", "failure");
                response.put("message", "Network error. Try again later.");
            }
            catch(Exception e){

            }

        }
        return response;
    }


    public static JSONObject doVerify(String code){
        JSONObject response=new JSONObject();
        String url=getURL("verify");
        Map<String,String> params=new HashMap<String,String>();
        params.put("code",code);
        try{
            response=RestClient.doPost(url,params);
        }
        catch(Exception ex){

            try {
                response.put("status", "failure");
                response.put("message", "Network error. Try again later.");
            }
            catch(Exception e){

            }

        }
        return response;
    }

    public static File downloadData(String url,String path){
       File responsedata=null;

        try{
            responsedata=RestClient.doGetFile(url,path);
        }
        catch(Exception e){
            try {
                responsedata = RestClient.doGetFile(url, path);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }

        }
        return responsedata;
    }




    public static JSONObject getUpdates(String uid,String packageid,String updates){
        JSONObject response=new JSONObject();
        String url=getURL("checkupdates");
        Map<String,String> params=new HashMap<String,String>();
        params.put("uid",uid);
        params.put("packageid",packageid);
        params.put("updates",updates);
        try{
            response=RestClient.doGet(url);
        }
        catch(Exception ex){

            try {
                response.put("status", "failure");
                response.put("message", "Network error. Try again later.");
            }
            catch(Exception e){

            }

        }
        return response;
    }


    public static JSONObject verifyIntegrityRequest(String uid,String packageid,String resourcestat){
        JSONObject response=new JSONObject();
        String url=getURL("verifyintegrity");
        Map<String,String> params=new HashMap<String,String>();
        params.put("uid",uid);
        params.put("packageid",packageid);
        params.put("resourcestat",resourcestat);
        try{
            response=RestClient.doPost(url,params);
        }
        catch(Exception ex){

            try {
                response.put("status", "failure");
                response.put("message", "Network error. Try again later.");
            }
            catch(Exception e){

            }

        }
        return response;
    }


    public static JSONObject reportBug(String uid,String title,String description){
        JSONObject response=new JSONObject();
        String url=getURL("reportbug");
        Map<String,String> params=new HashMap<String,String>();
        params.put("uid",uid);
        params.put("title",title);
        params.put("description",description);
        try{
            response=RestClient.doPost(url,params);
        }
        catch(Exception ex){

            try {
                response.put("status", "failure");
                response.put("message", "Network error. Try again later.");
            }
            catch(Exception e){

            }

        }
        return response;
    }


    public static JSONObject addNote(String uid,String resource,String resourceid,String title,String content,String isprivate,String cancomment){
        JSONObject response=new JSONObject();
        String url=getURL("notes/add");

        Map<String,String> params=new HashMap<String,String>();
        params.put("resource",resource);
        params.put("resourceid",resourceid);
        params.put("title",title);
        params.put("content",content);
        params.put("uid",uid);
        params.put("isprivate",isprivate);
        params.put("cancomment",cancomment);
        try{
            response=RestClient.doPost(url,params);
        }
        catch(Exception ex){

            try {
                response.put("status", "failure");
                response.put("message", "Network error. Try again later.");
            }
            catch(Exception e){

            }

        }
        return response;
    }


    public static JSONObject addAnnotation(String uid,String resource,String resourceid,String title,String content,String position,String comment){
        JSONObject response=new JSONObject();
        String url=getURL("annotations/add");

        Map<String,String> params=new HashMap<String,String>();
        params.put("resource",resource);
        params.put("resourceid",resourceid);
        params.put("title",title);
        params.put("content",content);
        params.put("uid",uid);
        params.put("position",position);
        params.put("comment",comment);
        try{
            response=RestClient.doPost(url,params);
        }
        catch(Exception ex){

            try {
                response.put("status", "failure");
                response.put("message", "Network error. Try again later.");
            }
            catch(Exception e){

            }

        }
        return response;
    }



    public static JSONObject postResearchRequest(String uid,String message,String title,String description,String researchdetail,String citations,String expecteddate){
        JSONObject response=new JSONObject();
        String url=getURL("genie/create");

        Map<String,String> params=new HashMap<String,String>();
        params.put("uid",uid);
        params.put("message",message);
        params.put("title",title);
        params.put("description",description);
        params.put("researchdetail",researchdetail);
        params.put("citations",citations);
        params.put("expecteddate",expecteddate);
        try{
            response=RestClient.doPost(url,params);
        }
        catch(Exception ex){

            try {
                response.put("status", "failure");
                response.put("message", "Network error. Try again later.");
            }
            catch(Exception e){

            }

        }
        return response;
    }



    public static JSONObject getProfile(){
        JSONObject response=new JSONObject();
        String url=getURL("getprofile");
        try{
            response=RestClient.doGet(url);
        }
        catch(Exception ex){

            try {
                response.put("status", "failure");
                response.put("message", "Network error. Try again later.");
            }
            catch(Exception e){

            }

        }
        return response;
    }


    public static JSONObject ResetAccount(){
        JSONObject response=new JSONObject();
        String url=getURL("resetaccount");
        try{
            response=RestClient.doGet(url);
        }
        catch(Exception ex){

            try {
                response.put("status", "failure");
                response.put("message", "Network error. Try again later.");
            }
            catch(Exception e){

            }

        }
        return response;
    }


    public static JSONObject updateProfile(String firstname,String lastname,String email,String phone,String skype,String address,String address1,String city,String town,String state,String country){
        JSONObject response=new JSONObject();
        String url=getURL("updateprofile");
        Map<String,String> params=new HashMap<String,String>();
        params.put("firstname",firstname);
        params.put("lastname",lastname);
        params.put("email",email);
        params.put("phone",phone);
        params.put("skype",skype);
        params.put("address",address);
        params.put("address1",address1);
        params.put("city",city);
        params.put("town",town);
        params.put("state",state);
        params.put("country",country);
        try{
            response=RestClient.doPost(url,params);
        }
        catch(Exception ex){

            try {
                response.put("status", "failure");
                response.put("message", "Network error. Try again later.");
            }
            catch(Exception e){

            }

        }
        return response;
    }


    public static JSONObject uploadProfilePicture(String uid,File photo){
        JSONObject response=new JSONObject();
        String url=getURL("profile/picture");
        String filefield="picture";
        String fileMimeType="image/png";
        String filepath=photo.getPath();
        Log.d("UploadFile",url);
        Log.d("File Path",photo.getPath());
        Map<String,String> params=new HashMap<String,String>();
        params.put("uid",uid);
        try{
           response=RestClient.doMultipartUpload(url,params, filepath,filefield, fileMimeType);
        }
        catch(Exception ex){
            ex.printStackTrace();
            try {
                response.put("status", "failure");
                response.put("message", "Network error. Try again later.");
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
        return response;
    }


    public JSONObject downloadItem(){
        JSONObject response=new JSONObject();
        String url="";
        Map<String,String> params=new HashMap<String,String>();
        try{
            response=RestClient.doPost(url,params);
        }
        catch(Exception ex){

            try {
                response.put("status", "failure");
                response.put("message", "Network error. Try again later.");
            }
            catch(Exception e){

            }

        }
        return response;
    }
}
