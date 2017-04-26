package com.sahaware.resysbni.repository;

import com.sahaware.resysbni.entity.DataGeneralInformation;
import com.sahaware.resysbni.entity.DataJenisReveral;
import com.sahaware.resysbni.entity.DataKantor;
import com.sahaware.resysbni.entity.DataReport;
import com.sahaware.resysbni.entity.DataUser;
import com.sahaware.resysbni.entity.Datastatus;
import com.sahaware.resysbni.entity.NasabahEntity;

import java.util.List;

/**
 * Created by DSTYO on 13/05/2016.
 */
public interface ISqliteRepository {
    void addNasabah(NasabahEntity nasabah);
    void addNasabahDispos(NasabahEntity nasabah);
    void addNasabahTemp(NasabahEntity nasabah);
    void addKantor(DataKantor dataKantor);
    void addJenisPinjaman(DataJenisReveral dataJenisReveral);
    void addJenisStatus(Datastatus datastatus);
    void addDetailUser (DataUser dataUser);
    void addReport (DataReport dataReport);
    void addInformation (DataGeneralInformation dataGeneralInformation);
    void updateAvatarUser(String avatar, String nip);
    List<DataKantor> getAllKantor();
    List<DataJenisReveral> getAllJenisPinjaman();
    List<NasabahEntity> getAllNasabah();
    List<NasabahEntity> getAllNasabahDispos();
    List<NasabahEntity> getAllNasabahTemp();
    List<Datastatus> getAllStatus();
    List<DataReport> getAllReport();
    List<DataGeneralInformation> getAllInformation();
    DataUser getDetailUser();
    int getContactsCount();
    boolean isGeneralInformationEmpty();
    boolean isMasterKantorEmpty();
    boolean isDataNasabahEmpty();
    boolean isDataNasabahDisposEmpty();
    boolean isDataReportEmpty();
    boolean isMasterJenisPinjamanEmpty();
    void clearNasabah();
    void clearNasabahDispos();
    void clearNasabahTemp();
    void clearDetailUser();
    void clearReport();
    void clearInformation();
    void resetDatabase();
}
