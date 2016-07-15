package com.sahaware.resysbni.implementation;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sahaware.resysbni.helper.DependencyInjection;
import com.sahaware.resysbni.repository.IAsyncRepository;
import com.sahaware.resysbni.repository.ISessionRepository;
import com.sahaware.resysbni.util.Constants;
import com.sahaware.resysbni.view.activity.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by DILo on 5/14/2016.
 */
public class AsyncRepository implements IAsyncRepository {
    Context context;
    private static final String TAG = "ResysBNI";
    boolean status = false,
            flag = false;

    public List<Boolean> value = new ArrayList<Boolean>();
    public AsyncRepository(Context context) {
        this.context = context;
    }

    public List<Boolean> getUserDetail(String username, String password){
        checkUser(username, password);
        value.add(status);
        value.add(flag);
        return (value);
    }

    public void checkUser(String username, String password){
        final int DEFAULT_TIMEOUT = 20 * 1000;
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;
        try {
            jsonParams.put("userName", username);
            jsonParams.put("password", password);
            entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT);
        client.setConnectTimeout(DEFAULT_TIMEOUT);
        client.setResponseTimeout(DEFAULT_TIMEOUT);
        client.post(context, Constants.API_LOGIN, entity, "application/json", new JsonHttpResponseHandler() {
            private ProgressDialog pDialog;

            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                JSONObject jo;
                String id = null,
                        rules = null,
                        token = null,
                        username = null;
                try {
                    jo = response.getJSONObject(Constants.KEY_STATUS);
                    status = jo.getBoolean(Constants.KEY_SUCCESS);
                    jo = response.getJSONObject(Constants.KEY_OBJ);
                    id = jo.getString(Constants.KEY_ID);
                    rules = jo.getString(Constants.KEY_RULES);
                    flag = jo.getBoolean(Constants.KEY_FLAG);
                    token = jo.getString(Constants.KEY_TOKEN);
                    username = jo.getString(Constants.KEY_USERNAME);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.String responseString) {
                Log.d(TAG, "onSuccess: "+String.valueOf(statusCode));
                Log.d(TAG, "onSuccess: "+headers);
                Log.d(TAG, "onSuccess: "+responseString);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.Throwable throwable, org.json.JSONArray errorResponse){
                Log.d(TAG, "onSuccess: "+String.valueOf(statusCode));
                Log.d(TAG, "onSuccess: "+headers);
                Log.d(TAG, "onSuccess: "+throwable);
                Log.d(TAG, "onSuccess: "+errorResponse);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.String responseString, java.lang.Throwable throwable){
                Log.d(TAG, "onSuccess: "+String.valueOf(statusCode));
                Log.d(TAG, "onSuccess: "+headers);
                Log.d(TAG, "onSuccess: "+throwable);
                Log.d(TAG, "onSuccess: "+responseString);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }
}
