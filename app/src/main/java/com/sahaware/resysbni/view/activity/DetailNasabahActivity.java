package com.sahaware.resysbni.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sahaware.resysbni.R;
import com.sahaware.resysbni.entity.NasabahEntity;
import com.sahaware.resysbni.helper.DependencyInjection;
import com.sahaware.resysbni.helper.MyApplication;
import com.sahaware.resysbni.repository.ISessionRepository;
import com.sahaware.resysbni.repository.ISqliteRepository;
import com.sahaware.resysbni.util.Constants;
import com.sahaware.resysbni.util.UtilityRupiahString;
import com.sahaware.resysbni.view.fragment.ProfileFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import dmax.dialog.SpotsDialog;

public class DetailNasabahActivity extends AppCompatActivity implements OnMapReadyCallback {
    @BindView(R.id.txt_detail_alamat)
    TextView txt_detail_alamat;
    @BindView(R.id.txt_detail_anggunan)
    TextView txt_detail_anggunan;
    @BindView(R.id.txt_detail_besar_pinjaman)
    TextView txt_detail_besar_pinjaman;
    @BindView(R.id.txt_detail_jenis_kredit)
    TextView txt_detail_jenis_kredit;
    @BindView(R.id.txt_detail_kantor)
    TextView txt_detail_kantor;
    @BindView(R.id.txt_detail_lama_usaha)
    TextView txt_detail_lama_usaha;
    @BindView(R.id.txt_detail_nama)
    TextView txt_detail_nama;
    @BindView(R.id.txt_detail_nama_user)
    TextView txt_detail_nama_user;
    @BindView(R.id.txt_detail_no_ktp)
    TextView txt_detail_no_ktp;
    @BindView(R.id.txt_detail_no_telp)
    TextView txt_detail_no_telp;
    @BindView(R.id.txt_detail_status)
    TextView txt_detail_status;
    @BindView(R.id.txt_detail_sektor_usaha)
    TextView txt_detail_sektor_usaha;
    @BindView(R.id.txt_detail_tanggal_submit)
    TextView txt_detail_tanggal_submit;
    @BindView(R.id.img_submit_location_1)
    ImageView img_submit_location_1;
    @BindView(R.id.img_submit_location_2)
    ImageView img_submit_location_2;
    /*
    @BindView(R.id.btn_detail_back)
    FButton btn_detail_back;
    @BindView(R.id.btn_detail_save)
    FButton btn_detail_save;*/
    private static final String TAG = "DetailNasabahActivity";
    private GoogleMap mMap;
    private String tanggal, nama, agunan, jumlah_kredit, alamat, no_hp, namaUser, status,
            lama_usaha, kantor, jenis_kredit, no_ktp, sektor_usaha, img1, img2, namaMarketing;
    private Integer idMarketing,idUser;
    private Double lat, lang;
    public UtilityRupiahString utilRupiah;
    private SpotsDialog progressDialog;
    private Activity Act;
    private Bundle bundle;
    private ArrayList<String> marketingList = new ArrayList<>();
    private ArrayList<Integer> marketingListKey = new ArrayList<>();


//    Class callerClass;
//    Activity obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Act = this;
        setContentView(R.layout.activity_detail_nasabah);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detail Nasabah");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
//        init();
//        implement();
//        try {
            // Loading map
//            initilizeMap();

//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        String theRoles = DependencyInjection.Get(ISessionRepository.class).getRules();
        if(MyApplication.isUkc(theRoles) || MyApplication.isRm(theRoles) || MyApplication.isCnm(theRoles)) {
            getAllMarketing();
//            showSpinnerMarketing(); //for debuging only
        }

