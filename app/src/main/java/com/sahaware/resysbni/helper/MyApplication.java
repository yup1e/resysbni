package com.sahaware.resysbni.helper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.sahaware.resysbni.implementation.AsyncRepository;
import com.sahaware.resysbni.implementation.CheckConnection;
import com.sahaware.resysbni.implementation.SessionRepository;
import com.sahaware.resysbni.implementation.SqliteRepository;
import com.sahaware.resysbni.repository.IAsyncRepository;
import com.sahaware.resysbni.repository.ICheckConnection;
import com.sahaware.resysbni.repository.ISessionRepository;
import com.sahaware.resysbni.repository.ISqliteRepository;
import com.sahaware.resysbni.view.activity.MainActivity;

/**
 * Created by DILo on 5/12/2016.
 */
public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        DependencyInjection.Add(ISessionRepository.class, new SessionRepository(context));
        DependencyInjection.Add(ICheckConnection.class,new CheckConnection());
        DependencyInjection.Add(ISqliteRepository.class, new SqliteRepository(context));
        DependencyInjection.Add(IAsyncRepository.class, new AsyncRepository(context));
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public static Boolean validateOtherLogin(Integer Code, Activity Act) {
        if (Code == 401) {
            Toast.makeText(Act, "User sudah login di tempat lain", Toast.LENGTH_LONG).show();
            MainActivity.logout(Act.getApplicationContext());
            return true;
        }else{
            return false;
        }
    }

    public static Boolean isMarketing(String role){
        if(role.equalsIgnoreCase("RM/JRM") || role.equalsIgnoreCase("MBA") || role.equalsIgnoreCase("ABB")) {
            return true;
        }
        return false;
    }

    public static Boolean isUkc(String role){
        if(role.equalsIgnoreCase("KepalaUKC")) {
            return true;
        }
        return false;
    }

    public static Boolean isRm(String role){
        if(role.equalsIgnoreCase("Penyelia RM")) {
            return true;
        }
        return false;
    }

    public static Boolean isSkc(String role){
        if(role.equalsIgnoreCase("KepalaSKC")) {
            return true;
        }
        return false;
    }

    public static Boolean isCnm(String role){
        if(role.equalsIgnoreCase("CNM")) {
            return true;
        }
        return false;
    }

    public static Boolean isSameId(Integer id){
        if(id.equals(DependencyInjection.Get(ISessionRepository.class).getId())){
            return true;
        }
        return false;
    }
}
