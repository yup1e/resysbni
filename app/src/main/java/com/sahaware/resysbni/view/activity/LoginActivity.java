package com.sahaware.resysbni.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sahaware.resysbni.R;
import com.sahaware.resysbni.entity.DataJenisReveral;
import com.sahaware.resysbni.entity.DataKantor;
import com.sahaware.resysbni.entity.Datastatus;
import com.sahaware.resysbni.helper.DependencyInjection;
import com.sahaware.resysbni.repository.ICheckConnection;
import com.sahaware.resysbni.repository.ISessionRepository;
import com.sahaware.resysbni.repository.ISqliteRepository;
import com.sahaware.resysbni.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.ButterKnife;
import butterknife.BindView;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import dmax.dialog.SpotsDialog;
import info.hoang8f.widget.FButton;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.input_email)
    EditText loginEmailText;
    @BindView(R.id.input_password)
    EditText loginPasswordText;
    @BindView(R.id.btn_login)
    FButton loginButton;
    //@BindView(R.id.link_forgot_password)
    TextView forgotLink;
    private SpotsDialog progressDialog;
    final Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ButterKnife.bind(this);
        progressDialog = new SpotsDialog(this, "Silahkan tunggu…");
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressDialog.show();
                login();
            }
        });

        /*forgotLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), ForgotActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });*/
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        String email = loginEmailText.getText().toString();
        String password = loginPasswordText.getText().toString();
        Boolean isConnectedToInternet = DependencyInjection.Get(ICheckConnection.class).isConnectedToInternet();
        Boolean isConnectedToServer = DependencyInjection.Get(ICheckConnection.class).isConnectedToServer();
        if (isConnectedToInternet)
            if (isConnectedToServer)
                checkUser(email, bin2hex(getHash(password)));
            else {
                progressDialog.dismiss();
                loginButton.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Maaf, tidak dapat tergubung ke server.", Toast.LENGTH_SHORT).show();
            }
        else {
            progressDialog.dismiss();
            loginButton.setEnabled(true);
            Toast.makeText(LoginActivity.this, "Tidak dapat terhubung ke internet, silahkan periksa jarigan internet anda.", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(Boolean flag) {
        loginButton.setEnabled(true);
        progressDialog.dismiss();
        if (flag) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(getApplicationContext(), VerificationActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Silahkan masukan NPP atau Password dengan benar.", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = loginEmailText.getText().toString();
        String password = loginPasswordText.getText().toString();

        if (email.isEmpty() || email.length() < 4) {
            loginEmailText.setError("Masukkan NPP anda dengan Benar");
            valid = false;
        } else {
            loginEmailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 7 || password.length() > 20) {
            loginPasswordText.setError("Password antara 6 sampai 20 karakter");
            valid = false;
        } else {
            loginPasswordText.setError(null);
        }

        return valid;
    }

    public void getMasterData(final Boolean flag){
        final int DEFAULT_TIMEOUT = 20 * 1000;
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;
        try {
            jsonParams.put("token", DependencyInjection.Get(ISessionRepository.class).getToken());
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
        client.post(context, Constants.API_GET_MASTER_DATA, entity, "application/json", new JsonHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                JSONObject obj, status ;
                JSONArray dataStatus=null, dataKantor = null, dataJenisReveral = null;

                try {
                    status = response.getJSONObject("status");
                    if (status.getInt("code") == 200) {
                        obj = response.getJSONObject("obj");
                        if (obj != null) {
                            dataJenisReveral = obj.getJSONArray("DataJenisReveral");
                            dataKantor = obj.getJSONArray("DataKantor");
                            dataStatus = obj.getJSONArray("Datastatus");
                        } else {
                            Toast.makeText(LoginActivity.this, "Data tidak tersedia !", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, status.getString("description"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i =0; i<dataJenisReveral.length();i++){
                    try {
                        JSONObject object = dataJenisReveral.getJSONObject(i);
                        int idJenis = object.getInt("IDJenis");
                        String namaJenis = object.getString("Nama");
                        String keterangan = object.getString("Keterangan");
                        DependencyInjection.Get(ISqliteRepository.class).addJenisPinjaman(new DataJenisReveral(idJenis, namaJenis, keterangan));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                for (int j =0; j<dataKantor.length();j++){
                    try {
                        JSONObject object = dataKantor.getJSONObject(j);
                        int idKantor = object.getInt("IDKantor");
                        String lat = object.getString("Lan");
                        String lan = object.getString("Lon");
                        String namaKantor = object.getString("Nama");
                        String alamat = object.getString("Alamat");
                        String noTlpn = null;
                        if (object.getString("noTlpn")!=null){
                            noTlpn = object.getString("noTlpn");
                        }
                        JSONObject jenis = object.getJSONObject("jenisKantor");
                        String jenisKantor = jenis.getString("namaJenis");
                        DependencyInjection.Get(ISqliteRepository.class).addKantor(new DataKantor(alamat, jenisKantor, idKantor, namaKantor, lan, lat, noTlpn));
                        if (object.getJSONArray("kantorUKC") != null){
                            JSONArray ukc = object.getJSONArray("kantorUKC");
                            if (ukc.length()>0){
                                for (int k=0;k<ukc.length();k++){
                                    JSONObject dataUKC = ukc.getJSONObject(k);
                                    String alamatUKC = dataUKC.getString("Alamat");
                                    int idKantorUKC = dataUKC.getInt("IDKantor");
                                    String latUKC = dataUKC.getString("Lan");
                                    String lanUKC = dataUKC.getString("Lon");
                                    String namaKantorUKC = dataUKC.getString("Nama");
                                    String jenisKantorUKC = "UKC";
                                    String noTlpnUKC=null;
                                    if (object.getString("noTlpn")!=null){
                                        noTlpnUKC = object.getString("noTlpn");
                                    }
                                    DependencyInjection.Get(ISqliteRepository.class).addKantor(new DataKantor(alamatUKC, jenisKantorUKC, idKantorUKC, namaKantorUKC, lanUKC, latUKC, noTlpnUKC));
                                }

                            }
                        }
                        //DependencyInjection.Get(SqliteRepository.class).addJenisPinjaman(new MasterEntity(idJenis, namaJenis, keterangan));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                for (int m =0; m<dataStatus.length();m++){
                    try {
                        JSONObject object = dataStatus.getJSONObject(m);
                        int idStatus = object.getInt("IDStatus");
                        String namaStatus = object.getString("Nama");
                        String keteranganStatus = object.getString("Keterangan");
                        DependencyInjection.Get(ISqliteRepository.class).addJenisStatus(new Datastatus(idStatus, namaStatus, keteranganStatus));
                        JSONArray subStatus = object.getJSONArray("Substatus");
                        if (subStatus.length()>0){
                            for (int l=0;l<subStatus.length();l++){
                                JSONObject dataSubStatus = subStatus.getJSONObject(l);
                                int idSubStatus = dataSubStatus.getInt("IDStatus");
                                String namaSubStatus = dataSubStatus.getString("Nama");
                                String keteranganSubStatus = dataSubStatus.getString("Keterangan");
                                DependencyInjection.Get(ISqliteRepository.class).addJenisStatus(new Datastatus(idSubStatus, namaSubStatus, keteranganSubStatus));
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                onLoginSuccess(flag);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.Throwable throwable, JSONObject errorResponse){
                Log.d(TAG, "onSuccess: "+String.valueOf(statusCode));
                Log.d(TAG, "onSuccess: "+headers);
                Log.d(TAG, "onSuccess: "+throwable);
                Log.d(TAG, "onSuccess: "+errorResponse);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public byte[] getHash(String password) {
        MessageDigest digest=null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        digest.reset();
        return digest.digest(password.getBytes());
    }
    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
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

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                JSONObject jo;
                Boolean status = false, flag = false;
                String id = null,
                        rules = null,
                        token = null,
                        username = null;
                try {
                    jo = response.getJSONObject(Constants.KEY_STATUS);
                    status = jo.getBoolean(Constants.KEY_SUCCESS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if(status==true) {
                    try {
                        jo = response.getJSONObject(Constants.KEY_OBJ);
                        id = jo.getString(Constants.KEY_ID);
                        rules = jo.getString(Constants.KEY_RULES);
                        flag = jo.getBoolean(Constants.KEY_FLAG);
                        token = jo.getString(Constants.KEY_TOKEN);
                        username = jo.getString(Constants.KEY_USERNAME);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    DependencyInjection.Get(ISessionRepository.class).createLoginSession(id, username, token, rules);
                    getMasterData(flag);
                }else
                    onLoginFailed();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.Throwable throwable, JSONObject errorResponse){
                Log.d(TAG, "onSuccess: "+String.valueOf(statusCode));
                Log.d(TAG, "onSuccess: "+headers);
                Log.d(TAG, "onSuccess: "+throwable);
                Log.d(TAG, "onSuccess: "+errorResponse);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
}