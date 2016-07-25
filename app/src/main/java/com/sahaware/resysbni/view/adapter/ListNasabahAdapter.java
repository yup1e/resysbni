package com.sahaware.resysbni.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahaware.resysbni.R;
import com.sahaware.resysbni.entity.NasabahEntity;
import com.sahaware.resysbni.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListNasabahAdapter extends RecyclerView.Adapter<ListNasabahAdapter.MyViewHolder> {

    private List<NasabahEntity> nasabahList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama, tanggal, status, jenis, no_hp, no_ktp, sla;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            nama = (TextView) view.findViewById(R.id.nama_nasabah);
            tanggal = (TextView) view.findViewById(R.id.waktu_submit);
            status = (TextView) view.findViewById(R.id.status);
            jenis = (TextView) view.findViewById(R.id.jenis_pinjaman);
            no_hp = (TextView) view.findViewById(R.id.telp_nasabah);
            no_ktp = (TextView) view.findViewById(R.id.ktp_nasabah);
            sla = (TextView) view.findViewById(R.id.sla_nasabah);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }


    public ListNasabahAdapter(List<NasabahEntity> nasabahList, Context context) {
        this.nasabahList = nasabahList;
        this.context = context;
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
        holder.no_hp.setText(nasabah.getNo_hp());
        holder.no_ktp.setText(nasabah.getNo_ktp());
        if (nasabah.getSla() != null) {
            holder.sla.setText(Html.fromHtml(Html.fromHtml(nasabah.getSla()).toString()));
        }


        Picasso.with(context)
                .load(Constants.API_IMAGE_NASABAH_URL + nasabah.getImg1())
                .fit()
                .centerInside()
                .error(R.drawable.profile_user)
                .into(holder.imageView);
        if(nasabah.getStatus()!=null) {
            if (nasabah.getStatus().equalsIgnoreCase("open")) {
                holder.status.setText(nasabah.getStatus());
                holder.status.setBackgroundResource(R.drawable.border_open);
            } else if (nasabah.getStatus().equalsIgnoreCase("on progress") || nasabah.getStatus().equalsIgnoreCase("survey")) {
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