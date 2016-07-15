package com.sahaware.resysbni.entity;

public class DataKantor {

    private String Alamat;
    private String jenisKantor;
    private int IDKantor;
    private String Nama;
    private String Lon;
    private String Lan;
    private String noTelp;
    private String jarak;

    public DataKantor() {
    }

    public DataKantor(String alamat, String jenisKantor, int IDKantor, String nama, String lon, String lan, String noTelp, String jarak) {
        Alamat = alamat;
        this.jenisKantor = jenisKantor;
        this.IDKantor = IDKantor;
        Nama = nama;
        Lon = lon;
        Lan = lan;
        this.noTelp = noTelp;
        this.jarak = jarak;
    }

    public DataKantor(String alamat, String jenisKantor, Integer IDKantor, String nama, String lon, String lan, String noTelp) {
        Alamat = alamat;
        this.jenisKantor = jenisKantor;
        this.IDKantor = IDKantor;
        Nama = nama;
        Lon = lon;
        Lan = lan;
        this.noTelp = noTelp;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public String getJarak() {
        return jarak;
    }

    public void setJarak(String jarak) {
        this.jarak = jarak;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    /**
     *
     * @return
     * The Alamat
     */
    public String getAlamat() {
        return Alamat;
    }

    /**
     *
     * @param Alamat
     * The Alamat
     */
    public void setAlamat(String Alamat) {
        this.Alamat = Alamat;
    }

    /**
     *
     * @return
     * The jenisKantor
     */
    public String getJenisKantor() {
        return jenisKantor;
    }

    /**
     *
     * @param jenisKantor
     * The jenisKantor
     */
    public void setJenisKantor(String jenisKantor) {
        this.jenisKantor = jenisKantor;
    }

    /**
     *
     * @return
     * The IDKantor
     */
    public Integer getIDKantor() {
        return IDKantor;
    }

    /**
     *
     * @param IDKantor
     * The IDKantor
     */
    public void setIDKantor(Integer IDKantor) {
        this.IDKantor = IDKantor;
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
     * The Lon
     */
    public String getLon() {
        return Lon;
    }

    /**
     *
     * @param Lon
     * The Lon
     */
    public void setLon(String Lon) {
        this.Lon = Lon;
    }

    /**
     *
     * @return
     * The Lan
     */
    public String getLan() {
        return Lan;
    }

    /**
     *
     * @param Lan
     * The Lan
     */
    public void setLan(String Lan) {
        this.Lan = Lan;
    }

}
