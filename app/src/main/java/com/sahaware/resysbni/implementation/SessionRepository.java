package com.sahaware.resysbni.implementation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.sahaware.resysbni.view.activity.LoginActivity;
import com.sahaware.resysbni.repository.ISessionRepository;
import com.sahaware.resysbni.util.Constants;

import java.util.HashMap;

public class SessionRepository implements ISessionRepository {
    // Shared Preferences
    SharedPreferences pref_login, pref_connection, pref_;

    // Editor for Shared preferences
    Editor editor_login, editor_connection;

    // Context
    Context _context;

    // Shared pref mode


    // Sharedpref file name


    // Constructor
    public SessionRepository(Context context){
        this._context = context;
        pref_login = _context.getSharedPreferences(Constants.PREF_LOGIN, Constants.PRIVATE_MODE);
        pref_connection = _context.getSharedPreferences(Constants.PREF_CON, Constants.PRIVATE_MODE);
        editor_connection = pref_connection.edit();
        editor_login = pref_login.edit();
    }

    @Override
    public void createIPSession(String ip){
        editor_connection.putString(Constants.KEY_IP, ip);
        editor_connection.commit();
    }

    @Override
    public String getIP(){
        String ip;
        ip = pref_connection.getString(Constants.KEY_IP, null);
        return ip;
    }

    @Override
    public String getId(){
        String id;
        id = pref_login.getString(Constants.KEY_ID, null);
        return id;
    }

    @Override
    public String getToken(){
        String token;
        token = pref_login.getString(Constants.KEY_TOKEN, null);
        return token;
    }

    @Override
    public String getUsername(){
        String username;
        username = pref_login.getString(Constants.KEY_USERNAME, null);
        return username;
    }

    @Override
    public String getRules(){
        String rules;
        rules = pref_login.getString(Constants.KEY_RULES, null);
        return rules;
    }

    /**
     * Create login session
     * */
    @Override
    public void createLoginSession(String id, String username, String token, String rules){
        editor_login.putBoolean(Constants.IS_LOGIN, true);
        editor_login.putString(Constants.KEY_ID, id);
        editor_login.putString(Constants.KEY_USERNAME, username);
        editor_login.putString(Constants.KEY_TOKEN, token);
        editor_login.putString(Constants.KEY_RULES, rules);

        // commit changes
        editor_login.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    @Override
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    @Override
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(Constants.KEY_ID, pref_login.getString(Constants.KEY_ID, null));
        user.put(Constants.KEY_USERNAME, pref_login.getString(Constants.KEY_USERNAME, null));
        user.put(Constants.KEY_TOKEN, pref_login.getString(Constants.KEY_TOKEN, null));
        user.put(Constants.KEY_RULES, pref_login.getString(Constants.KEY_RULES, null));
        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){

        // Clearing all data from Shared Preferences
        editor_login.clear();
        editor_login.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref_login.getBoolean(Constants.IS_LOGIN, false);
    }
}