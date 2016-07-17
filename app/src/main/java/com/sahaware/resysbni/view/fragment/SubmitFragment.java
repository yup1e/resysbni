package com.sahaware.resysbni.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sahaware.resysbni.R;
import com.sahaware.resysbni.entity.DataJenisReveral;
import com.sahaware.resysbni.entity.DataKantor;
import com.sahaware.resysbni.entity.NasabahEntity;
import com.sahaware.resysbni.helper.DependencyInjection;
import com.sahaware.resysbni.repository.ISessionRepository;
import com.sahaware.resysbni.repository.ISqliteRepository;
import com.sahaware.resysbni.util.Constants;
import com.sahaware.resysbni.util.GPSTracker;
import com.sahaware.resysbni.util.UtilityImageByte;
import com.sahaware.resysbni.view.activity.DetailNasabahActivity;
import com.sahaware.resysbni.view.activity.MainActivity;
import com.sahaware.resysbni.view.adapter.ListJenisPinjamanAdapter;
import com.sahaware.resysbni.view.adapter.ListKantorAdapter;
import com.sahaware.resysbni.view.adapter.ListNasabahAdapter;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;
import dmax.dialog.SpotsDialog;
import info.hoang8f.widget.FButton;


public class SubmitFragment extends Fragment {


    @BindView(R.id.txt_submit_no_ktp)
    TextView txt_submit_no_ktp;
    @BindView(R.id.input_submit_nama)
    EditText edt_submit_nama;
    @BindView(R.id.input_submit_alamat)
    EditText edt_submit_alamat;
    @BindView(R.id.input_submit_nope)
    EditText edt_submit_nope;
    @BindView(R.id.input_submit_sektor_usaha)
    EditText edt_submit_sektor_usaha;
    @BindView(R.id.input_submit_lama_usaha)
    EditText edt_submit_lama_usaha;
    @BindView(R.id.input_submit_kredit_jumlah)
    EditText edt_submit_kredit_jumlah;
    @BindView(R.id.input_submit_kredit_agunan)
    EditText edt_submit_kredit_agunan;
    @BindView(R.id.spinner_kredit)
    MaterialBetterSpinner spn_jenis_kredit;
    @BindView(R.id.spinner_location)
    MaterialBetterSpinner spn_kantor;
    @BindView(R.id.img_submit_location_1)
    ImageView img_submit_location_1;
    @BindView(R.id.img_submit_location_2)
    ImageView img_submit_location_2;
    @BindView(R.id.btn_submit_data_nasabah)
    FButton btn_submit_data_nasabah;
    @BindView(R.id.TILNamaLengkap)
    TextInputLayout TILNamaLengkap;
    @BindView(R.id.txt_address_marker)
    TextView txt_address_marker;
    @BindView(R.id.txt_rekomendasi_kantor)
    TextView txt_rekomendasi_kantor;
    @BindView(R.id.rgJenis)
    RadioGroup rgJenis;
    @BindView(R.id.rbEDC)
    RadioButton rbEDC;
    @BindView(R.id.rbKUR)
    RadioButton rbKUR;
    @BindView(R.id.rbLakuPandai)
    RadioButton rbLakuPandai;
    @BindView(R.id.layoutJumlah)
    TextInputLayout layoutJumlah;
    @BindView(R.id.layoutAgunan)
    TextInputLayout layoutAgunan;
    private Unbinder unbinder;

    ImageView id_img;
    public static FragmentManager fragmentManager;
    String str_no_ktp,
            str_nama,
            str_alamat,
            str_no_hp,
            str_sektor_usaha,
            str_lama_usaha,
            str_jenis_kredit,
            str_jumlah_kredit,
            str_agunan,
            str_kantor,
            str_img1,
            str_img2;
    int MY_PERMISSIONS_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE;
    private FragmentActivity myContext;
    private GoogleMap mMap;
    private UtilityImageByte util;
    private SpotsDialog progressDialog, progressDialogSave;
    public GPSTracker gps;
    private RecyclerView recyclerView;
    private ListNasabahAdapter mAdapter;
    private Bitmap bitmap;
    private String selectedImagePath, namaKantor;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private String TAG = "Submit Referral";
    private double userLat = 0, userLng = 0, jarak;
    private MapView mMapView;
    private LatLng user, kantor, usaha;
    private DataKantor dataKantorTemp;
    List<DataKantor> listKantorRadius;
    List<DataKantor> listKantorTemp;
    List<DataKantor> listKantor;
    DataKantor dataKantorTerdekat;
    ListKantorAdapter adapterKantor;
    ListJenisPinjamanAdapter adapterJenis;
    List<DataJenisReveral> listJenis;
    double jarakTemp = 99999, jarakTerdekat = 99999;
    private MarkerOptions markerOptions;
    final HorizontalScrollView hsv;
    ScrollView mainScrollView;
    ImageView transparentImageView;
    int RadioBtnId;
    RadioButton r;
    int idNasabah;
    boolean statusSent = false, statusresult = false;
    public SubmitFragment() {
        listKantorRadius = new ArrayList<>();
        listJenis = new ArrayList<>();
        listKantor = new ArrayList<>();
        listKantorTemp = new ArrayList<>();
        hsv = null;
    }

