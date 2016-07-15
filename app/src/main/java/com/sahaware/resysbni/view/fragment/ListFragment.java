package com.sahaware.resysbni.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sahaware.resysbni.R;
import com.sahaware.resysbni.entity.NasabahEntity;
import com.sahaware.resysbni.helper.DependencyInjection;
import com.sahaware.resysbni.repository.ISessionRepository;
import com.sahaware.resysbni.repository.ISqliteRepository;
import com.sahaware.resysbni.util.Constants;
import com.sahaware.resysbni.view.activity.DetailNasabahActivity;
import com.sahaware.resysbni.view.adapter.ListNasabahAdapter;
import com.sahaware.resysbni.view.custom.DividerItemDecoration;

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


public class ListFragment extends Fragment {

    private static final String TAG = "ListFragment";
    private FragmentActivity myContext;
    private RecyclerView recyclerView;
    private ListNasabahAdapter mAdapter;
    private List<NasabahEntity> nasabahList;
    private SpotsDialog progressDialog;

    public ListFragment() {
        nasabahList = new ArrayList<>();
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
        View view = inflater.inflate(R.layout.fragment_list
                , container, false);
        LinearLayout toolbar = (LinearLayout) view.findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("LIST NASABAH");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        progressDialog = new SpotsDialog(getActivity(), "Mencari data...");
        mAdapter = new ListNasabahAdapter(nasabahList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                progressDialog.show();
                NasabahEntity nasabah = nasabahList.get(position);
                getDetailUser(nasabah.getIdNasabah());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            if(DependencyInjection.Get(ISqliteRepository.class).isDataNasabahEmpty())
            {
                progressDialog.show();
                getDataUser();
            }else {
                showDB();
            }
        }
    }

    public void showDB(){
        List<NasabahEntity> nasabahs = DependencyInjection.Get(ISqliteRepository.class).getAllNasabah();
        List<NasabahEntity> nasabahTemp = DependencyInjection.Get(ISqliteRepository.class).getAllNasabahTemp();
        nasabahList.clear();
        if (nasabahs != null) {
            for (NasabahEntity nasabah : nasabahs) {
                nasabahList.add(nasabah);
            }
        }
        if (nasabahTemp != null) {
            for (NasabahEntity dataNasabahTemp : nasabahTemp) {
                nasabahList.add(dataNasabahTemp);
            }
        }
        if(mAdapter!= null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void getDataUser() {
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
        client.post(getActivity(), Constants.API_GET_DATA_NASABAH, entity, "application/json", new JsonHttpResponseHandler() {

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
                        Toast.makeText(getActivity(), status.getString("description"), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "Data tidak tersedia !", Toast.LENGTH_SHORT).show();
                }
                if (progressDialog!=null) {
                    progressDialog.dismiss();
                }
                showDB();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity(), "Hubungan ke server gagal (Code: "+statusCode+")", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ListFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ListFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }



        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
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
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT);
        client.setConnectTimeout(DEFAULT_TIMEOUT);
        client.setResponseTimeout(DEFAULT_TIMEOUT);
        client.post(getActivity(), Constants.API_GET_NASABAH_DETAIL, entity, "application/json", new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

                JSONObject jo = null, obj = null;
                Boolean status = false;
                String code = null, description = null;
                try {
                    try {
                        obj = response.getJSONObject("obj");
                    }catch (JSONException e){
                        obj = null;
                    }
                    jo = response.getJSONObject(Constants.KEY_STATUS);
                    status = jo.getBoolean(Constants.KEY_SUCCESS);
                    code = String.valueOf(jo.getInt("code"));
                    description = jo.getString("description");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (status){
                    if(obj!=null){
                        String img1, img2;
                        Intent intent = new Intent(getActivity(), DetailNasabahActivity.class);
                        Bundle bundle = new Bundle();
                        try {
                            bundle.putString(Constants.KEY_TANGGAL_SUBMIT, obj.getString(Constants.KEY_TANGGAL_SUBMIT));
                            bundle.putString(Constants.KEY_NAMA_REVERAL, obj.getString(Constants.KEY_NAMA_REVERAL));
                            bundle.putString(Constants.KEY_ANGGUNAN, obj.getString(Constants.KEY_ANGGUNAN));
                            bundle.putString(Constants.KEY_LAT, obj.getString(Constants.KEY_LAT));
                            bundle.putString(Constants.KEY_JUMLAH_KREDIT, obj.getString(Constants.KEY_JUMLAH_KREDIT));
                            bundle.putString(Constants.KEY_ALAMAT, obj.getString(Constants.KEY_ALAMAT));
                            bundle.putString(Constants.KEY_NO_TELP, obj.getString(Constants.KEY_NO_TELP));
                            bundle.putString(Constants.KEY_NAMA_USER, obj.getString(Constants.KEY_NAMA_USER));
                            bundle.putString(Constants.KEY_NAMA_STATUS, obj.getString(Constants.KEY_NAMA_STATUS));
                            bundle.putString(Constants.KEY_LAMA_USAHA, obj.getString(Constants.KEY_LAMA_USAHA));
                            bundle.putString(Constants.KEY_KANTOR, obj.getString(Constants.KEY_KANTOR));
                            bundle.putString(Constants.KEY_NAMA, obj.getString(Constants.KEY_NAMA));
                            bundle.putString(Constants.KEY_LONG, obj.getString(Constants.KEY_LONG));
                            bundle.putString(Constants.KEY_KTP, obj.getString(Constants.KEY_KTP));
                            bundle.putString(Constants.KEY_SEKTOR_USAHA, obj.getString(Constants.KEY_SEKTOR_USAHA));
                            try {
                                JSONObject image = obj.getJSONObject("Image");

                                img1 = image.getString(obj.getString(Constants.KEY_IMAGE_1));
                                img2 = image.getString(obj.getString(Constants.KEY_IMAGE_2));

                            } catch (JSONException e) {
                                img1 = null;
                                img2 = null;
                            }
                            bundle.putString(Constants.KEY_IMAGE_1,img1);
                            bundle.putString(Constants.KEY_IMAGE_2,img2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getActivity(), "ID belum terdaftar", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (code.equalsIgnoreCase("400")){
                        Toast.makeText(getActivity(), "Peringatan: Pengguna lain dengan nama pengguna yang sama ("+DependencyInjection.Get(ISessionRepository.class).getUsername()+") sudah login perangkat lain, silakan log out.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), "Error Connection (Code : "+code+")", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Error: "+description);
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.Throwable throwable, org.json.JSONArray errorResponse){
                Log.d("ListFragment", "onSuccess: "+String.valueOf(statusCode));
                Log.d("ListFragment", "onSuccess: "+headers);
                Log.d("ListFragment", "onSuccess: "+throwable);
                Log.d("ListFragment", "onSuccess: "+errorResponse);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.String responseString, java.lang.Throwable throwable){
                Log.d("ListFragment", "onSuccess: "+String.valueOf(statusCode));
                Log.d("ListFragment", "onSuccess: "+headers);
                Log.d("ListFragment", "onSuccess: "+throwable);
                Log.d("ListFragment", "onSuccess: "+responseString);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
}