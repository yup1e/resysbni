package com.sahaware.resysbni.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.sahaware.resysbni.R;
import com.sahaware.resysbni.entity.DataGeneralInformation;
import com.sahaware.resysbni.helper.DependencyInjection;
import com.sahaware.resysbni.repository.ISqliteRepository;
import com.sahaware.resysbni.view.activity.MainActivity;
import com.sahaware.resysbni.view.adapter.CustomTextSliderView;
import com.sahaware.resysbni.view.adapter.ListInformationAdapter;
import com.sahaware.resysbni.view.custom.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InformationFragment extends Fragment implements BaseSliderView.OnSliderClickListener{
    private SliderLayout sliderShow;
    private MainActivity mainActivity;
    private HashMap<String, String> listImage;
    private List<DataGeneralInformation> dataInformation;
    private ListInformationAdapter mAdapter;
    private RecyclerView mRecyclerView;
    public InformationFragment() {
        dataInformation = new ArrayList<>();
        // Required empty public constructor
    }
    private final String[] array = {"Hello", "World", "Android", "is", "Awesome", "World", "Android", "is", "Awesome", "World", "Android", "is", "Awesome", "World", "Android", "is", "Awesome"};

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
        mTitle.setText("Information");
        sliderShow = (SliderLayout) view.findViewById(R.id.slider);
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

    @Override
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
    }

    public void entryListSlider() {
        listImage = new HashMap<String, String>();
        listImage.put("Bus Rejeki BNI Mudik", "http://bni.yapyek.com/images/banner/resysbni-baner-1.jpg");
        listImage.put("Special Promo Ramadhan", "http://bni.yapyek.com/images/banner/resysbni-baner-2.jpg");
        listImage.put("BNI e-billing", "http://bni.yapyek.com/images/banner/resysbni-baner-3.jpg");
        listImage.put("BNI Digital Loan", "http://bni.yapyek.com/images/banner/resysbni-baner-4.jpg");
        listImage.put("BNI Grand Prize", "http://bni.yapyek.com/images/banner/resysbni-baner-5.jpg");
    }

    public void loadSlider(View view) {
        for (String name : listImage.keySet()) {
            CustomTextSliderView sliderView = new CustomTextSliderView(getActivity());
            sliderView.description(name).image(listImage.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(this);

            sliderView.bundle(new Bundle());
            sliderView.getBundle().putString("title",name);

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
        Toast.makeText(getActivity(),slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();

        //unbinder.unbind();
    }

    private void populateSampleData() {

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

    private void initData(){
        DependencyInjection.Get(ISqliteRepository.class).clearInformation();
        DependencyInjection.Get(ISqliteRepository.class).addInformation(new DataGeneralInformation("Jenis Pinjaman", "KUR", "BNI dapat memberikan pembiayaan kepada usaha anda yang feasible namu masih belum memiliki agunan sesuai persyaratan Bank."));
        DependencyInjection.Get(ISqliteRepository.class).addInformation(new DataGeneralInformation("Jenis Pinjaman", "EDC", "Pembayaran customer lebih mudah. Customer tidak harus bawa uang Cash/pergi ke ATM dulu untuk ambil uang cash. Tinggal gesek kartu ATM (Debit) atau Kartu Kredit saja maka uang langsung masuk ke rekening pemilik/owner merchant."));
        DependencyInjection.Get(ISqliteRepository.class).addInformation(new DataGeneralInformation("Jenis Pinjaman", "Laku Pandai", "Laku Pandai"));
        populateSampleData();
    }


}