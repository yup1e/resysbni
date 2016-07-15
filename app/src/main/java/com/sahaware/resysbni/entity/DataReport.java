package com.sahaware.resysbni.entity;

public class DataReport {

    private String Keterangan;
    private String Jam;
    private String Tanggal;
    private Integer ID;


    public DataReport() {
    }

    public DataReport(Integer idNasabah, String keterangan, String jam, String tanggal) {
        Keterangan = keterangan;
        Jam = jam;
        Tanggal = tanggal;
        ID = idNasabah;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getKeterangan() {
        return Keterangan;
    }

    public void setKeterangan(String keterangan) {
        Keterangan = keterangan;
    }

    public String getJam() {
        return Jam;
    }

    public void setJam(String jam) {
        Jam = jam;
    }

    public String getTanggal() {
        return Tanggal;
    }

    public void setTanggal(String tanggal) {
        Tanggal = tanggal;
    }
}