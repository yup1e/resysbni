package com.sahaware.resysbni.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sahaware.resysbni.R;
import com.sahaware.resysbni.entity.DataReport;

import java.util.List;

public class ListReportAdapter extends RecyclerView.Adapter<ListReportAdapter.MyViewHolder> {

    private List<DataReport> reportList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView keterangan, waktu, id;

        public MyViewHolder(View view) {
            super(view);
            keterangan = (TextView) view.findViewById(R.id.keterangan_report);
            waktu = (TextView) view.findViewById(R.id.waktu_report);
            id = (TextView) view.findViewById(R.id.id_report);
        }
    }


    public ListReportAdapter(List<DataReport> reportList) {
        this.reportList = reportList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataReport report = reportList.get(position);
        holder.id.setText(String.valueOf(report.getID()));
        holder.keterangan.setText(report.getKeterangan());
        holder.waktu.setText(report.getTanggal() + " - " + report.getJam());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}