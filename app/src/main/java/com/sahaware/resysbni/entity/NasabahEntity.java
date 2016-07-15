package com.sahaware.resysbni.entity;

/**
 * Created by PCnya-AINI on 28/04/2016.
 */
public class NasabahEntity {
    private String no_ktp,
            nama,
            alamat,
            no_hp,
            sektor_usaha,
            lama_usaha,
            jenis_kredit,
            jumlah_kredit,
            agunan,
            kantor, tanggal, status, img1, img2, namaUser;
    private Double lat, lang;
    private Integer idNasabah;

    public NasabahEntity() {
    }

    public NasabahEntity(String no_ktp, String nama, String alamat, String no_hp, String sektor_usaha, String lama_usaha, String jenis_kredit, String jumlah_kredit, String agunan, String kantor, String tanggal, String status, String img1, String img2, Double lat, Double lang) {
        this.no_ktp = no_ktp;
        this.nama = nama;
        this.alamat = alamat;
        this.no_hp = no_hp;
        this.sektor_usaha = sektor_usaha;
        this.lama_usaha = lama_usaha;
        this.jenis_kredit = jenis_kredit;
        this.jumlah_kredit = jumlah_kredit;
        this.agunan = agunan;
        this.kantor = kantor;
        this.tanggal = tanggal;
        this.status = status;
        this.img1 = img1;
        this.img2 = img2;
        this.lat = lat;
        this.lang = lang;
    }

    public NasabahEntity(String no_ktp, String nama, String alamat, String no_hp, String sektor_usaha, String lama_usaha, String jenis_kredit, String jumlah_kredit, String agunan, String kantor, String tanggal, String status, String img1, String img2, Double lat, Double lang, Integer idNasabah) {
        this.no_ktp = no_ktp;
        this.nama = nama;
        this.alamat = alamat;
        this.no_hp = no_hp;
        this.sektor_usaha = sektor_usaha;
        this.lama_usaha = lama_usaha;
        this.jenis_kredit = jenis_kredit;
        this.jumlah_kredit = jumlah_kredit;
        this.agunan = agunan;
        this.kantor = kantor;
        this.tanggal = tanggal;
        this.status = status;
        this.img1 = img1;
        this.img2 = img2;
        this.lat = lat;
        this.lang = lang;
        this.idNasabah = idNasabah;
    }

    public NasabahEntity(String no_ktp, String nama, String alamat, String no_hp, String sektor_usaha, String lama_usaha, String jenis_kredit, String jumlah_kredit, String agunan, String kantor, String tanggal, String status, String img1, String img2, Double lat, Double lang, String namaUser, Integer idNasabah) {
        this.no_ktp = no_ktp;
        this.nama = nama;
        this.alamat = alamat;
        this.no_hp = no_hp;
        this.sektor_usaha = sektor_usaha;
        this.lama_usaha = lama_usaha;
        this.jenis_kredit = jenis_kredit;
        this.jumlah_kredit = jumlah_kredit;
        this.agunan = agunan;
        this.kantor = kantor;
        this.tanggal = tanggal;
        this.status = status;
        this.img1 = img1;
        this.img2 = img2;
        this.lat = lat;
        this.lang = lang;
        this.namaUser = namaUser;
        this.idNasabah = idNasabah;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public Integer getIdNasabah() {
        return idNasabah;
    }

    public void setIdNasabah(Integer idNasabah) {
        this.idNasabah = idNasabah;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLang() {
        return lang;
    }

    public void setLang(Double lang) {
        this.lang = lang;
    }

    public String getNo_ktp() {
        return no_ktp;
    }

    public void setNo_ktp(String no_ktp) {
        this.no_ktp = no_ktp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getSektor_usaha() {
        return sektor_usaha;
    }

    public void setSektor_usaha(String sektor_usaha) {
        this.sektor_usaha = sektor_usaha;
    }

    public String getLama_usaha() {
        return lama_usaha;
    }

    public void setLama_usaha(String lama_usaha) {
        this.lama_usaha = lama_usaha;
    }

    public String getJenis_kredit() {
        return jenis_kredit;
    }

    public void setJenis_kredit(String jenis_kredit) {
        this.jenis_kredit = jenis_kredit;
    }

    public String getJumlah_kredit() {
        return jumlah_kredit;
    }

    public void setJumlah_kredit(String jumlah_kredit) {
        this.jumlah_kredit = jumlah_kredit;
    }

    public String getAgunan() {
        return agunan;
    }

    public void setAgunan(String agunan) {
        this.agunan = agunan;
    }

    public String getKantor() {
        return kantor;
    }

    public void setKantor(String kantor) {
        this.kantor = kantor;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
