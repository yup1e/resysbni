package com.sahaware.resysbni.entity;

public class Datastatus {

    private String Keterangan;
    private String Nama;
    private Integer IDStatus;

    public Datastatus() {
    }

    public Datastatus( Integer IDStatus, String nama,String keterangan) {
        Keterangan = keterangan;
        Nama = nama;
        this.IDStatus = IDStatus;
    }

    /**
     *
     * @return
     * The Keterangan
     */
    public String getKeterangan() {
        return Keterangan;
    }

    /**
     *
     * @param Keterangan
     * The Keterangan
     */
    public void setKeterangan(String Keterangan) {
        this.Keterangan = Keterangan;
    }

    /**
     *
     * @return
     * The Nama
     */
    public String getNama() {
        return Nama;
    }

    /**
     *
     * @param Nama
     * The Nama
     */
    public void setNama(String Nama) {
        this.Nama = Nama;
    }

    /**
     *
     * @return
     * The IDStatus
     */
    public Integer getIDStatus() {
        return IDStatus;
    }

    /**
     *
     * @param IDStatus
     * The IDStatus
     */
    public void setIDStatus(Integer IDStatus) {
        this.IDStatus = IDStatus;
    }

}