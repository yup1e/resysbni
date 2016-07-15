package com.sahaware.resysbni.helper;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.sahaware.resysbni.implementation.AsyncRepository;
import com.sahaware.resysbni.implementation.CheckConnection;
import com.sahaware.resysbni.implementation.SessionRepository;
import com.sahaware.resysbni.implementation.SqliteRepository;
import com.sahaware.resysbni.repository.IAsyncRepository;
import com.sahaware.resysbni.repository.ICheckConnection;
import com.sahaware.resysbni.repository.ISessionRepository;
import com.sahaware.resysbni.repository.ISqliteRepository;

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
}
