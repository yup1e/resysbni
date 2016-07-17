package com.sahaware.resysbni.view.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sahaware.resysbni.R;
import com.sahaware.resysbni.entity.DataGeneralInformation;
import com.sahaware.resysbni.entity.DataReport;
import com.sahaware.resysbni.entity.DataUser;
import com.sahaware.resysbni.entity.NasabahEntity;
import com.sahaware.resysbni.helper.DependencyInjection;
import com.sahaware.resysbni.repository.ISessionRepository;
import com.sahaware.resysbni.repository.ISqliteRepository;
import com.sahaware.resysbni.util.Constants;
import com.sahaware.resysbni.view.fragment.GIFragment;
import com.sahaware.resysbni.view.fragment.HomeFragment;
import com.sahaware.resysbni.view.fragment.InformationFragment;
import com.sahaware.resysbni.view.fragment.ListFragment;
import com.sahaware.resysbni.view.fragment.ProfileFragment;
import com.sahaware.resysbni.view.fragment.SubmitFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import dmax.dialog.SpotsDialog;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static FragmentManager fragmentManager;
    private int tabIcons[] = {R.drawable.home_icon_tab,
            R.drawable.list_icon_tab,
            R.drawable.submit_icon_tab,
            R.drawable.profile_icon_tab,
            R.drawable.info_icon_tab};
    private String TAG = "MainActivity";
    private SpotsDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog = new SpotsDialog(this, "Silahkan tunggu…");
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        getDataReport();
                        break;
                    case 1:
                        if(DependencyInjection.Get(ISqliteRepository.class).isDataNasabahEmpty())
                            getNasabah();

                        break;
                    case 3:
                        getDataUser();
                        break;
                    case 4:
                        DependencyInjection.Get(ISqliteRepository.class).clearInformation();
                        DependencyInjection.Get(ISqliteRepository.class).addInformation(new DataGeneralInformation("Jenis Pinjaman", "KUR", "BNI dapat memberikan pembiayaan kepada usaha anda yang feasible namu masih belum memiliki agunan sesuai persyaratan Bank."));
                        DependencyInjection.Get(ISqliteRepository.class).addInformation(new DataGeneralInformation("Jenis Pinjaman", "EDC", "Pembayaran customer lebih mudah. Customer tidak harus bawa uang Cash/pergi ke ATM dulu untuk ambil uang cash. Tinggal gesek kartu ATM (Debit) atau Kartu Kredit saja maka uang langsung masuk ke rekening pemilik/owner merchant."));
                        DependencyInjection.Get(ISqliteRepository.class).addInformation(new DataGeneralInformation("Jenis Pinjaman", "Laku Pandai", "Laku Pandai"));
                        break;
                }
            }
        });
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InformationFragment(), "Information");
        adapter.addFragment(new ListFragment(), "List");
        adapter.addFragment(new SubmitFragment(), "Submit");
        adapter.addFragment(new ProfileFragment(), "Profile");
        adapter.addFragment(new HomeFragment(), "Home");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    public void getDataUser() {
        final int DEFAULT_TIMEOUT = 20 * 1000;
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;
        try {
            jsonParams.put("id", DependencyInjection.Get(ISessionRepository.class).getId());
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
        client.post(MainActivity.this, Constants.API_GET_USER_DETAIL, entity, "application/json", new JsonHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                JSONObject dataUser = null, status;
                int point;
                try {
                    status = response.getJSONObject("status");
                    if (status.getInt("code") == 200) {
                        dataUser = response.getJSONObject("obj");
                    } else {
                        Toast.makeText(MainActivity.this, status.getString("description"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (dataUser != null) {
                    DependencyInjection.Get(ISqliteRepository.class).clearDetailUser();
                    try {
                        String alamat = dataUser.getString(Constants.KEY_ALAMAT);
                        String nip = dataUser.getString(Constants.KEY_NIP);
                        String nama = dataUser.getString(Constants.KEY_NAMA);
                        String noTelp = dataUser.getString(Constants.KEY_NO_TELP_USER);
                        String email = dataUser.getString(Constants.KEY_EMAIL);
                        if (dataUser.getString(Constants.KEY_POINT).equalsIgnoreCase(null)) {
                            point = 0;
                        }
                        point = dataUser.getInt(Constants.KEY_POINT);
                        String tglLahir = dataUser.getString(Constants.KEY_TGL_LAHIR);
                        String avatar = dataUser.getString(Constants.KEY_AVATAR);
                        String jmlNasabah = dataUser.getString(Constants.KEY_JUMLAH_NASABAH);

                        DependencyInjection.Get(ISqliteRepository.class).addDetailUser(new DataUser(Integer.parseInt(DependencyInjection.Get(ISessionRepository.class).getId()), nip, alamat, noTelp, email, nama, point, tglLahir, jmlNasabah, avatar));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Data tidak tersedia !", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.Throwable throwable, JSONObject errorResponse) {
                if(statusCode==0) {
                    Toast.makeText(MainActivity.this, "Gagal Terkoneksi ke server. (Internet Mati)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("No", null).show();
    }

    public void getNasabah() {
        final int DEFAULT_TIMEOUT = 20 * 1000;
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;
        int page = 1, pageSize = 20;
        try {
            jsonParams.put("idUser", DependencyInjection.Get(ISessionRepository.class).getId());
            jsonParams.put("page", page);
            jsonParams.put("pageSize", pageSize);
            jsonParams.put("roles", DependencyInjection.Get(ISessionRepository.class).getRules());
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
        client.post(MainActivity.this, Constants.API_GET_DATA_NASABAH, entity, "application/json", new JsonHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

                JSONObject dataNasabah, status;
                JSONArray listNasabah = null;

                try {
                    status = response.getJSONObject("status");
                    if (status.getInt("code") == 200) {
                        listNasabah = response.getJSONArray("listObj");
                    } else {
                        Toast.makeText(MainActivity.this, "Peringatan: Pengguna lain dengan nama pengguna yang sama ("+DependencyInjection.Get(ISessionRepository.class).getUsername()+") sudah login perangkat lain, silakan log out.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String img1 = null, img2 = null;
                if (listNasabah != null) {
                    DependencyInjection.Get(ISqliteRepository.class).clearNasabah();
                    for (int i = 0; i < listNasabah.length(); i++) {
                        try {
                            dataNasabah = listNasabah.getJSONObject(i);
                            String alamat = dataNasabah.getString("Alamat");
                            String anggunan = dataNasabah.getString("Anggunan");
                            Integer idNasabah = dataNasabah.getInt("IDNasabah");
                            String jumlahKredit = dataNasabah.getString("JmlhKredit");
                            String ktp = dataNasabah.getString("KTP");
                            String lamaUsaha = dataNasabah.getString("LamaUsaha");
                            Double lat = dataNasabah.getDouble("Lan");
                            Double lang = dataNasabah.getDouble("Lon");
                            String nama = dataNasabah.getString("Nama");
                            String noTelp = dataNasabah.getString("NoTlpn");
                            String sektorUsaha = dataNasabah.getString("SektorUsaha");
                            String namaKantor = dataNasabah.getString("namaKantor");
                            String jenis = dataNasabah.getString("namaReveral");
                            String namaStatus = dataNasabah.getString("namaStatus");
                            String tglSubmit = dataNasabah.getString("tglDibuat");
                            try {
                                JSONArray image = dataNasabah.getJSONArray("Image");
                                if (image.length() > 0) {
                                    for (int l = 0; l < image.length(); l++) {
                                        JSONObject dataImage = image.getJSONObject(l);
                                        img1 = dataImage.getString("image1");
                                        img2 = dataImage.getString("image2");
                                    }
                                }
                            } catch (JSONException e) {
                                img1 = null;
                                img2 = null;
                            }

                            DependencyInjection.Get(ISqliteRepository.class).addNasabah(new NasabahEntity(ktp, nama, alamat, noTelp, sektorUsaha, lamaUsaha, jenis, jumlahKredit, anggunan, namaKantor, tglSubmit, namaStatus, img1, img2, lat, lang, idNasabah));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                } else {
                    Toast.makeText(MainActivity.this, "Data tidak tersedia !", Toast.LENGTH_SHORT).show();
                }
                if (progressDialog!=null) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.Throwable throwable, JSONObject errorResponse) {
                Log.d("ListFragment", "onFailure: " + String.valueOf(statusCode));
                Log.d("ListFragment", "onFailure: " + headers);
                Log.d("ListFragment", "onFailure: " + throwable);
                Log.d("ListFragment", "onFailure: " + errorResponse);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public void getDataReport() {
        final int DEFAULT_TIMEOUT = 20 * 1000;
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;
        int page = 1, pageSize = 20;
        try {
            jsonParams.put("idUser", DependencyInjection.Get(ISessionRepository.class).getId());
            jsonParams.put("page", page);
            jsonParams.put("pageSize", pageSize);
            jsonParams.put("roles", DependencyInjection.Get(ISessionRepository.class).getRules());
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
        client.post(MainActivity.this, Constants.API_GET_REPORT, entity, "application/json", new JsonHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

                JSONObject dataReport, status;
                JSONArray listReport = null;

                try {
                    status = response.getJSONObject("status");
                    if (status.getInt("code") == 200) {
                        listReport = response.getJSONArray("listObj");
                    } else {
                        Toast.makeText(MainActivity.this, status.getString("description"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (listReport != null) {
                    DependencyInjection.Get(ISqliteRepository.class).clearReport();
                    for (int i = 0; i < listReport.length(); i++) {
                        try {
                            dataReport = listReport.getJSONObject(i);
                            String report = dataReport.getString("Report");
                            String jam = dataReport.getString("jam");
                            String tanggal = dataReport.getString("tanggal");
                            Integer id = dataReport.getInt("IDNasabah");

                            DependencyInjection.Get(ISqliteRepository.class).addReport(new DataReport(id, report, jam, tanggal));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Data tidak tersedia !", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.Throwable throwable, JSONObject errorResponse) {
                Log.d("ListFragment", "onFailure: " + String.valueOf(statusCode));
                Log.d("ListFragment", "onFailure: " + headers);
                Log.d("ListFragment", "onFailure: " + throwable);
                Log.d("ListFragment", "onFailure: " + errorResponse);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

}