package com.sahaware.resysbni.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sahaware.resysbni.R;
import com.sahaware.resysbni.entity.NasabahEntity;

import java.util.List;

public class ListNasabahAdapter extends RecyclerView.Adapter<ListNasabahAdapter.MyViewHolder> {

    private List<NasabahEntity> nasabahList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama, tanggal, status, jenis;

        public MyViewHolder(View view) {
            super(view);
            nama = (TextView) view.findViewById(R.id.nama_nasabah);
            tanggal = (TextView) view.findViewById(R.id.waktu_submit);
            status = (TextView) view.findViewById(R.id.status);
            jenis = (TextView) view.findViewById(R.id.jenis_pinjaman);
        }
    }


    public ListNasabahAdapter(List<NasabahEntity> nasabahList) {
        this.nasabahList = nasabahList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_nasabah, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NasabahEntity nasabah = nasabahList.get(position);
        holder.nama.setText(nasabah.getNama());
        holder.tanggal.setText(nasabah.getTanggal());
        if(nasabah.getStatus()!=null) {
            if (nasabah.getStatus().equalsIgnoreCase("open")) {
                holder.status.setText(nasabah.getStatus());
                holder.status.setBackgroundResource(R.drawable.border_open);
            } else if (nasabah.getStatus().equalsIgnoreCase("on progress")) {
                holder.status.setText(nasabah.getStatus());
                holder.status.setBackgroundResource(R.drawable.border_on_progress);
            } else if (nasabah.getStatus().equalsIgnoreCase("closed")) {
                holder.status.setText(nasabah.getStatus());
                holder.status.setBackgroundResource(R.drawable.border_closed);
            } else if (nasabah.getStatus().equalsIgnoreCase("approved")) {
                holder.status.setText(nasabah.getStatus());
                holder.status.setBackgroundResource(R.drawable.border_approved);
            } else if (nasabah.getStatus().equalsIgnoreCase("rejected")) {
                holder.status.setText(nasabah.getStatus());
                holder.status.setBackgroundResource(R.drawable.border_rejected);
            } else {
                holder.status.setText(nasabah.getStatus());
            }
        }
        holder.jenis.setText(nasabah.getJenis_kredit());
    }

    @Override
    public int getItemCount() {
        return nasabahList.size();
    }
}