    @Override
    public void onAttach(Activity act) {
        myContext = (FragmentActivity) act;
        super.onAttach(act);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_submit_data_nasabah, container, false);
        ButterKnife.bind(this, view);
        mainScrollView = (ScrollView) view.findViewById(R.id.main_scrollview);
        transparentImageView = (ImageView) view.findViewById(R.id.transparent_image);

        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            //Toast.makeText(getContext(),"You need have granted permission",Toast.LENGTH_SHORT).show();
            gps = new GPSTracker(getContext(), getActivity());

            // Check if GPS enabled
            if (gps.canGetLocation()) {

                userLat = gps.getLatitude();
                userLng = gps.getLongitude();

                // \n is for new line
                //Toast.makeText(getActivity().getApplicationContext(), "Your Location is - \nLat: " + userLat + "\nLong: " + userLng, Toast.LENGTH_LONG).show();
            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }
        }
        //===================================================================================================

        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        hsv.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        hsv.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return mMapView.onTouchEvent(event);
            }
        });
        mMap = mMapView.getMap();

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        user = new LatLng(userLat, userLng);
        usaha = user;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);

            if (listKantor != null) {
                for (DataKantor dataKantor : listKantor) {
                    double xDiff = userLat - Double.parseDouble(dataKantor.getLan());
                    double xSqr = Math.pow(xDiff, 2);

                    double yDiff = userLng - Double.parseDouble(dataKantor.getLon());
                    double ySqr = Math.pow(yDiff, 2);
                    //INI RUMUS EUCLIDIAN DISTANCE UNTUK MENENTUKAN JARAK
                    jarak = Math.sqrt(xSqr + ySqr);
                    jarak = jarak * 111.11;
                    dataKantor.setJarak(String.valueOf(new DecimalFormat("##").format(jarak)));
                    if (!dataKantor.getJenisKantor().equalsIgnoreCase("UMUM") && jarak <= 10){
                        //String jarakStr = String.valueOf(new DecimalFormat("##").format(jarak));
                        listKantorRadius.add(dataKantor);
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(dataKantor.getLan()), Double.parseDouble(dataKantor.getLon())))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.kantor))
                                .title(dataKantor.getNama())
                                .snippet(dataKantor.getAlamat()));
                        Circle circle = mMap.addCircle(new CircleOptions()
                                .center(user)
                                .radius(50 * 1000)
                                .strokeWidth(1)
                                .strokeColor(Color.RED)
                                .fillColor(Color.argb(10, 220, 237, 200)));
                        circle.setVisible(true);
                        //http://stackoverflow.com/questions/14326482/android-maps-v2-polygon-transparency
                    } else if (listKantorRadius.isEmpty() && dataKantor.getJenisKantor().equalsIgnoreCase("skc")) {
                        if (jarakTemp < jarak) {

                        } else {
                            jarakTemp = jarak;
                            dataKantorTemp = dataKantor;
                        }


                    }
                }
                if (dataKantorTemp != null) {
                    listKantorRadius.add(dataKantorTemp);
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(dataKantorTemp.getLan()), Double.parseDouble(dataKantorTemp.getLon())))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.kantor))
                            .title(dataKantorTemp.getNama())
                            .snippet(dataKantorTemp.getAlamat()));
                    Circle circle = mMap.addCircle(new CircleOptions()
                            .center(user)
                            .radius(50 * 1000)
                            .strokeWidth(1)
                            .strokeColor(Color.RED)
                            .fillColor(Color.argb(10, 220, 237, 200)));
                    circle.setVisible(true);
                }
            }

            mMap.addMarker(new MarkerOptions()
                        .position(user)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.user))
                        .title("User"));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom((user), 15);
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((user), 45));
            mMap.animateCamera(cameraUpdate);

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    double jarakTemp = 99999, jarakTerdekat = 99999;
                    usaha = latLng;
                    mMap.clear();
                    listKantorRadius.clear();
                    mMap.addMarker(new MarkerOptions()
                            .position(user)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.user))
                            .title("User"));
                    if (listKantor != null) {
                        for (DataKantor dataKantor : listKantor) {
                            double xDiff = usaha.latitude - Double.parseDouble(dataKantor.getLan());
                            double xSqr = Math.pow(xDiff, 2);

                            double yDiff = usaha.longitude - Double.parseDouble(dataKantor.getLon());
                            double ySqr = Math.pow(yDiff, 2);
                            //INI RUMUS EUCLIDIAN DISTANCE UNTUK MENENTUKAN JARAK
                            jarak = Math.sqrt(xSqr + ySqr);
                            jarak = jarak * 111.11;
                            Log.d(TAG, "jarak: >>>>>>>>>>>"+jarak + " - " + dataKantor.getNama());
                            if (jarakTerdekat < jarak) {

                            } else {
                                jarakTerdekat = jarak;
                                namaKantor = dataKantor.getNama();
                                dataKantorTerdekat = dataKantor;

                            }
                            if (!dataKantor.getJenisKantor().equalsIgnoreCase("UMUM") && jarak <= 10){
                                dataKantor.setJarak(String.valueOf(new DecimalFormat("##").format(jarak)));
                                //String jarakStr = String.valueOf(new DecimalFormat("##").format(jarak));
                                listKantorRadius.add(dataKantor);
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(dataKantor.getLan()), Double.parseDouble(dataKantor.getLon())))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.kantor))
                                        .title(dataKantor.getNama() + " " + String.valueOf(new DecimalFormat("##").format(jarak)) + " KM")
                                        .snippet(dataKantor.getAlamat()));
                                Circle circle = mMap.addCircle(new CircleOptions()
                                        .center(user)
                                        .radius(50 * 1000)
                                        .strokeWidth(1)
                                        .strokeColor(Color.RED)
                                        .fillColor(Color.argb(10, 220, 237, 200)));
                                circle.setVisible(true);
                                //http://stackoverflow.com/questions/14326482/android-maps-v2-polygon-transparency
                            } else if (listKantorRadius.isEmpty() && dataKantor.getJenisKantor().equalsIgnoreCase("skc")) {

                                if (jarakTemp < jarak) {


                                } else {
                                    jarakTemp = jarak;
                                    dataKantorTemp = dataKantor;
                                }


                            }
                        }

                        if (dataKantorTemp != null) {
                            listKantorRadius.add(dataKantorTemp);
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(dataKantorTemp.getLan()), Double.parseDouble(dataKantorTemp.getLon())))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.kantor))
                                    .title(dataKantorTemp.getNama() + " " + String.valueOf(new DecimalFormat("##").format(jarak)) + " KM")
                                    .snippet(dataKantorTemp.getAlamat()));
                            Circle circle = mMap.addCircle(new CircleOptions()
                                    .center(user)
                                    .radius(50 * 1000)
                                    .strokeWidth(1)
                                    .strokeColor(Color.RED)
                                    .fillColor(Color.argb(10, 220, 237, 200)));
                            circle.setVisible(true);
                        }
                    }
                    txt_rekomendasi_kantor.setText(namaKantor);
                    txt_rekomendasi_kantor.setTag(dataKantorTerdekat.getIDKantor());
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(dataKantorTerdekat.getLan()), Double.parseDouble(dataKantorTerdekat.getLon())))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.kantor))
                            .title(dataKantorTerdekat.getNama() + " " + String.valueOf(new DecimalFormat("##").format(jarak)) + " KM")
                            .snippet(dataKantorTerdekat.getAlamat()));
                    Circle circle = mMap.addCircle(new CircleOptions()
                            .center(user)
                            .radius(50 * 1000)
                            .strokeWidth(1)
                            .strokeColor(Color.RED)
                            .fillColor(Color.argb(10, 220, 237, 200)));
                    circle.setVisible(true);
                    markerOptions = new MarkerOptions();
                    // Setting the position for the marker
                    markerOptions.position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.nasabah));
                    // Placing a marker on the touched position
                    mMap.addMarker(markerOptions);
                    adapterKantor.notifyDataSetChanged();
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((latLng), 15));
                    //mMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);

                    // Adding Marker on the touched location with address
                    new ReverseGeocodingTask(getContext()).execute(latLng);

                }
            });
            /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng latLng) {
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Setting the position for the marker
                    markerOptions.position(latLng);

                    // Setting the title for the marker.
                    // This will be displayed on taping the marker
                    markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                    // Clears the previously touched position
                    mMap.clear();

                    // Animating to the touched position
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    // Placing a marker on the touched position
                    mMap.addMarker(markerOptions);
                }

            });*/

        }
        LinearLayout toolbar = (LinearLayout) view.findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mTitle.setText("HALAMAN UTAMA");
        progressDialog = new SpotsDialog(getActivity(), "Mencari data...");
        Resources res = getResources();


        adapterJenis = new ListJenisPinjamanAdapter(getActivity(), R.layout.spinner_rows, listJenis, res);
        spn_jenis_kredit.setAdapter(adapterJenis);


        adapterKantor = new ListKantorAdapter(getActivity(), R.layout.spinner_rows, listKantorRadius, res);
        spn_kantor.setAdapter(adapterKantor);

        AdapterView.OnItemClickListener listenerJenis = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String namaKantor = ((TextView) view.findViewById(R.id.nama)).getText().toString();
                String idKantor = ((TextView) view.findViewById(R.id.idKantor)).getText().toString();
                spn_jenis_kredit.setText(namaKantor);
                spn_jenis_kredit.setTag(idKantor);
                edt_submit_kredit_jumlah.setFocusable(true);
            }
        };
        AdapterView.OnItemClickListener listenerKantor = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String namaKantor = ((TextView) view.findViewById(R.id.nama)).getText().toString();
                String idKantor = ((TextView) view.findViewById(R.id.idKantor)).getText().toString();
                spn_kantor.setText(namaKantor);
                spn_kantor.setTag(idKantor);
                spn_kantor.setFocusable(false);
            }
        };

        spn_jenis_kredit.setOnItemClickListener(listenerJenis);
        spn_kantor.setOnItemClickListener(listenerKantor);
        util = new UtilityImageByte();

        rgJenis.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                    case R.id.rbKUR:
                        // TODO Something
                        layoutJumlah.setVisibility(View.VISIBLE);
                        layoutAgunan.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rbLakuPandai:
                        // TODO Something
                        layoutJumlah.setVisibility(View.GONE);
                        layoutAgunan.setVisibility(View.GONE);
                        break;
                    case R.id.rbEDC:
                        // TODO Something
                        layoutJumlah.setVisibility(View.GONE);
                        layoutAgunan.setVisibility(View.GONE);
                        break;
                }
            }
        });

        /*if (mMap == null) {
            mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
        }*/
        /*SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the

                    // contacts-related task you need to do.

                    gps = new GPSTracker(getContext(), getActivity());

                    // Check if GPS enabled
                    if (gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        // \n is for new line
                        //Toast.makeText(getActivity().getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        // Can't get location.
                        // GPS or network is not enabled.
                        // Ask user to enable GPS/network in settings.
                        gps.showSettingsAlert();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    Toast.makeText(getContext(), "You need to grant permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void initMap() {
        listKantor = DependencyInjection.Get(ISqliteRepository.class).getAllKantor();
        listJenis = DependencyInjection.Get(ISqliteRepository.class).getAllJenisPinjaman();
        if (adapterKantor != null) {
            adapterKantor.notifyDataSetChanged();
        }
        if (adapterJenis != null) {
            adapterJenis.notifyDataSetChanged();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            initMap();
            new MaterialDialog.Builder(getActivity())
                    .backgroundColor(Color.rgb(254, 253, 252))
                    .title("Cek KTP")
                    .content("Masukkan nomor KTP")
                    .inputType(InputType.TYPE_CLASS_NUMBER)
                    .inputRange(16, 16)
                    .positiveText("Submit")
                    .negativeText("Batal")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            myContext.finish();
                        }
                    })
                    .input("", null, false, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            progressDialog = new SpotsDialog(getActivity(), "Cek nomor KTP...");
                            String ktp = input.toString();
                            progressDialog.show();
                            CheckKTP(ktp);
                            /*new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "KTP belum terdaftar", Toast.LENGTH_SHORT).show();
                                }
                            }, SPLASH_TIME_OUT);*/


                        }
                    })
                    .cancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            myContext.finish();
                        }
                    })
                    .canceledOnTouchOutside(false)
                    .show();
        }
    }


    @OnClick({R.id.img_submit_location_1,
            R.id.img_submit_location_2,
            R.id.btn_submit_data_nasabah,
            R.id.rgJenis})
    public void onClick(View view) {
        Intent cameraIntent;
        switch (view.getId()) {
            case R.id.img_submit_location_1:
                if((ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        || (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))

                {
                    ActivityCompat.requestPermissions
                            (getActivity(), new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            },MY_PERMISSIONS_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE);
                }
                id_img = img_submit_location_1;
                id_img.setTag("img_submit_location_1");
                startDialog();
                break;
            case R.id.img_submit_location_2:
                if((ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        || (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))

                {
                    ActivityCompat.requestPermissions
                            (getActivity(), new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            },MY_PERMISSIONS_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE);
                }
                id_img = img_submit_location_2;
                id_img.setTag("img_submit_location_2");
                startDialog();
                break;
            case R.id.btn_submit_data_nasabah:
                btn_submit_data_nasabah.setEnabled(false);

                //new AddImageTask().execute();
                 InitValue();
                break;
        }
    }

    public void InitValue() {
        String sla = "0 Hari yang lalu";
        str_no_ktp = txt_submit_no_ktp.getText().toString();
        str_nama = edt_submit_nama.getText().toString();
        str_alamat = edt_submit_alamat.getText().toString();
        str_no_hp = edt_submit_nope.getText().toString();
        str_sektor_usaha = edt_submit_sektor_usaha.getText().toString();
        str_lama_usaha = edt_submit_lama_usaha.getText().toString();
        str_jenis_kredit = spn_jenis_kredit.getText().toString();
        str_jumlah_kredit = edt_submit_kredit_jumlah.getText().toString();
        //if (rgJenis.getCheckedRadioButtonId(R.id.rbKUR))
        str_agunan = edt_submit_kredit_agunan.getText().toString();
        str_kantor = txt_rekomendasi_kantor.getText().toString();
        RadioBtnId = rgJenis.getCheckedRadioButtonId();
        View radioButton = rgJenis.findViewById(RadioBtnId);
        int idx = rgJenis.indexOfChild(radioButton);
        r = (RadioButton)rgJenis.getChildAt(idx);
        if (img_submit_location_1.getTag().toString().equalsIgnoreCase("img"))
            str_img1 = null;
        else
            str_img1 = img_submit_location_1.getTag().toString();

        if (img_submit_location_2.getTag().toString().equalsIgnoreCase("img"))
            str_img2 = null;
        else
            str_img2 = img_submit_location_2.getTag().toString();

        if (str_nama.equals(null)) {
            edt_submit_nama.setError("Nama wajib di isi !");
        }else if (str_alamat.equals(null)) {
            edt_submit_alamat.setError("Alamat wajib di isi !");
        }else if (str_no_hp.equals(null)) {
            edt_submit_nope.setError("No. HP wajib di isi !");
        }else if (str_sektor_usaha.equals(null)) {
            edt_submit_sektor_usaha.setError("Sektor usaha wajib di isi !");
        }else if (str_lama_usaha.equals(null)) {
            edt_submit_lama_usaha.setError("Lama usaha wajib di isi !");
        }else if (rgJenis.getCheckedRadioButtonId()<=0) {
            rbKUR.setError("Jenis Kredit wajib d isi !");
        }/*else if (str_jumlah_kredit.equals(null)) {
            edt_submit_kredit_jumlah.setError("Jumlah kredit wajib di isi !");
        }else if (str_agunan.equals(null)) {
            edt_submit_kredit_agunan.setError("Agunan wajib di isi !");
        }*/else {
            Calendar c = Calendar.getInstance();
            Double latUsaha = 0.0;
            Double langUsaha = 0.0;
            if (!Double.isNaN(usaha.latitude)){
                latUsaha = usaha.latitude;
            }
            if (!Double.isNaN(usaha.longitude)){
                langUsaha = usaha.longitude;
            }
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c.getTime());
            NasabahEntity nb = new NasabahEntity(str_no_ktp, str_nama, str_alamat, str_no_hp, str_sektor_usaha,
                    str_lama_usaha, r.getTag().toString() , str_jumlah_kredit, str_agunan,
                    str_kantor, formattedDate
                    , "Open", str_img1, str_img2, latUsaha, langUsaha, sla);
            DependencyInjection.Get(ISqliteRepository.class).addNasabahTemp(nb);

            SaveDataNasabah();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       /* if (mMap != null) {
            MainActivity.fragmentManager.beginTransaction()
                    .remove(MainActivity.fragmentManager.findFragmentById(R.id.map)).commit();
            mMap = null;
        }*/
        //unbinder.unbind();
    }

    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = null;

                        pictureActionIntent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(
                                pictureActionIntent,
                                GALLERY_PICTURE);

                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,
                                CAMERA_REQUEST);

                    }
                });
        myAlertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        bitmap = null;
        selectedImagePath = null;

        if (resultCode == getActivity().RESULT_OK && requestCode == CAMERA_REQUEST) {
            String uri = Environment.getExternalStorageDirectory() + "/Android/data/" + getActivity().getPackageName() + "/images/";
            File f = new File(uri);
            if (!f.exists()) {
                f.mkdirs();
            }

            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                Bitmap compressedphoto = util.getResizedBitmap(photo, 400);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                compressedphoto.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                try {
                    File file = new File(id_img.getTag() + ".jpeg");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileOutputStream fo = new FileOutputStream(file);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                selectedImagePath = uri + id_img.getTag() + ".jpeg";
                Log.d(">>>>>>>>>>>>>>>>>", "onActivityResult: " + selectedImagePath);

                id_img.setImageBitmap(compressedphoto);
            }

        } else if (resultCode == getActivity().RESULT_OK && requestCode == GALLERY_PICTURE) {
            if (data != null) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                selectedImagePath = c.getString(columnIndex);
                c.close();
                File f= new File(selectedImagePath);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                try {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                /*bitmap = BitmapFactory.decodeFile(selectedImagePath); // load
                // preview image
                bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);*/

                Log.d(">>>>>>>>>>>>>>>>>", "onActivityResult: " + selectedImagePath);

                id_img.setImageBitmap(bitmap);

            } else {
                Toast.makeText(getActivity(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }
        if (id_img.getTag().toString().equalsIgnoreCase("img_submit_location_1"))
            img_submit_location_1.setTag(selectedImagePath);
        else
            img_submit_location_2.setTag(selectedImagePath);
    }

    public void SaveDataNasabah() {
        final SpotsDialog progressDialogSave = new SpotsDialog(getActivity(), "Menyimpan Data...");
        progressDialogSave.show();
        final int DEFAULT_TIMEOUT = 20 * 1000;
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;
        try {
            jsonParams.put("Alamat", str_alamat);
            jsonParams.put("Anggunan", str_agunan);
            jsonParams.put("IDKantor", txt_rekomendasi_kantor.getTag());
            jsonParams.put("IDReveral", r.getTag());
            jsonParams.put("IDUser", DependencyInjection.Get(ISessionRepository.class).getId());
            jsonParams.put("JmlhKredit", Double.parseDouble(str_jumlah_kredit));
            jsonParams.put("KTP", txt_submit_no_ktp.getText().toString());
            jsonParams.put("LamaUsaha", str_lama_usaha);
            jsonParams.put("Lan", String.valueOf(usaha.latitude));
            jsonParams.put("Lon", String.valueOf(usaha.longitude));
            jsonParams.put("Nama", str_nama);
            jsonParams.put("NoTlpn", str_no_hp);
            jsonParams.put("SektorUsaha", str_sektor_usaha);
            jsonParams.put("token", DependencyInjection.Get(ISessionRepository.class).getToken());
            entity = new StringEntity(jsonParams.toString());
            Log.d(TAG, "SaveDataNasabah: " + jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT);
        client.setConnectTimeout(DEFAULT_TIMEOUT);
        client.setResponseTimeout(DEFAULT_TIMEOUT);
        client.post(getActivity(), Constants.API_SAVE_DATA_NASABAH, entity, "application/json", new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                //progressDialogSave.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

                JSONObject jo, obj;
                Boolean status = false;
                try {
                    jo = response.getJSONObject(Constants.KEY_STATUS);
                    status = jo.getBoolean(Constants.KEY_SUCCESS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (status) {
                    try {
                        obj = response.getJSONObject("obj");
                        idNasabah = obj.getInt("IDNasabah");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialogSave.dismiss();
                    new AddImageTask().execute();
                    /*DependencyInjection.Get(ISqliteRepository.class).clearNasabah();
                    DependencyInjection.Get(ISqliteRepository.class).clearNasabahTemp();
                    Toast.makeText(getActivity(), "Data Berhasil Disimpan.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();*/
                } else {
                    progressDialogSave.dismiss();
                    Toast.makeText(getActivity(), "Terjadi kesalahan pada server.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.Throwable throwable, org.json.JSONArray errorResponse) {
                progressDialogSave.dismiss();
                Toast.makeText(getActivity(), "Code " + String.valueOf(statusCode) + "\n Response " + errorResponse, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + String.valueOf(statusCode));
                Log.d(TAG, "onFailure: " + headers);
                Log.d(TAG, "onFailure: " + throwable);
                Log.d(TAG, "onFailure: " + errorResponse);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.String responseString, java.lang.Throwable throwable) {
                progressDialogSave.dismiss();
                Toast.makeText(getActivity(), "Code " + String.valueOf(statusCode) + "\n Response " + responseString, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + String.valueOf(statusCode));
                Log.d(TAG, "onFailure: " + headers);
                Log.d(TAG, "onFailure: " + throwable);
                Log.d(TAG, "onFailure: " + responseString);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public void CheckKTP(final String ktp) {

        progressDialog.show();
        final int DEFAULT_TIMEOUT = 20 * 1000;
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;
        try {

            jsonParams.put("ktp", ktp);
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
        client.post(getActivity(), Constants.API_CHECK_KTP, entity, "application/json", new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                if(progressDialog!=null)
                    progressDialog.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

                JSONObject jo, obj = null;
                Boolean status = false;
                try {
                    try {
                        obj = response.getJSONObject("obj");
                    } catch (JSONException e) {
                        obj = null;
                    }
                    jo = response.getJSONObject(Constants.KEY_STATUS);
                    status = jo.getBoolean(Constants.KEY_SUCCESS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (status) {
                    if (obj != null) {
                        progressDialog.dismiss();
                        try {
                            final String[] img1 = new String[1];
                            final String[] img2 = new String[1];
                            final JSONObject finalObj = obj;
                            new MaterialDialog.Builder(getActivity())
                                    .backgroundColor(Color.rgb(254, 253, 252))
                                    .title("Data Tersedia")
                                    .content("Nama : " + obj.getString(Constants.KEY_NAMA) + "\n" +
                                            "Status Pinjaman : " + obj.getString(Constants.KEY_NAMA_STATUS))
                                    .positiveText("Detail")
                                    .negativeText("Batal")
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(MaterialDialog dialog, DialogAction which) {
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            startActivity(intent);
                                            myContext.finish();
                                        }
                                    })
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            Intent intent = new Intent(getActivity(), DetailNasabahActivity.class);
                                            Bundle bundle = new Bundle();
                                            try {
                                                bundle.putString(Constants.KEY_TANGGAL_SUBMIT, finalObj.getString(Constants.KEY_TANGGAL_SUBMIT));
                                                bundle.putString(Constants.KEY_NAMA_REVERAL, finalObj.getString(Constants.KEY_NAMA_REVERAL));
                                                bundle.putString(Constants.KEY_ANGGUNAN, finalObj.getString(Constants.KEY_ANGGUNAN));
                                                bundle.putDouble(Constants.KEY_LAT, finalObj.getDouble(Constants.KEY_LAT));
                                                bundle.putString(Constants.KEY_JUMLAH_KREDIT, finalObj.getString(Constants.KEY_JUMLAH_KREDIT));
                                                bundle.putString(Constants.KEY_ALAMAT, finalObj.getString(Constants.KEY_ALAMAT));
                                                bundle.putString(Constants.KEY_NO_TELP, finalObj.getString(Constants.KEY_NO_TELP));
                                                bundle.putString(Constants.KEY_NAMA_USER, finalObj.getString(Constants.KEY_NAMA_USER));
                                                bundle.putString(Constants.KEY_NAMA_STATUS, finalObj.getString(Constants.KEY_NAMA_STATUS));
                                                bundle.putString(Constants.KEY_LAMA_USAHA, finalObj.getString(Constants.KEY_LAMA_USAHA));
                                                bundle.putString(Constants.KEY_KANTOR, finalObj.getString(Constants.KEY_KANTOR));
                                                bundle.putString(Constants.KEY_NAMA, finalObj.getString(Constants.KEY_NAMA));
                                                bundle.putDouble(Constants.KEY_LONG, finalObj.getDouble(Constants.KEY_LONG));
                                                bundle.putString(Constants.KEY_KTP, finalObj.getString(Constants.KEY_KTP));
                                                bundle.putString(Constants.KEY_SEKTOR_USAHA, finalObj.getString(Constants.KEY_SEKTOR_USAHA));
                                                try {
                                                    JSONObject image = finalObj.getJSONObject("Image");

                                                    img1[0] = image.getString(finalObj.getString(Constants.KEY_IMAGE_1));
                                                    img2[0] = image.getString(finalObj.getString(Constants.KEY_IMAGE_2));

                                                } catch (JSONException e) {
                                                    img1[0] = null;
                                                    img2[0] = null;
                                                }
                                                bundle.putString(Constants.KEY_IMAGE_1, img1[0]);
                                                bundle.putString(Constants.KEY_IMAGE_2, img2[0]);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    })
                                    .cancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            startActivity(intent);
                                            myContext.finish();
                                        }
                                    })
                                    .canceledOnTouchOutside(false)
                                    .show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        progressDialog.dismiss();
                        txt_submit_no_ktp.setText(ktp);
                        Toast.makeText(getActivity(), "KTP belum terdaftar", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Terjadi kesalahan koneksi, silahkan coba lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.String responseString) {
                Log.d(TAG, "onSuccess: " + String.valueOf(statusCode));
                Log.d(TAG, "onSuccess: " + headers);
                Log.d(TAG, "onSuccess: " + responseString);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.Throwable throwable, org.json.JSONArray errorResponse) {
                progressDialog.dismiss();
                Log.d(TAG, "onSuccess: " + String.valueOf(statusCode));
                Log.d(TAG, "onSuccess: " + headers);
                Log.d(TAG, "onSuccess: " + throwable);
                Log.d(TAG, "onSuccess: " + errorResponse);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.String responseString, java.lang.Throwable throwable) {

                progressDialog.dismiss();
                Log.d(TAG, "onSuccess: " + String.valueOf(statusCode));
                Log.d(TAG, "onSuccess: " + headers);
                Log.d(TAG, "onSuccess: " + throwable);
                Log.d(TAG, "onSuccess: " + responseString);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String> {
        Context mContext;

        public ReverseGeocodingTask(Context context){
            super();
            mContext = context;
        }

        // Finding address using reverse geocoding
        @Override
        protected String doInBackground(LatLng... params) {
            Geocoder geocoder = new Geocoder(mContext);
            double latitude = params[0].latitude;
            double longitude = params[0].longitude;

            List<Address> addresses = null;
            String addressText="";

            try {
                addresses = geocoder.getFromLocation(latitude, longitude,1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(addresses != null && addresses.size() > 0 ){
                Address address = addresses.get(0);

                addressText = String.format("%s, %s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getLocality(),
                        address.getCountryName());
            }

            return addressText;
        }

        @Override
        protected void onPostExecute(String addressText) {
            // Setting the title for the marker.
            // This will be displayed on taping the marker
            markerOptions.title(addressText);

            // Placing a marker on the touched position
            mMap.addMarker(markerOptions);
            txt_address_marker.setText(addressText);

        }
    }


    public  String httpPostFile(byte[] file, String url) throws Exception {
        System.out.println("Start Connection " + url);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("Content-Type", "application/octet-stream");

        httppost.setEntity(new ByteArrayEntity(file));

        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        String responseText = EntityUtils.toString(entity);

        System.out.println("Fetch Data");
        System.out.println(responseText);

        return responseText;
    }

    private class AddImageTask extends AsyncTask<Void, Void, Void> {
        //  ProgressDialog dialog;
        public String dataResult = "";
        public Bitmap bPhoto;
        //String poiId = "";
        File file ;

        public AddImageTask()
        {
            if (!statusSent) {
                file = new File(img_submit_location_1.getTag().toString());
            }else {
                file = new File(img_submit_location_2.getTag().toString());
            }
        }
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
           /* dialog = ProgressDialog.show(activity, "", "Insert Image", true,
                    false);
            dialog.show();*/
            progressDialog.show();
        }
        // Referensi : http://stackoverflow.com/questions/17388240/posting-image-file-to-wcf-rest-service-from-android
        /*
        Erorr dan Bug :
        1. Upload Avatar via galerry bisa
        2. Upload Avatar Via Camera bisa tapi masih ada BUG (harus 2x tekan tombol upload)

        3. Upload Gambar Nasabah via Gallery --> Blom Bisa
        4. Upload Gambar Nasabah via Camera --> Blum Bisa
         */


        @Override
        protected Void doInBackground(Void... params) {

            try {
                byte[] streamByteArray;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                try {
                    FileInputStream fis = new FileInputStream(this.file);
                    bPhoto = BitmapFactory.decodeStream(fis);
                } catch (Exception e) { }
                if (bPhoto != null) {
                    bPhoto.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    bPhoto.recycle();
                    streamByteArray = stream.toByteArray();
                    stream.close();
                    stream = null;
                    String token = DependencyInjection.Get(ISessionRepository.class).getToken();
                    dataResult = httpPostFile(
                            streamByteArray,
                            Constants.API_SAVE_IMAGE_NASABAH + token + "/" + idNasabah);
                    //"http://bni.yapyek.com/servicesreveral/upload_image_nasabah/"+token+"/"+id);
                    //"http://bni.yapyek.com/servicesreveral/upload_image_avatar/"+token+"/"+id);
                    Log.d("response",dataResult);
                    JSONObject dataUser = null, status, result=null;
                    result = new JSONObject(dataResult);
                    int point;
                    try {
                        status = result.getJSONObject("status");
                        if (status.getInt("code") == 200) {
                            dataUser = result.getJSONObject("obj");
                        } else {
                            Toast.makeText(getActivity(), status.getString("description"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (dataUser != null) {
                        try {
                            String image = dataUser.getString("image");
                            if (statusSent){
                                statusresult = true;
                            }
                            statusSent = true;

                            //DependencyInjection.Get(ISqliteRepository.class).updateAvatarUser(image, DependencyInjection.Get(ISessionRepository.class).getUsername());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Data tidak tersedia !", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());

            }
            Log.d(TAG, "onSuccessObject: sukses mereun");

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if (dataResult != "") {
                try {
                    JSONObject jo = new JSONObject(dataResult);
                    JSONObject status = jo.getJSONObject("status");

                    Log.d(TAG, "onSuccessObject: startup onPostExecute");
                    Log.d(TAG, "Data Response Code  =" + status.getInt("code"));
                } catch (Exception e) {

                }
            } else
            {
                Log.d(TAG, "Dataresult: kosong");

            }
            progressDialog.dismiss();
            if (!statusresult){
                new AddImageTask().execute();
            }else if (statusresult){
                btn_submit_data_nasabah.setEnabled(true);
                DependencyInjection.Get(ISqliteRepository.class).clearNasabah();
                DependencyInjection.Get(ISqliteRepository.class).clearNasabahTemp();
                Toast.makeText(getActivity(), "Data Berhasil Disimpan.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }
    }
}

//Testgithub