package com.sahaware.resysbni.entity;

/**
 * Created by DSTYO on 13/05/2016.
 */
public class MasterEntity {

    public String IdKantor, NamaKantor, Latitude, Langitude, JenisKantor, AlamatKantor, IdPinjaman, NamaPinjaman, Keterangan
            , IdStatus, NamaStatus, KeteranganStatus;

    public MasterEntity() {
    }

    public void MasterEntity(String idKantor, String namaKantor, String latitude, String langitude, String jenisKantor, String alamatKantor) {
        IdKantor = idKantor;
        NamaKantor = namaKantor;
        Latitude = latitude;
        Langitude = langitude;
        JenisKantor = jenisKantor;
        AlamatKantor = alamatKantor;
    }

    public MasterEntity(String idPinjaman, String namaPinjaman, String keterangan) {
        IdPinjaman = idPinjaman;
        NamaPinjaman = namaPinjaman;
        Keterangan = keterangan;
    }



/*    public MasterEntity(String idStatus, String namaStatus, String keteranganStatus) {
        IdStatus = idStatus;
        NamaStatus = namaStatus;
        KeteranganStatus = keteranganStatus;
    }*/

    public String getIdKantor() {
        return IdKantor;
    }

    public void setIdKantor(String idKantor) {
        IdKantor = idKantor;
    }

    public String getNamaKantor() {
        return NamaKantor;
    }

    public void setNamaKantor(String namaKantor) {
        NamaKantor = namaKantor;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLangitude() {
        return Langitude;
    }

    public void setLangitude(String langitude) {
        Langitude = langitude;
    }

    public String getJenisKantor() {
        return JenisKantor;
    }

    public void setJenisKantor(String jenisKantor) {
        JenisKantor = jenisKantor;
    }

    public String getAlamatKantor() {
        return AlamatKantor;
    }

    public void setAlamatKantor(String alamatKantor) {
        AlamatKantor = alamatKantor;
    }

    public String getIdPinjaman() {
        return IdPinjaman;
    }

    public void setIdPinjaman(String idPinjaman) {
        IdPinjaman = idPinjaman;
    }

    public String getNamaPinjaman() {
        return NamaPinjaman;
    }

    public void setNamaPinjaman(String namaPinjaman) {
        NamaPinjaman = namaPinjaman;
    }

    public String getKeterangan() {
        return Keterangan;
    }

    public void setKeterangan(String keterangan) {
        Keterangan = keterangan;
    }

    public String getIdStatus() {
        return IdStatus;
    }

    public void setIdStatus(String idStatus) {
        IdStatus = idStatus;
    }

    public String getNamaStatus() {
        return NamaStatus;
    }

    public void setNamaStatus(String namaStatus) {
        NamaStatus = namaStatus;
    }

    public String getKeteranganStatus() {
        return KeteranganStatus;
    }

    public void setKeteranganStatus(String keteranganStatus) {
        KeteranganStatus = keteranganStatus;
    }
}

