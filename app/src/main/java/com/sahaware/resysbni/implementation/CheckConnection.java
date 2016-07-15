package com.sahaware.resysbni.implementation;

import android.util.Log;

import com.sahaware.resysbni.helper.DependencyInjection;
import com.sahaware.resysbni.repository.ICheckConnection;
import com.sahaware.resysbni.repository.ISessionRepository;
import com.sahaware.resysbni.util.Constants;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by Administrator on 03/12/2015.
 */
public class CheckConnection implements ICheckConnection {


    public Boolean isConnectedToServer() {
        String url = Constants.ip;
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(3000);
            connection.connect();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean isConnectedToInternet() {
        String url = "https://www.google.com/";
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(3000);
            connection.connect();
            return true;
        } catch (Exception e) {

            return false;
        }
    }


}

