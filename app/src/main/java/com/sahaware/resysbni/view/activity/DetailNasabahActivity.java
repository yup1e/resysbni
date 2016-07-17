package com.sahaware.resysbni.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
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
import com.sahaware.resysbni.R;
import com.sahaware.resysbni.util.Constants;
import com.sahaware.resysbni.util.UtilityRupiahString;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailNasabahActivity extends AppCompatActivity {
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
    private GoogleMap mMap;
    private LatLng locUsaha;
    private MarkerOptions markerOptions;
    private String tanggal, nama, agunan, jumlah_kredit, alamat, no_hp, namaUser, status,
            lama_usaha, kantor, jenis_kredit, no_ktp, sektor_usaha, img1, img2;
    private String lat, lang;
    public UtilityRupiahString utilRupiah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_nasabah);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detail Nasabah");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        init();
        implement();
        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initilizeMap() {
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            MarkerOptions marker = new MarkerOptions().position(locUsaha).title("Hello Maps ");
            mMap.addMarker(new MarkerOptions()
                    .position(locUsaha)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.user))
                    .title("User"));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom((locUsaha), 15);
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((user), 45));
            mMap.animateCamera(cameraUpdate);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(false);
            // check if map is created successfully or not
            if (mMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
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

    /*@OnClick({R.id.btn_detail_back,
            R.id.btn_detail_save})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_detail_back:
                Intent intent = new Intent(DetailNasabahActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_detail_save:
                break;
        }
    }*/

    public void init(){
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        tanggal = bundle.getString(Constants.KEY_TANGGAL_SUBMIT);
        nama = bundle.getString(Constants.KEY_NAMA);
        agunan = bundle.getString(Constants.KEY_ANGGUNAN);
        jumlah_kredit = bundle.getString(Constants.KEY_JUMLAH_KREDIT);
        alamat = bundle.getString(Constants.KEY_ALAMAT);
        no_hp = bundle.getString(Constants.KEY_NO_TELP);
        namaUser = bundle.getString(Constants.KEY_NAMA_USER);
        status = bundle.getString(Constants.KEY_NAMA_STATUS);
        lama_usaha = bundle.getString(Constants.KEY_LAMA_USAHA);
        kantor = bundle.getString(Constants.KEY_KANTOR);
        jenis_kredit = bundle.getString(Constants.KEY_NAMA_REVERAL);
        no_ktp = bundle.getString(Constants.KEY_KTP);
        sektor_usaha = bundle.getString(Constants.KEY_SEKTOR_USAHA);
        img1 = bundle.getString(Constants.KEY_IMAGE_1);
        img2 = bundle.getString(Constants.KEY_IMAGE_2);
        lat = bundle.getString(Constants.KEY_LAT);
        lang = bundle.getString(Constants.KEY_LONG);
        locUsaha = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));

        if (!img1.isEmpty()) {
            Picasso.with(this)
                    .load(Constants.API_IMAGE_NASABAH_URL + img1)
                    .error(R.drawable.profile_user)
                    .into(img_submit_location_1);
        }
        if (!img2.isEmpty()) {
            Picasso.with(this)
                    .load(Constants.API_IMAGE_NASABAH_URL + img2)
                    .error(R.drawable.profile_user)
                    .into(img_submit_location_2);
        }
    }

    private void implement(){
        txt_detail_alamat.setText(Html.fromHtml(Html.fromHtml("&lt;p&gt;&lt;font color=&quot;red&quot; size=&quot;3&quot;&gt;34 hari yang lalu&lt;/font&gt;&lt;/p&gt;").toString()));
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
    }
}
