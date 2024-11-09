package com.neotechInnovations.retailrevamp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.neotechInnovations.retailrevamp.Constant.Tags;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
    public static String userName, userImage,userId;

    public SessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(Tags.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public static void saveSession(String userId , String userName,String userImage){
        editor.putString(Tags.SESSION_USER_ID,userId).commit();
        editor.putString(Tags.SESSION_USER_NAME,userName).commit();
        editor.putString(Tags.SESSION_USER_IMAGE,userImage).commit();
    }
    public void getSession(){
        String userIdString = sharedPreferences.getString(Tags.SESSION_USER_ID,null);
        if(userIdString==null||userIdString.equals("")){
            userId= null;
            userName= null;
            clearSession();
            return;
        }
        else {
            userId = userIdString;
            userName = sharedPreferences.getString(Tags.SESSION_USER_NAME,null);
            userImage = sharedPreferences.getString(Tags.SESSION_USER_IMAGE,null);
        }
    }
    public void clearSession(){
        userId = null;
        userName = null;
        userImage=null;
        editor.putString(Tags.SESSION_USER_ID,null).commit();
        editor.putString(Tags.SESSION_USER_NAME,null).commit();
        editor.putString(Tags.SESSION_USER_IMAGE,null).commit();
    }
}