        bundle = getIntent().getExtras();
        getDetailUser(bundle.getInt(Constants.KEY_ID));

    }

    private void initilizeMap() {
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initilizeMap();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    public void init() {
//        Bundle bundle;
//        bundle = getIntent().getExtras();
//        tanggal = bundle.getString(Constants.KEY_TANGGAL_SUBMIT);
//        nama = bundle.getString(Constants.KEY_NAMA);
//        agunan = bundle.getString(Constants.KEY_ANGGUNAN);
//        jumlah_kredit = bundle.getString(Constants.KEY_JUMLAH_KREDIT);
//        alamat = bundle.getString(Constants.KEY_ALAMAT);
//        no_hp = bundle.getString(Constants.KEY_NO_TELP);
//        namaUser = bundle.getString(Constants.KEY_NAMA_USER);
//        status = bundle.getString(Constants.KEY_NAMA_STATUS);
//        lama_usaha = bundle.getString(Constants.KEY_LAMA_USAHA);
//        kantor = bundle.getString(Constants.KEY_KANTOR);
//        jenis_kredit = bundle.getString(Constants.KEY_NAMA_REVERAL);
//        no_ktp = bundle.getString(Constants.KEY_KTP);
//        sektor_usaha = bundle.getString(Constants.KEY_SEKTOR_USAHA);
//        img1 = bundle.getString(Constants.KEY_IMAGE_1);
//        img2 = bundle.getString(Constants.KEY_IMAGE_2);
//        lat = bundle.getDouble(Constants.KEY_LAT);
//        lang = bundle.getDouble(Constants.KEY_LONG);
////        locUsaha = new LatLng(lat, lang);
//
//        if (img1 != null) {
//            Picasso.with(this)
//                    .load(Constants.API_IMAGE_NASABAH_URL + img1)
//                    .error(R.drawable.profile_user)
//                    .into(img_submit_location_1);
//        }
//        if (img2 != null) {
//            Picasso.with(this)
//                    .load(Constants.API_IMAGE_NASABAH_URL + img2)
//                    .error(R.drawable.profile_user)
//                    .into(img_submit_location_2);
//        }
//    }

    private void implement()    {
//        Log.e(TAG,"run implement()");
        txt_detail_alamat.setText(Html.fromHtml(Html.fromHtml(alamat).toString()));
        txt_detail_anggunan.setText(agunan);
        txt_detail_besar_pinjaman.setText(utilRupiah.formatRupiah(jumlah_kredit));
        txt_detail_jenis_kredit.setText(jenis_kredit);
        txt_detail_kantor.setText(kantor);
        txt_detail_lama_usaha.setText(lama_usaha);
        txt_detail_nama.setText(nama);
        txt_detail_nama_user.setText(namaUser);
        txt_detail_no_ktp.setText(no_ktp);
        txt_detail_no_telp.setText(no_hp);
        txt_detail_sektor_usaha.setText(sektor_usaha);
        txt_detail_tanggal_submit.setText(tanggal);
        txt_detail_status.setText(status);
        if (status != null) {
            if (status.equalsIgnoreCase("open")) {
                txt_detail_status.setText(status);
                txt_detail_status.setBackgroundResource(R.drawable.border_open);
            } else if (status.equalsIgnoreCase("on progres") || status.equalsIgnoreCase("survey")) {
                txt_detail_status.setText(status);
                txt_detail_status.setBackgroundResource(R.drawable.border_on_progress);
            } else if (status.equalsIgnoreCase("closed")) {
                txt_detail_status.setText(status);
                txt_detail_status.setBackgroundResource(R.drawable.border_closed);
            } else if (status.equalsIgnoreCase("approved")) {
                txt_detail_status.setText(status);
                txt_detail_status.setBackgroundResource(R.drawable.border_approved);
            } else if (status.equalsIgnoreCase("rejected")) {
                txt_detail_status.setText(status);
                txt_detail_status.setBackgroundResource(R.drawable.border_rejected);
            } else {
                txt_detail_status.setText(status);
            }
        }
        // define roles
        String theRoles = DependencyInjection.Get(ISessionRepository.class).getRules();
        Log.e(TAG,"Roles :"+theRoles);

        if((MyApplication.isMarketing(theRoles) || MyApplication.isSkc(theRoles) || MyApplication.isCnm(theRoles)) && status != null) {
            Boolean showSpinner=false;
            String[] statusList = new String[3];
            Integer[] statusListKey = new Integer[3];

            if (MyApplication.isMarketing(theRoles) && MyApplication.isSameId(idUser) &&
                    (status.equalsIgnoreCase("open") || status.equalsIgnoreCase("On Progres") || status.equalsIgnoreCase("Survey"))) {
                statusList = new String[]{"On Progres","Survey"};
                statusListKey = new Integer[]{5,6};
                showSpinner=true;
            }

            if ((MyApplication.isSkc(theRoles) || MyApplication.isCnm(theRoles)) &&
                    (status.equalsIgnoreCase("open") || status.equalsIgnoreCase("On Progres") || status.equalsIgnoreCase("Survey"))) {
                statusList = new String[]{"Approved","Reject-6 Bulan","Reject-Selamanya"};
                statusListKey = new Integer[]{2,12,13};
                showSpinner=true;
            }

            if(showSpinner){
                //Initializing spinner
                Spinner spinner = (Spinner) findViewById(R.id.spinner_status);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);

                //initialize button
                addListenerOnButton(statusListKey);

                // show table
                TableRow tabRow = (TableRow) findViewById(R.id.table_status);
                tabRow.setVisibility(View.VISIBLE);
                TableRow tabRow2 = (TableRow) findViewById(R.id.table_status_button);
                tabRow2.setVisibility(View.VISIBLE);
                TableRow tabRow3 = (TableRow) findViewById(R.id.table_status_line);
                tabRow3.setVisibility(View.VISIBLE);
            }
        }

//        if(MyApplication.isUkcRm(theRoles)) {
//            // get all marketing list
//            getAllMarketing();
//        }

    }

    public void addListenerOnButton(final Integer[] keyStatus) {
        final Spinner spinner1 = (Spinner) findViewById(R.id.spinner_status);
        Button btnSubmit = (Button) findViewById(R.id.button_status);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            final Integer idStatus = keyStatus[spinner1.getSelectedItemPosition()];
//            Log.e(TAG,"keyStatus "+keyStatus.toString());
//            Log.e(TAG,"spinner1position "+spinner1.getSelectedItemPosition());
//            Log.e(TAG,"spinner1select "+spinner1.getSelectedItem());

            AlertDialog.Builder builder = new AlertDialog.Builder(Act);
            builder.setTitle("Update status Nasabah");
            builder.setMessage("Yakin merubah status ?");
            builder.setIcon(R.drawable.ic_launcher);
            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                // do api update status
                updateStatusNasabah(bundle.getInt(Constants.KEY_ID),idStatus);
                }
            });
            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            }

        });
    }

    public void addListenerOnButtonDispos(final ArrayList<Integer> marketingKey) {
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner_dispos);
        Button btnSubmitDispos = (Button) findViewById(R.id.button_dispos);

        btnSubmitDispos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Integer marketingUser = marketingKey.get(spinner2.getSelectedItemPosition());
                Log.e(TAG,"keyMarketing "+marketingKey.toString());
                Log.e(TAG,"spinner1position "+spinner2.getSelectedItemPosition());
                Log.e(TAG,"spinner1select "+spinner2.getSelectedItem());
                AlertDialog.Builder builder = new AlertDialog.Builder(Act);
                builder.setTitle("Disposisi Nasabah");
                builder.setMessage("Yakin disposisikan nasabah ?");
                builder.setIcon(R.drawable.ic_launcher);
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        // do api update status
                        disposisiNasabah(bundle.getInt(Constants.KEY_ID),marketingUser);
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng locUsaha = new LatLng(-34, 151);
        LatLng locUsaha = new LatLng(lat, lang);
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.user))
                .position(locUsaha).title("Lokasi Usaha Nasabah"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locUsaha,15));
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
    }

    public void getDetailUser(final Integer idNasabah){
        final int DEFAULT_TIMEOUT = 20 * 1000;
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;

        try {
            jsonParams.put("idNasabah", idNasabah);
            jsonParams.put("token", DependencyInjection.Get(ISessionRepository.class).getToken());
            entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        progressDialog = new SpotsDialog(this, "Mencari data...");
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT);
        client.setConnectTimeout(DEFAULT_TIMEOUT);
        client.setResponseTimeout(DEFAULT_TIMEOUT);
        client.post(getApplicationContext(), Constants.API_GET_NASABAH_DETAIL, entity, "application/json", new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

                JSONObject jo, obj = null;
                Boolean statusResponse = false;
                Integer code = null;
//                Log.e(TAG,"API -> "+Constants.API_GET_NASABAH_DETAIL+": "+response.toString());

                if(!response.isNull("obj")) {
                    try {
                        obj = response.getJSONObject("obj");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    jo = response.getJSONObject(Constants.KEY_STATUS);
                    statusResponse = jo.getBoolean(Constants.KEY_SUCCESS);
                    code = jo.getInt("code");
                    if(MyApplication.validateOtherLogin(jo.getInt("code"),Act)) return;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (statusResponse){
                    if(obj!=null){
                        String img1, img2;
                        try {
                            tanggal = obj.getString(Constants.KEY_TANGGAL_SUBMIT);
                            nama = obj.getString(Constants.KEY_NAMA);
                            agunan = obj.getString(Constants.KEY_ANGGUNAN);
                            jumlah_kredit = obj.getString(Constants.KEY_JUMLAH_KREDIT);
                            alamat = obj.getString(Constants.KEY_ALAMAT);
                            no_hp = obj.getString(Constants.KEY_NO_TELP);
                            namaUser = obj.getString(Constants.KEY_NAMA_USER);
                            status = obj.getString(Constants.KEY_NAMA_STATUS);
                            lama_usaha = obj.getString(Constants.KEY_LAMA_USAHA);
                            kantor = obj.getString(Constants.KEY_KANTOR);
                            jenis_kredit = obj.getString(Constants.KEY_NAMA_REVERAL);
                            no_ktp = obj.getString(Constants.KEY_KTP);
                            sektor_usaha = obj.getString(Constants.KEY_SEKTOR_USAHA);
                            lat = obj.getDouble(Constants.KEY_LAT);
                            lang = obj.getDouble(Constants.KEY_LONG);
                            idMarketing = obj.getInt(Constants.KEY_ID_MARKETING);
                            idUser = obj.getInt(Constants.KEY_ID_USER);
//                          locUsaha = new LatLng(lat, lang);

                            try{
                                namaMarketing = obj.getString(Constants.KEY_NAMA_MARKETING);
                            } catch (JSONException e) {
                                namaMarketing = "";
                            }

                            try {
                                JSONObject image = obj.getJSONObject("Image");
                                img1 = image.getString(Constants.KEY_IMAGE_1);
                                img2 = image.getString(Constants.KEY_IMAGE_2);
                            } catch (JSONException e) {
                                img1 = null;
                                img2 = null;
                            }

                            if (img1 != null) {
                                Picasso.with(getApplicationContext())
                                        .load(Constants.API_IMAGE_NASABAH_URL + img1)
                                        .error(R.drawable.profile_user)
                                        .into(img_submit_location_1);
                            }
                            if (img2 != null) {
                                Picasso.with(getApplicationContext())
                                        .load(Constants.API_IMAGE_NASABAH_URL + img2)
                                        .error(R.drawable.profile_user)
                                        .into(img_submit_location_2);
                            }


                            implement();
                            initilizeMap();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG,e.toString());
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "ID belum terdaftar", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(code==401){
                        MainActivity.logout(getApplicationContext());
                    }
                    Toast.makeText(getApplicationContext(), "Terjadi kesalahan pada koneksi.", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.String responseString) {
                Log.d("ListFragment", "onSuccess: "+String.valueOf(statusCode));
                Log.d("ListFragment", "onSuccess: "+headers);
                Log.d("ListFragment", "onSuccess: "+responseString);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.Throwable throwable, org.json.JSONArray errorResponse){
                Log.d("ListFragment", "onSuccess: "+String.valueOf(statusCode));
                Log.d("ListFragment", "onSuccess: "+headers);
                Log.d("ListFragment", "onSuccess: "+throwable);
                Log.d("ListFragment", "onSuccess: "+errorResponse);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.String responseString, java.lang.Throwable throwable){
                Log.d("ListFragment", "onSuccess: "+String.valueOf(statusCode));
                Log.d("ListFragment", "onSuccess: "+headers);
                Log.d("ListFragment", "onSuccess: "+throwable);
                Log.d("ListFragment", "onSuccess: "+responseString);
                progressDialog.dismiss();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                progressDialog.dismiss();
            }
        });
    }

    public void updateStatusNasabah(final Integer idNasabah,Integer idStatus){
        final int DEFAULT_TIMEOUT = 20 * 1000;
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;

        try {
            jsonParams.put("Note", "Update status nasabah");
            jsonParams.put("idStatus", idStatus);
            jsonParams.put("idNasabah", idNasabah);
            jsonParams.put("idUser", DependencyInjection.Get(ISessionRepository.class).getId());
            jsonParams.put("token", DependencyInjection.Get(ISessionRepository.class).getToken());
            entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        progressDialog = new SpotsDialog(this, "Proses update status ...");
        progressDialog.show();

        Log.e(TAG,"Update status nasabah: "+Constants.API_UPDATE_STATUS_NASABAH);
        Log.e(TAG,"params: "+jsonParams.toString());

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT);
        client.setConnectTimeout(DEFAULT_TIMEOUT);
        client.setResponseTimeout(DEFAULT_TIMEOUT);
        client.post(getApplicationContext(), Constants.API_UPDATE_STATUS_NASABAH, entity, "application/json", new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

                JSONObject jo, obj = null;
                Boolean statusResponse = false;
                Integer code = null;
                Log.e(TAG,"response: "+response.toString());

                if(!response.isNull("obj")) {
                    try {
                        obj = response.getJSONObject("obj");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    jo = response.getJSONObject(Constants.KEY_STATUS);
                    statusResponse = jo.getBoolean(Constants.KEY_SUCCESS);
                    code = jo.getInt("code");
                    if(MyApplication.validateOtherLogin(jo.getInt("code"),Act)) return;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (statusResponse){
                    if(obj!=null){
                        Toast.makeText(getApplicationContext(), "Update status berhasil", Toast.LENGTH_SHORT).show();

                        //clear data for refresh table
                        DependencyInjection.Get(ISqliteRepository.class).clearNasabahDispos();
                        DependencyInjection.Get(ISqliteRepository.class).clearNasabah();

                        //go to home
                        Intent intent = new Intent(Act, MainActivity.class);
                        startActivity(intent);
                        Act.finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "Update status gagal.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(code==401){
                        MainActivity.logout(getApplicationContext());
                    }
                    Toast.makeText(getApplicationContext(), "Terjadi kesalahan pada koneksi.", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.String responseString) {
                Log.d("ListFragment", "onSuccess: "+String.valueOf(statusCode));
                Log.d("ListFragment", "onSuccess: "+headers);
                Log.d("ListFragment", "onSuccess: "+responseString);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.Throwable throwable, org.json.JSONArray errorResponse){
                Log.d("ListFragment", "onSuccess: "+String.valueOf(statusCode));
                Log.d("ListFragment", "onSuccess: "+headers);
                Log.d("ListFragment", "onSuccess: "+throwable);
                Log.d("ListFragment", "onSuccess: "+errorResponse);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.String responseString, java.lang.Throwable throwable){
                Log.d("ListFragment", "onSuccess: "+String.valueOf(statusCode));
                Log.d("ListFragment", "onSuccess: "+headers);
                Log.d("ListFragment", "onSuccess: "+throwable);
                Log.d("ListFragment", "onSuccess: "+responseString);
                progressDialog.dismiss();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                progressDialog.dismiss();
            }
        });
    }

    public void getAllMarketing(){
        Log.e(TAG,"run getAllmarketing()");
        final int DEFAULT_TIMEOUT = 20 * 1000;
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;

        try {
            jsonParams.put("idUser",DependencyInjection.Get(ISessionRepository.class).getId());
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
        client.post(getApplicationContext(), Constants.API_GET_MARKETING, entity, "application/json", new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                Log.e(TAG,"API : "+Constants.API_GET_MARKETING);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Log.e(TAG,"onSuccess : "+response.toString());
                JSONObject status;
                JSONObject dataMarketing;
                JSONArray listMarketing= null;

                try {
                    status = response.getJSONObject("status");
                    if(MyApplication.validateOtherLogin(status.getInt("code"),Act)) return;

                    if (status.getInt("code") == 200) {
                        listMarketing = response.getJSONArray("listObj");
                    }else{
                        Toast.makeText(Act, "Terjadi kesalahan koneksi", Toast.LENGTH_LONG).show();
                        Log.e(TAG,"StatusResponse :"+status.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (listMarketing != null) {
                    DependencyInjection.Get(ISqliteRepository.class).clearNasabah();
                    for (int i = 0; i < listMarketing.length(); i++) {
                        try {
                            dataMarketing = listMarketing.getJSONObject(i);
                            String nama = dataMarketing.getString("nama");
                            Integer idMarketing = dataMarketing.getInt("idUser");
                            String username = dataMarketing.getString("username");

                            // add to array
                            if(!nama.isEmpty())
                                marketingList.add(nama);
                            else
                                marketingList.add(username);
                            marketingListKey.add(idMarketing);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //Initializing spinner
                    showSpinnerMarketing();
                }
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.String responseString) {
                Log.e(TAG, "onSuccess: "+String.valueOf(statusCode));
                Log.e(TAG, "onSuccess: "+headers);
                Log.e(TAG, "onSuccess: "+responseString);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.Throwable throwable, org.json.JSONArray errorResponse){
                Log.e(TAG, "onFailure: "+String.valueOf(statusCode));
                Log.e(TAG, "onFailure: "+headers);
                Log.e(TAG, "onFailure: "+throwable);
                Log.e(TAG, "onFailure: "+errorResponse);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.String responseString, java.lang.Throwable throwable){
                Log.e(TAG, "onFailure2: "+String.valueOf(statusCode));
                Log.e(TAG, "onFailure2: "+headers);
                Log.e(TAG, "onFailure2: "+throwable);
                Log.e(TAG, "onFailure2: "+responseString);
            }

            @Override
            public void onRetry(int retryNo) {
                Log.e(TAG,"onRetry");
            }
        });
    }

    public void disposisiNasabah(final Integer idNasabah, Integer marketingUser){
        final int DEFAULT_TIMEOUT = 20 * 1000;
        final JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;

        try {
            jsonParams.put("idNasabah", idNasabah);
            jsonParams.put("idUser", marketingUser);
            jsonParams.put("token", DependencyInjection.Get(ISessionRepository.class).getToken());
            entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8"));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG,e.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(TAG,e.toString());
        }
        progressDialog = new SpotsDialog(this, "Proses disposisi nasabah ...");
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT);
        client.setConnectTimeout(DEFAULT_TIMEOUT);
        client.setResponseTimeout(DEFAULT_TIMEOUT);
        client.post(getApplicationContext(), Constants.API_DISPOS_NASABAH, entity, "application/json", new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                Log.e(TAG,"Goto API :"+Constants.API_DISPOS_NASABAH+" postvars: "+ jsonParams.toString());
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

                JSONObject jo, obj = null;
                Boolean statusResponse = false;
                Integer code = null;

                if(!response.isNull("obj")) {
                    try {
                        obj = response.getJSONObject("obj");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG,e.toString());
                    }
                }

                try {
                    jo = response.getJSONObject(Constants.KEY_STATUS);
                    statusResponse = jo.getBoolean(Constants.KEY_SUCCESS);
                    code = jo.getInt("code");
                    if(MyApplication.validateOtherLogin(jo.getInt("code"),Act)) return;

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG,e.toString());
                }

                if (statusResponse){
                    if(obj!=null){
                        Toast.makeText(getApplicationContext(), "Disposisi nasabah berhasil", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Act, MainActivity.class);
                        startActivity(intent);
                        Act.finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "Disposisi nasabah gagal.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(code==401){
                        MainActivity.logout(getApplicationContext());
                    }
                    Toast.makeText(getApplicationContext(), "Terjadi kesalahan pada koneksi.", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.String responseString) {
                Log.e(TAG, "onSuccess2: "+String.valueOf(statusCode));
                Log.e(TAG, "onSuccess2: "+headers);
                Log.e(TAG, "onSuccess2: "+responseString);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.Throwable throwable, org.json.JSONArray errorResponse){
                Log.e(TAG, "onFailure: "+String.valueOf(statusCode));
                Log.e(TAG, "onFailure: "+headers);
                Log.e(TAG, "onFailure: "+throwable);
                Log.e(TAG, "onFailure: "+errorResponse);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.String responseString, java.lang.Throwable throwable){
                Log.e(TAG, "onFailure2: "+String.valueOf(statusCode));
                Log.e(TAG, "onFailure2: "+headers);
                Log.e(TAG, "onFailure2: "+throwable);
                Log.e(TAG, "onFailure2: "+responseString);
                progressDialog.dismiss();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                progressDialog.dismiss();
            }
        });
    }

    private void showSpinnerMarketing(){
//        // add sample to array - for debuging only
//        marketingList.add("Roni");
//        marketingList.add("Zaki");
//        marketingListKey.add(1);
//        marketingListKey.add(2);

        //Initializing spinner
        Spinner spinnerMarketing = (Spinner) findViewById(R.id.spinner_dispos);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Act, android.R.layout.simple_spinner_item, marketingList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMarketing.setAdapter(dataAdapter);

        //initialize button
        addListenerOnButtonDispos(marketingListKey);

        // show table
        TableRow tabRow = (TableRow) findViewById(R.id.table_dispos);
        tabRow.setVisibility(View.VISIBLE);
        TableRow tabRow2 = (TableRow) findViewById(R.id.table_dispos_button);
        tabRow2.setVisibility(View.VISIBLE);
        TableRow tabRow3 = (TableRow) findViewById(R.id.table_dispos_line);
        tabRow3.setVisibility(View.VISIBLE);

    }
}
