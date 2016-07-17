package com.sahaware.resysbni.implementation;

/**
 * Created by PCnya-AINI on 28/04/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sahaware.resysbni.entity.DataGeneralInformation;
import com.sahaware.resysbni.entity.DataJenisReveral;
import com.sahaware.resysbni.entity.DataKantor;
import com.sahaware.resysbni.entity.DataReport;
import com.sahaware.resysbni.entity.DataUser;
import com.sahaware.resysbni.entity.Datastatus;
import com.sahaware.resysbni.entity.NasabahEntity;
import com.sahaware.resysbni.repository.ISqliteRepository;
import com.sahaware.resysbni.util.UtilityImageByte;

import java.util.ArrayList;
import java.util.List;

public class SqliteRepository extends SQLiteOpenHelper implements ISqliteRepository {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "db_bnisys";

    // table name
    private static final String TABLE_NASABAH = "t_nasabah";
    private static final String TABLE_NASABAH_TEMP = "t_nasabah_temp";
    private static final String TABLE_MASTER_KANTOR = "t_kantor";
    private static final String TABLE_MASTER_JENIS_PINJAMAN = "t_jenis_pinjaman";
    private static final String TABLE_MASTER_STATUS = "t_status_nasabah";
    private static final String TABLE_DETAIL_USER = "t_detail_user";
    private static final String TABLE_REPORT = "t_report";
    private static final String TABLE_INFORMATION = "t_information";


    // Nasabah Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NO_KTP = "no_ktp";
    private static final String KEY_NAMA = "nama";
    private static final String KEY_ALAMAT = "alamat";
    private static final String KEY_NO_HP = "no_hp";
    private static final String KEY_SEKTOR = "sektor";
    private static final String KEY_LAMA = "lama";
    private static final String KEY_JENIS = "jenis";
    private static final String KEY_JUMLAH = "jumlah";
    private static final String KEY_AGUNAN = "agunan";
    private static final String KEY_KANTOR = "kantor";
    private static final String KEY_IMG1 = "img1";
    private static final String KEY_IMG2 = "img2";
    private static final String KEY_WAKTU = "waktu_submit";
    private static final String KEY_STATUS = "status";
    private static final String KEY_SLA = "sla";

    // Master Office Table Columns names
    private static final String KEY_NAMA_KANTOR = "nama_kantor";
    private static final String KEY_LAT = "latitude";
    private static final String KEY_LANG = "langitude";
    private static final String KEY_JENIS_KANTOR = "jenis_kantor";
    private static final String KEY_ALAMAT_KANTOR = "alamat_kantor";
    private static final String KEY_NOTELP_KANTOR = "telp_kantor";

    //Master Jenis Pinjaman Table Coloumns names
    private static final String KEY_NAMA_PINJAMAN = "nama_pinjaman";
    private static final String KEY_KETERANGAN = "keterangan";

    //Master Jenis Status Table Coloumns names
    private static final String KEY_NAMA_STATUS = "nama_status";

    //Detail user
    private static final String KEY_NIP_USER = "NIP";
    private static final String KEY_ALAMAT_USER = "alamatUser";
    private static final String KEY_NO_TELP = "noTelp";
    private static final String KEY_EMAIL_USER = "email";
    private static final String KEY_NAMA_USER = "nama";
    private static final String KEY_POINT_USER = "point";
    private static final String KEY_TGL_LAHIR_USER = "tglLahir";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_JUMLAH_NASABAH = "jmlNasabah";

    //List Report
    private static final String KEY_KETERANGAN_REPORT = "keterangan";
    private static final String KEY_DATE_REPORT = "tanggal";
    private static final String KEY_TIME_REPORT = "jam";

    //List information
    private static final String KEY_TITLE_INFORMATION = "title";
    private static final String KEY_SECTION_INFORMATION = "section";
    private static final String KEY_DESCRIPTION_INFORMATION = "desctiption";

    UtilityImageByte util;


    public SqliteRepository(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NASABAH_TABLE = "CREATE TABLE " + TABLE_NASABAH + "("
                + KEY_ID + " INTEGER,"
                + KEY_NO_KTP + " TEXT,"
                + KEY_NAMA + " TEXT,"
                + KEY_ALAMAT + " TEXT,"
                + KEY_NO_HP + " TEXT,"
                + KEY_SEKTOR + " TEXT,"
                + KEY_LAMA + " TEXT,"
                + KEY_JENIS + " TEXT,"
                + KEY_JUMLAH + " TEXT,"
                + KEY_AGUNAN + " TEXT,"
                + KEY_KANTOR + " TEXT,"
                + KEY_LAT + " TEXT,"
                + KEY_LANG + " TEXT,"
                + KEY_IMG1 + " TEXT,"
                + KEY_IMG2 + " TEXT,"
                + KEY_WAKTU + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + KEY_SLA + " TEXT" + ")";

        String CREATE_NASABAH_TEMP_TABLE = "CREATE TABLE " + TABLE_NASABAH_TEMP + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NO_KTP + " TEXT,"
                + KEY_NAMA + " TEXT,"
                + KEY_ALAMAT + " TEXT,"
                + KEY_NO_HP + " TEXT,"
                + KEY_SEKTOR + " TEXT,"
                + KEY_LAMA + " TEXT,"
                + KEY_JENIS + " TEXT,"
                + KEY_JUMLAH + " TEXT,"
                + KEY_AGUNAN + " TEXT,"
                + KEY_KANTOR + " TEXT,"
                + KEY_LAT + " TEXT,"
                + KEY_LANG + " TEXT,"
                + KEY_IMG1 + " TEXT,"
                + KEY_IMG2 + " TEXT,"
                + KEY_WAKTU + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + KEY_SLA + " TEXT" + ")";

        String CREATE_MASTER_KANTOR = "CREATE TABLE " + TABLE_MASTER_KANTOR + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAMA_KANTOR + " TEXT,"
                + KEY_LAT + " TEXT,"
                + KEY_LANG + " TEXT,"
                + KEY_JENIS_KANTOR + " TEXT,"
                + KEY_NOTELP_KANTOR + " TEXT, "
                + KEY_ALAMAT_KANTOR + " TEXT" + ")";

        String CREATE_MASTER_JENIS = "CREATE TABLE " + TABLE_MASTER_JENIS_PINJAMAN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAMA_PINJAMAN + " TEXT,"
                + KEY_KETERANGAN + " TEXT" + ")";

        String CREATE_MASTER_STATUS = "CREATE TABLE " + TABLE_MASTER_STATUS + "("
                + KEY_ID + " INTEGER,"
                + KEY_NAMA_STATUS + " TEXT,"
                + KEY_KETERANGAN + " TEXT" + ")";

        String CREATE_MASTER_INFORMATION = "CREATE TABLE " + TABLE_INFORMATION + "("
                + KEY_SECTION_INFORMATION + " TEXT,"
                + KEY_TITLE_INFORMATION + " TEXT,"
                + KEY_DESCRIPTION_INFORMATION + " TEXT" + ")";

        String CREATE_DETAIL_USER = "CREATE TABLE " + TABLE_DETAIL_USER + "("
                + KEY_NIP_USER + " TEXT,"
                + KEY_ALAMAT_USER + " TEXT,"
                + KEY_NO_TELP + " TEXT,"
                + KEY_JUMLAH_NASABAH + " TEXT,"
                + KEY_AVATAR + " TEXT,"
                + KEY_EMAIL_USER + " TEXT,"
                + KEY_NAMA_USER + " TEXT,"
                + KEY_POINT_USER + " INTEGER,"
                + KEY_TGL_LAHIR_USER + " TEXT" + ")";

        String CREATE_REPORT_TABLE = "CREATE TABLE " + TABLE_REPORT + "("
                + KEY_ID + " INTEGER,"
                + KEY_KETERANGAN_REPORT + " TEXT,"
                + KEY_DATE_REPORT + " TEXT,"
                + KEY_TIME_REPORT + " TEXT" + ")";

        db.execSQL(CREATE_NASABAH_TABLE);
        db.execSQL(CREATE_NASABAH_TEMP_TABLE);
        db.execSQL(CREATE_MASTER_KANTOR);
        db.execSQL(CREATE_MASTER_JENIS);
        db.execSQL(CREATE_MASTER_STATUS);
        db.execSQL(CREATE_DETAIL_USER);
        db.execSQL(CREATE_REPORT_TABLE);
        db.execSQL(CREATE_MASTER_INFORMATION);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NASABAH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NASABAH_TEMP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_KANTOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_JENIS_PINJAMAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_STATUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAIL_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INFORMATION);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addNasabah(NasabahEntity nasabah) {
        SQLiteDatabase db = this.getWritableDatabase();
        util = new UtilityImageByte();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, nasabah.getIdNasabah());
        values.put(KEY_NO_KTP, nasabah.getNo_ktp());
        values.put(KEY_NAMA, nasabah.getNama());
        values.put(KEY_ALAMAT, nasabah.getAlamat());
        values.put(KEY_NO_HP, nasabah.getNo_hp());
        values.put(KEY_SEKTOR, nasabah.getSektor_usaha());
        values.put(KEY_LAMA, nasabah.getLama_usaha());
        values.put(KEY_JENIS, nasabah.getJenis_kredit());
        values.put(KEY_JUMLAH, nasabah.getJumlah_kredit());
        values.put(KEY_AGUNAN, nasabah.getAgunan());
        values.put(KEY_KANTOR, nasabah.getKantor());
        values.put(KEY_IMG1, nasabah.getImg1());
        values.put(KEY_IMG2, nasabah.getImg2());
        values.put(KEY_WAKTU, nasabah.getTanggal());
        values.put(KEY_STATUS, nasabah.getStatus());
        values.put(KEY_LAT, nasabah.getLat());
        values.put(KEY_LANG, nasabah.getLang());
        values.put(KEY_SLA, nasabah.getSla());
        // Inserting Row
        db.insert(TABLE_NASABAH, null, values);
        db.close(); // Closing database connection
    }

    public void clearNasabah() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NASABAH, null, null);
    }

    // Adding new contact
    public void addNasabahTemp(NasabahEntity nasabah) {
        SQLiteDatabase db = this.getWritableDatabase();
        util = new UtilityImageByte();
        ContentValues values = new ContentValues();
        values.put(KEY_NO_KTP, nasabah.getNo_ktp());
        values.put(KEY_NAMA, nasabah.getNama());
        values.put(KEY_ALAMAT, nasabah.getAlamat());
        values.put(KEY_NO_HP, nasabah.getNo_hp());
        values.put(KEY_SEKTOR, nasabah.getSektor_usaha());
        values.put(KEY_LAMA, nasabah.getLama_usaha());
        values.put(KEY_JENIS, nasabah.getJenis_kredit());
        values.put(KEY_JUMLAH, nasabah.getJumlah_kredit());
        values.put(KEY_AGUNAN, nasabah.getAgunan());
        values.put(KEY_KANTOR, nasabah.getKantor());
        values.put(KEY_IMG1, nasabah.getImg1());
        values.put(KEY_IMG2, nasabah.getImg2());
        values.put(KEY_WAKTU, nasabah.getTanggal());
        values.put(KEY_STATUS, nasabah.getStatus());
        values.put(KEY_LAT, nasabah.getLat());
        values.put(KEY_LANG, nasabah.getLang());
        values.put(KEY_SLA, nasabah.getSla());
        // Inserting Row
        db.insert(TABLE_NASABAH_TEMP, null, values);
        db.close(); // Closing database connection
    }

    public void clearNasabahTemp() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NASABAH_TEMP, null, null);
    }

    // Adding new contact
    public void addKantor(DataKantor dataKantor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, dataKantor.getIDKantor());
        values.put(KEY_NAMA_KANTOR, dataKantor.getNama());
        values.put(KEY_LAT, dataKantor.getLan());
        values.put(KEY_LANG, dataKantor.getLon());
        values.put(KEY_JENIS_KANTOR, dataKantor.getJenisKantor());
        values.put(KEY_NOTELP_KANTOR, dataKantor.getJenisKantor());
        values.put(KEY_ALAMAT_KANTOR, dataKantor.getAlamat());
        // Inserting Row
        db.insert(TABLE_MASTER_KANTOR, null, values);
        db.close(); // Closing database connection
    }

    public void addJenisPinjaman(DataJenisReveral dataJenisReveral) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, dataJenisReveral.getIDJenis());
        values.put(KEY_NAMA_PINJAMAN, dataJenisReveral.getNama());
        values.put(KEY_KETERANGAN, dataJenisReveral.getKeterangan());
        db.insert(TABLE_MASTER_JENIS_PINJAMAN, null, values);
        db.close();
    }

    public void addReport(DataReport dataReport) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, dataReport.getID());
        values.put(KEY_KETERANGAN_REPORT, dataReport.getKeterangan());
        values.put(KEY_DATE_REPORT, dataReport.getTanggal());
        values.put(KEY_TIME_REPORT, dataReport.getJam());
        db.insert(TABLE_REPORT, null, values);
        db.close();
    }

    public void clearReport() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REPORT, null, null);
    }

    public void addInformation(DataGeneralInformation dataGeneralInformation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SECTION_INFORMATION, dataGeneralInformation.getSection());
        values.put(KEY_DESCRIPTION_INFORMATION, dataGeneralInformation.getDescription());
        values.put(KEY_TITLE_INFORMATION, dataGeneralInformation.getTitle());
        db.insert(TABLE_INFORMATION, null, values);
        db.close();
    }

    public void clearInformation() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INFORMATION, null, null);
    }

    public void addJenisStatus(Datastatus datastatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, datastatus.getIDStatus());
        values.put(KEY_NAMA_STATUS, datastatus.getNama());
        values.put(KEY_KETERANGAN, datastatus.getKeterangan());
        db.insert(TABLE_MASTER_STATUS, null, values);
        db.close();
    }

    public void addDetailUser(DataUser dataUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NIP_USER, dataUser.getNIP());
        values.put(KEY_ALAMAT_USER, dataUser.getAlamat());
        values.put(KEY_NO_TELP, dataUser.getNoTlpn());
        values.put(KEY_EMAIL_USER, dataUser.getEmail());
        values.put(KEY_NAMA_USER, dataUser.getNama());
        values.put(KEY_POINT_USER, dataUser.getPoint());
        values.put(KEY_TGL_LAHIR_USER, dataUser.getTglLahir());
        values.put(KEY_AVATAR, dataUser.getAvatar());
        values.put(KEY_JUMLAH_NASABAH, dataUser.getJumlahNasabah());
        db.insert(TABLE_DETAIL_USER, null, values);
        db.close();
    }

    public void clearDetailUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DETAIL_USER, null, null);
    }

    public void updateAvatarUser(String avatar, String nip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("avatar",avatar);
        db.update(TABLE_DETAIL_USER, cv, KEY_NIP_USER+" = ?", new String[] {nip});
    }

    // Getting single contact
    NasabahEntity getNasabah(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NASABAH, new String[]{KEY_NO_KTP,
                        KEY_NAMA, KEY_ALAMAT, KEY_NO_HP, KEY_SEKTOR, KEY_LAMA, KEY_JENIS, KEY_JUMLAH, KEY_AGUNAN, KEY_KANTOR, KEY_LAT, KEY_LANG, KEY_IMG1, KEY_IMG2, KEY_WAKTU, KEY_STATUS, KEY_SLA}, KEY_NO_KTP + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        NasabahEntity contact = new NasabahEntity(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5),
                cursor.getString(6), cursor.getString(7), cursor.getString(8),
                cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13), cursor.getDouble(14), cursor.getDouble(15), cursor.getString(16));
        // return contact
        return contact;
    }

    public DataUser getDetailUser() {
        String selectQuery = "SELECT  * FROM " + TABLE_DETAIL_USER;
        DataUser user = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            user = new DataUser();
            user.setNIP(cursor.getString(0));
            user.setAlamat(cursor.getString(1));
            user.setNoTlpn(cursor.getString(2));
            user.setJumlahNasabah(cursor.getString(3));
            user.setAvatar(cursor.getString(4));
            user.setEmail(cursor.getString(5));
            user.setNama(cursor.getString(6));
            user.setPoint(cursor.getInt(7));
            user.setTglLahir(cursor.getString(8));
        }
        return user;
    }

    public List<DataKantor> getAllKantor() {
        List<DataKantor> kantorList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_MASTER_KANTOR;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DataKantor kantor = new DataKantor();
                kantor.setIDKantor(cursor.getInt(0));
                kantor.setNama(cursor.getString(1));
                kantor.setLan(cursor.getString(2));
                kantor.setLon(cursor.getString(3));
                kantor.setJenisKantor(cursor.getString(4));
                kantor.setNoTelp(cursor.getString(5));
                kantor.setAlamat(cursor.getString(6));
                // Adding contact to list
                kantorList.add(kantor);
            } while (cursor.moveToNext());
        }
        return kantorList;
    }

    public List<DataJenisReveral> getAllJenisPinjaman() {
        List<DataJenisReveral> jenisPinjamanList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_MASTER_JENIS_PINJAMAN;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DataJenisReveral jenisPinjaman = new DataJenisReveral();
                jenisPinjaman.setIDJenis(cursor.getInt(0));
                jenisPinjaman.setNama(cursor.getString(1));
                jenisPinjaman.setKeterangan(cursor.getString(2));
                // Adding contact to list
                jenisPinjamanList.add(jenisPinjaman);
            } while (cursor.moveToNext());
        }
        return jenisPinjamanList;
    }

    public List<DataReport> getAllReport() {
        List<DataReport> listDataReport = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_REPORT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DataReport dataReport = new DataReport();
                dataReport.setID(cursor.getInt(0));
                dataReport.setKeterangan(cursor.getString(1));
                dataReport.setTanggal(cursor.getString(2));
                dataReport.setJam(cursor.getString(3));
                // Adding contact to list
                listDataReport.add(dataReport);
            } while (cursor.moveToNext());
        }
        return listDataReport;
    }

    public List<Datastatus> getAllStatus() {
        List<Datastatus> jenisStatusList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_MASTER_STATUS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Datastatus jenisstatus = new Datastatus();
                jenisstatus.setIDStatus(cursor.getInt(0));
                jenisstatus.setNama(cursor.getString(1));
                jenisstatus.setKeterangan(cursor.getString(2));
                // Adding contact to list
                jenisStatusList.add(jenisstatus);
            } while (cursor.moveToNext());
        }
        return jenisStatusList;
    }

    public List<DataGeneralInformation> getAllInformation() {
        List<DataGeneralInformation> dataInformationList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_INFORMATION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DataGeneralInformation dataInformation = new DataGeneralInformation();
                dataInformation.setSection(cursor.getString(0));
                dataInformation.setTitle(cursor.getString(1));
                dataInformation.setDescription(cursor.getString(2));
                // Adding contact to list
                dataInformationList.add(dataInformation);
            } while (cursor.moveToNext());
        }
        return dataInformationList;
    }

    public List<NasabahEntity> getAllNasabah() {
        List<NasabahEntity> nasabahList = new ArrayList<NasabahEntity>();
        String selectQuery = "SELECT  * FROM " + TABLE_NASABAH;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                NasabahEntity contact = new NasabahEntity();
                contact.setIdNasabah(cursor.getInt(0));
                contact.setNo_ktp(cursor.getString(1));
                contact.setNama(cursor.getString(2));
                contact.setAlamat(cursor.getString(3));
                contact.setNo_hp(cursor.getString(4));
                contact.setSektor_usaha(cursor.getString(5));
                contact.setLama_usaha(cursor.getString(6));
                contact.setJenis_kredit(cursor.getString(7));
                contact.setJumlah_kredit(cursor.getString(8));
                contact.setAgunan(cursor.getString(9));
                contact.setKantor(cursor.getString(10));
                contact.setLat(cursor.getDouble(11));
                contact.setLang(cursor.getDouble(12));
                contact.setImg1(cursor.getString(13));
                contact.setImg2(cursor.getString(14));
                contact.setTanggal(cursor.getString(15));
                contact.setStatus(cursor.getString(16));
                contact.setSla(cursor.getString(17));
                // Adding contact to list
                nasabahList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return nasabahList;
    }

    public List<NasabahEntity> getAllNasabahTemp() {
        List<NasabahEntity> nasabahList = new ArrayList<NasabahEntity>();
        String selectQuery = "SELECT  * FROM " + TABLE_NASABAH_TEMP;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                NasabahEntity contact = new NasabahEntity();
                contact.setIdNasabah(cursor.getInt(0));
                contact.setNo_ktp(cursor.getString(1));
                contact.setNama(cursor.getString(2));
                contact.setAlamat(cursor.getString(3));
                contact.setNo_hp(cursor.getString(4));
                contact.setSektor_usaha(cursor.getString(5));
                contact.setLama_usaha(cursor.getString(6));
                contact.setJenis_kredit(cursor.getString(7));
                contact.setJumlah_kredit(cursor.getString(8));
                contact.setAgunan(cursor.getString(9));
                contact.setKantor(cursor.getString(10));
                contact.setLat(cursor.getDouble(11));
                contact.setLang(cursor.getDouble(12));
                contact.setImg1(cursor.getString(13));
                contact.setImg2(cursor.getString(14));
                contact.setTanggal(cursor.getString(15));
                contact.setStatus(cursor.getString(16));
                // Adding contact to list
                nasabahList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return nasabahList;
    }

/*    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }*/

/*    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }*/


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NASABAH;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public boolean isDataNasabahEmpty() {
        boolean flag;

        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_NASABAH;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0)
            flag = false;
        else
            flag = true;
        return flag;
    }

    public boolean isDataReportEmpty() {
        boolean flag;

        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_REPORT;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0)
            flag = false;
        else
            flag = true;
        return flag;
    }

    public boolean isMasterKantorEmpty() {
        boolean flag;

        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_MASTER_KANTOR;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0)
            flag = false;
        else
            flag = true;
        return flag;
    }

    public boolean isMasterJenisPinjamanEmpty() {
        boolean flag;

        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_MASTER_JENIS_PINJAMAN;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0)
            flag = false;
        else
            flag = true;
        return flag;
    }
}
