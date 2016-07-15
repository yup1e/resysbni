package com.sahaware.resysbni.entity;

public class JenisKantor {

    private Integer id;
    private String CreateDate;
    private String namaJenis;
    private Object UpdateDate;

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The CreateDate
     */
    public String getCreateDate() {
        return CreateDate;
    }

    /**
     *
     * @param CreateDate
     * The CreateDate
     */
    public void setCreateDate(String CreateDate) {
        this.CreateDate = CreateDate;
    }

    /**
     *
     * @return
     * The namaJenis
     */
    public String getNamaJenis() {
        return namaJenis;
    }

    /**
     *
     * @param namaJenis
     * The namaJenis
     */
    public void setNamaJenis(String namaJenis) {
        this.namaJenis = namaJenis;
    }

    /**
     *
     * @return
     * The UpdateDate
     */
    public Object getUpdateDate() {
        return UpdateDate;
    }

    /**
     *
     * @param UpdateDate
     * The UpdateDate
     */
    public void setUpdateDate(Object UpdateDate) {
        this.UpdateDate = UpdateDate;
    }

}