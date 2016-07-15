package com.sahaware.resysbni.entity;

/**
 * Created by DILo on 5/18/2016.
 */
public class DataUser {

    private int id;
    private String NIP;
    private String Alamat;
    private String noTlpn;
    private String Email;
    private String Nama;
    private int Point;
    private String TglLahir;
    private String jumlahNasabah;
    private String avatar;

    public DataUser() {
    }

    public DataUser(String NIP, String alamat, String noTlpn, String email, String nama, String tglLahir) {
        this.NIP = NIP;
        Alamat = alamat;
        this.noTlpn = noTlpn;
        Email = email;
        Nama = nama;
        TglLahir = tglLahir;
    }

    public DataUser(int id, String NIP, String alamat, String noTlpn, String email, String nama, int point, String tglLahir, String jumlahNasabah, String avatar) {
        this.id = id;
        this.NIP = NIP;
        Alamat = alamat;
        this.noTlpn = noTlpn;
        Email = email;
        Nama = nama;
        Point = point;
        TglLahir = tglLahir;
        this.jumlahNasabah = jumlahNasabah;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJumlahNasabah() {
        return jumlahNasabah;
    }

    public void setJumlahNasabah(String jumlahNasabah) {
        this.jumlahNasabah = jumlahNasabah;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     *
     * @return
     * The NIP
     */
    public String getNIP() {
        return NIP;
    }

    /**
     *
     * @param NIP
     * The NIP
     */
    public void setNIP(String NIP) {
        this.NIP = NIP;
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
     * The noTlpn
     */
    public String getNoTlpn() {
        return noTlpn;
    }

    /**
     *
     * @param noTlpn
     * The noTlpn
     */
    public void setNoTlpn(String noTlpn) {
        this.noTlpn = noTlpn;
    }

    /**
     *
     * @return
     * The Email
     */
    public String getEmail() {
        return Email;
    }

    /**
     *
     * @param Email
     * The Email
     */
    public void setEmail(String Email) {
        this.Email = Email;
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
     * The Point
     */
    public int getPoint() {
        return Point;
    }

    /**
     *
     * @param Point
     * The Point
     */
    public void setPoint(int Point) {
        this.Point = Point;
    }

    /**
     *
     * @return
     * The TglLahir
     */
    public String getTglLahir() {
        return TglLahir;
    }

    /**
     *
     * @param TglLahir
     * The TglLahir
     */
    public void setTglLahir(String TglLahir) {
        this.TglLahir = TglLahir;
    }
}
