package com.sahaware.resysbni.view.fragment;

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
import com.sahaware.resysbni.entity.DataReport;
import com.sahaware.resysbni.helper.DependencyInjection;
import com.sahaware.resysbni.repository.ISessionRepository;
import com.sahaware.resysbni.repository.ISqliteRepository;
import com.sahaware.resysbni.util.Constants;
import com.sahaware.resysbni.view.activity.DetailNasabahActivity;
import com.sahaware.resysbni.view.adapter.ListReportAdapter;

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


public class HomeFragment extends Fragment {
    private FragmentActivity myContext;
    private RecyclerView recyclerView;
    private ListReportAdapter mAdapter;
    private List<DataReport> reportList;
    private SpotsDialog progressDialog;

    public HomeFragment() {
       reportList = new ArrayList<>();
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
        View view = inflater.inflate(R.layout.fragment_home
                , container, false);
        LinearLayout toolbar = (LinearLayout) view.findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("AKTIFITAS REFERRAL TERBARU");
        progressDialog = new SpotsDialog(getActivity(), "Mencari data...");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mAdapter = new ListReportAdapter(reportList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                DataReport report = reportList.get(position);
                progressDialog.show();
                getDetailUser(report.getID());
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
            if(DependencyInjection.Get(ISqliteRepository.class).isDataReportEmpty()){
                progressDialog = new SpotsDialog(getActivity(), "Mencari data...");
                //progressDialog.show();
                getDataReport();
            }else {
                showDB();
            }


        }
    }

    public void showDB(){
        List<DataReport> listReport = DependencyInjection.Get(ISqliteRepository.class).getAllReport();
        reportList.clear();
        if (listReport != null) {
            for (DataReport report : listReport) {
                reportList.add(report);
            }
        }
        if(mAdapter!= null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private HomeFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final HomeFragment.ClickListener clickListener) {
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
        client.post(getActivity(), Constants.API_GET_REPORT, entity, "application/json", new JsonHttpResponseHandler() {

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
                        Toast.makeText(getActivity(), status.getString("description"), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "Data tidak tersedia !", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                showDB();

            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
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

                JSONObject jo, obj = null;
                Boolean status = false;
                try {
                    try {
                        obj = response.getJSONObject("obj");
                    }catch (JSONException e){
                        obj = null;
                    }
                    jo = response.getJSONObject(Constants.KEY_STATUS);
                    status = jo.getBoolean(Constants.KEY_SUCCESS);
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
                            bundle.putDouble(Constants.KEY_LAT, obj.getDouble(Constants.KEY_LAT));
                            bundle.putString(Constants.KEY_JUMLAH_KREDIT, obj.getString(Constants.KEY_JUMLAH_KREDIT));
                            bundle.putString(Constants.KEY_ALAMAT, obj.getString(Constants.KEY_ALAMAT));
                            bundle.putString(Constants.KEY_NO_TELP, obj.getString(Constants.KEY_NO_TELP));
                            bundle.putString(Constants.KEY_NAMA_USER, obj.getString(Constants.KEY_NAMA_USER));
                            bundle.putString(Constants.KEY_NAMA_STATUS, obj.getString(Constants.KEY_NAMA_STATUS));
                            bundle.putString(Constants.KEY_LAMA_USAHA, obj.getString(Constants.KEY_LAMA_USAHA));
                            bundle.putString(Constants.KEY_KANTOR, obj.getString(Constants.KEY_KANTOR));
                            bundle.putString(Constants.KEY_NAMA, obj.getString(Constants.KEY_NAMA));
                            bundle.putDouble(Constants.KEY_LONG, obj.getDouble(Constants.KEY_LONG));
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
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getActivity(), "ID belum terdaftar", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Terjadi kesalahan pada koneksi.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.String responseString) {
                Log.d("ListFragment", "onSuccess: "+String.valueOf(statusCode));
                Log.d("ListFragment", "onSuccess: "+headers);
                Log.d("ListFragment", "onSuccess: "+responseString);
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