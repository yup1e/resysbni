package com.sahaware.resysbni.entity;

public class DataJenisReveral {

    private String Keterangan;
    private String Nama;
    private int IDJenis;

    public DataJenisReveral() {
    }

    public DataJenisReveral(int IDJenis, String nama, String keterangan ) {
        Keterangan = keterangan;
        Nama = nama;
        this.IDJenis = IDJenis;
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
     * The IDJenis
     */
    public Integer getIDJenis() {
        return IDJenis;
    }

    /**
     *
     * @param IDJenis
     * The IDJenis
     */
    public void setIDJenis(Integer IDJenis) {
        this.IDJenis = IDJenis;
    }

}
