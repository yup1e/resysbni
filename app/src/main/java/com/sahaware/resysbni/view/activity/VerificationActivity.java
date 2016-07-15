package com.sahaware.resysbni.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sahaware.resysbni.R;
import com.sahaware.resysbni.entity.DataUser;
import com.sahaware.resysbni.helper.DependencyInjection;
import com.sahaware.resysbni.repository.ISessionRepository;
import com.sahaware.resysbni.repository.ISqliteRepository;
import com.sahaware.resysbni.util.Constants;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import dmax.dialog.SpotsDialog;
import info.hoang8f.widget.FButton;

public class VerificationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "VerificationActivity";
    @BindView(R.id.input_verify_email)
    public EditText verifyEmailText;
    @BindView(R.id.input_verify_address)
    public EditText verifyAddressText;
    @BindView(R.id.input_verify_full_name)
    public EditText verifyFullNameText;
    @BindView(R.id.input_verify_phone)
    public EditText verifyPhone;
    @BindView(R.id.input_verify_tanggal_lahir)
    public EditText verifyTanggalLahir;
    @BindView(R.id.btn_verify)
    public FButton verifyBtn;

    String  verifyEmailStr,
            verifyAddressStr,
            verifyFullNameStr,
            verifyPhoneStr,
            verifyTanggalLahirStr;
    private SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        progressDialog = new SpotsDialog(this, "Silahkan tunggu…");

        verifyTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                int day=1, month=0, year=1980;
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        VerificationActivity.this,
                        year,
                        month,
                        day
                );
                Log.d(TAG, "Date >>>>>>>>>>>>>>>>>>>: " + now.get(Calendar.YEAR) + " - " + now.get(Calendar.MONTH) + " " + now.get(Calendar.DAY_OF_MONTH));
                dpd.show(getFragmentManager(), "Datepickerdialog");
                dpd.setMaxDate(now);

            }
        });

    }

    @OnClick({ R.id.btn_verify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_verify:
                progressDialog.show();
                validate();
                saveDataVerification();
                break;
        }
    }

    public boolean validate() {
        boolean valid = true;
        verifyFullNameStr = verifyFullNameText.getText().toString();
        verifyAddressStr = verifyAddressText.getText().toString();
        verifyEmailStr = verifyEmailText.getText().toString();
        verifyPhoneStr = verifyPhone.getText().toString();
        verifyTanggalLahirStr = verifyTanggalLahir.getText().toString();




        if (verifyEmailStr.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(verifyEmailStr).matches()) {
            verifyEmailText.setError("Tolong masukkan alamt email dengan benar !");
            valid = false;
        } else {
            verifyEmailText.setError(null);
        }

        if (verifyAddressStr.isEmpty() || verifyAddressStr.length() < 4) {
            verifyAddressText.setError("Isi alamat dengan benar !");
            valid = false;
        } else {
            verifyAddressText.setError(null);
        }

        if (verifyFullNameStr.isEmpty() || verifyAddressStr.length() < 3) {
            verifyFullNameText.setError("Isi Nama Lengkap dengan benar!");
            valid = false;
        } else {
            verifyFullNameText.setError(null);
        }

        if (verifyPhoneStr.isEmpty() || verifyPhoneStr.length() < 11) {
            verifyPhone.setError("No. Telepon wajid di isi !");
            valid = false;
        } else {
            verifyPhone.setError(null);
        }

        return valid;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        int length = (int)(Math.log10(dayOfMonth)+1);
        String dayOfMonthstr = null, monthOfYearstr = null;
        if(length == 1) {
            dayOfMonthstr = String.valueOf("0" + dayOfMonth);
        }else{
            dayOfMonthstr = String.valueOf(dayOfMonth);
        }
        monthOfYear = monthOfYear + 1;
        length = (int)(Math.log10(monthOfYear)+1);
        if(length == 1) {
            monthOfYearstr = String.valueOf("0" + monthOfYear);
        }else{
            monthOfYearstr = String.valueOf(monthOfYear);
        }
        String date = dayOfMonthstr+"/"+monthOfYearstr+"/"+year;
        verifyTanggalLahir.setText(date);
    }

    public void saveDataVerification(){
        HashMap<String, String> user = DependencyInjection.Get(ISessionRepository.class).getUserDetails();
        final int DEFAULT_TIMEOUT = 20 * 1000;
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;
        try {
            jsonParams.put("alamat", verifyAddressStr);
            jsonParams.put("email", verifyEmailStr);
            jsonParams.put("id", user.get(Constants.KEY_ID));
            jsonParams.put("nama", verifyFullNameStr);
            jsonParams.put("nip", user.get(Constants.KEY_USERNAME));
            jsonParams.put("no_tlpn", verifyPhoneStr);
            jsonParams.put("tglLahir", verifyTanggalLahirStr);
            jsonParams.put("token", user.get(Constants.KEY_TOKEN));
            jsonParams.put("username", user.get(Constants.KEY_USERNAME));

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
        client.post(this, Constants.API_VERIFICATION, entity, "application/json", new JsonHttpResponseHandler() {


            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                progressDialog.hide();
                Log.d(TAG, "onSuccessObject: "+String.valueOf(statusCode));
                Log.d(TAG, "onSuccessObject: "+headers);
                Log.d(TAG, "onSuccessObject: "+response);
                JSONObject jo;
                Boolean status = false, flag = false;
                String nip = null,
                        alamat = null,
                        noTlpn = null,
                        email = null,
                        nama = null,
                        tglLahir = null;
                int point;
                try {
                    jo = response.getJSONObject(Constants.KEY_STATUS);
                    status = jo.getBoolean(Constants.KEY_SUCCESS);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if(status==true) {
                    try {
                        jo = response.getJSONObject(Constants.KEY_OBJ);
                        nip = jo.getString("NIP");
                        alamat = jo.getString("Alamat");
                        noTlpn = jo.getString("noTlpn");
                        email = jo.getString("Email");
                        nama = jo.getString("Nama");
                        if (jo.isNull("Point")) {
                            point = 0;
                        }else{
                            point = jo.getInt("Point");
                        }
                        tglLahir = jo.getString("TglLahir");
                        DependencyInjection.Get(ISqliteRepository.class).addDetailUser(new DataUser(Integer.parseInt(DependencyInjection.Get(ISessionRepository.class).getId()), nip, alamat, noTlpn, email, nama, point, tglLahir, "0", ""));
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(VerificationActivity.this, "data tidak ditemukan", Toast.LENGTH_SHORT).show();
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
