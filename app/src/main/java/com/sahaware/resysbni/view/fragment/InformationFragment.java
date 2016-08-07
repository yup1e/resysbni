package com.sahaware.resysbni.view.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sahaware.resysbni.R;
import com.sahaware.resysbni.entity.DataGeneralInformation;
import com.sahaware.resysbni.entity.NasabahEntity;
import com.sahaware.resysbni.helper.DependencyInjection;
import com.sahaware.resysbni.repository.ISessionRepository;
import com.sahaware.resysbni.repository.ISqliteRepository;
import com.sahaware.resysbni.util.Constants;
import com.sahaware.resysbni.view.activity.MainActivity;
import com.sahaware.resysbni.view.adapter.CustomTextSliderView;
import com.sahaware.resysbni.view.adapter.ListInformationAdapter;
import com.sahaware.resysbni.view.custom.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import dmax.dialog.SpotsDialog;


public class InformationFragment extends Fragment implements BaseSliderView.OnSliderClickListener {
    private SliderLayout sliderShow;
    private HashMap<String, String> listImage;
    private List<DataGeneralInformation> dataInformation;
    private ListInformationAdapter mAdapter;
    //private SpotsDialog progressDialog;
    private RecyclerView mRecyclerView;

    public InformationFragment() {
        dataInformation = new ArrayList<>();
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        LinearLayout toolbar = (LinearLayout) view.findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Halaman Utama");
        sliderShow = (SliderLayout) view.findViewById(R.id.slider);
        //progressDialog = new SpotsDialog(getContext(), "Mencari data...");
        entryListSlider();
        loadSlider(view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mAdapter = new ListInformationAdapter(dataInformation);
        mRecyclerView.setAdapter(mAdapter);
        //This is the code to provide a sectioned grid

        return view;
    }

   /* @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            if(DependencyInjection.Get(ISqliteRepository.class).isDataReportEmpty()){
                //progressDialog.show();
                initData();
            }else {
                populateSampleData();
            }


        }
    }*/

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            if (DependencyInjection.Get(ISqliteRepository.class).isGeneralInformationEmpty()) {
                //progressDialog.show();
                getGeneralInformation();
            } else
                showDB();
        }
    }

    public void showDB() {
        List<DataGeneralInformation> listInformation = DependencyInjection.Get(ISqliteRepository.class).getAllInformation();
        dataInformation.clear();
        if (listInformation != null) {
            for (DataGeneralInformation general : listInformation) {
                dataInformation.add(general);
            }
        }

        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    public void getGeneralInformation() {
        final int DEFAULT_TIMEOUT = 20 * 1000;
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;
        int page = 1, pageSize = 10;
        try {
            jsonParams.put("page", page);
            jsonParams.put("pageSize", pageSize);
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
        client.post(getContext(), Constants.API_GET_ALL_GENERAL_INFORMATION, entity, "application/json", new JsonHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                JSONObject data, status;
                JSONArray listInformation = null;

                try {
                    status = response.getJSONObject("status");
                    if (status.getInt("code") == 200)
                        listInformation = response.getJSONArray("listObj");
                    else
                        Toast.makeText(getContext(), status.getString("description"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (listInformation != null) {
                    DependencyInjection.Get(ISqliteRepository.class).clearReport();
                    for (int i = 0; i < listInformation.length(); i++) {
                        try {
                            data = listInformation.getJSONObject(i);
                            String deskripsi = data.getString("deskripsi");
                            Integer id = data.getInt("id");
                            Integer idJenis = data.getInt("idJenisGeneral");
                            String image = data.getString("image");
                            String judul = data.getString("judul");
                            String namaJenis = data.getString("namaJenisGeneral");
                            Integer orderby = data.getInt("orderby");

                            DependencyInjection.Get(ISqliteRepository.class).addInformation(new DataGeneralInformation(namaJenis, judul, deskripsi));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else
                    Toast.makeText(getContext(), "Data tidak tersedia !", Toast.LENGTH_SHORT).show();
/*
                if (progressDialog!=null)
                    progressDialog.dismiss();*/

                showDB();
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

    public void entryListSlider() {
        listImage = new HashMap<String, String>();
        listImage.put("Bus Rejeki BNI Mudik", "http://bni.referralsystems.net/images/banner/resysbni-baner-1.jpg");
        listImage.put("Special Promo Ramadhan", "http://bni.referralsystems.net/images/banner/resysbni-baner-2.jpg");
        listImage.put("BNI e-billing", "http://bni.referralsystems.net/images/banner/resysbni-baner-3.jpg");
        listImage.put("BNI Digital Loan", "http://bni.referralsystems.net/images/banner/resysbni-baner-4.jpg");
        listImage.put("BNI Grand Prize", "http://bni.referralsystems.net/images/banner/resysbni-baner-5.jpg");
    }

    public void loadSlider(View view) {
        for (String name : listImage.keySet()) {
            CustomTextSliderView sliderView = new CustomTextSliderView(getActivity());
            sliderView.description(name).image(listImage.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(this);

            sliderView.bundle(new Bundle());
            sliderView.getBundle().putString("title", name);

            sliderShow.addSlider(sliderView);
            sliderShow.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
            sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderShow.setCustomAnimation(new DescriptionAnimation());
            sliderShow.setCustomIndicator((PagerIndicator) view.findViewById(R.id.custom_indicator));
            sliderShow.startAutoCycle();
        }
    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        sliderShow.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getActivity(), slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //unbinder.unbind();
    }

  /*  private void populateSampleData() {
        List<DataGeneralInformation> listInformation = DependencyInjection.Get(ISqliteRepository.class).getAllInformation();
        dataInformation.clear();
        if (listInformation != null) {
            for (DataGeneralInformation information : listInformation) {
                dataInformation.add(information);
            }
        }
        if(mAdapter!= null) {
            mAdapter.notifyDataSetChanged();
        }
    }
*/
    /*private void initData(){
        DependencyInjection.Get(ISqliteRepository.class).clearInformation();
        *//*DependencyInjection.Get(ISqliteRepository.class).addInformation(new DataGeneralInformation("Jenis Pinjaman", "Kedit Usaha Rakyat", "BNI dapat memberikan pembiayaan kepada usaha anda yang feasible namu masih belum memiliki agunan sesuai persyaratan Bank.  Solusinya adalah dengan Kredit Usaha Rakyat yang dapat diberikan kepada calon debitur Usaha Mikro, Kecil, Menengah, Anggota keluarga dari karyawan/karyawati yang berpenghasilan tetap atau bekerja sebagai Tenaga Kerja Indonesia (TKI) dan TKI yang purna dari bekerja di luar negeri."));
        DependencyInjection.Get(ISqliteRepository.class).addInformation(new DataGeneralInformation("Jenis Pinjaman", "EDC", "Bisnis Merchant merupakan salah satu aktivitas usaha yang dilakukan oleh Bank dalam upaya memberikan layanan transaksi perbankan kepada nasabahnya dengan cara memasang atau menempatkan EDC dan/atau Imprinter di tempat usaha Merchant. Dalam Bisnis Merchant ini Bank bertindak sebagai Acquiring dari VISA dan MasterCard yang dapat menerima dan memproses Transaksi yang dilakukan dengan menggunakan Kartu Kredit ataupun Kartu Debit."));
        DependencyInjection.Get(ISqliteRepository.class).addInformation(new DataGeneralInformation("Jenis Pinjaman", "Laku Pandai", "Perorangan atau badan hukum yang telah bekerjasama dengan BNI untuk menjadi kepanjangan tangan BNI dalam menyediakan layanan perbankan kepada masyarakat dalam rangka pemerataan layanan perbankan berupa produk tabungan, kredit mikro, asuransi mikro, uang elektronik, pembelian pulsa/voucher dan pembayaran tagihan."));*//*
        populateSampleData();
    }
*/

}