package com.sahaware.resysbni.util;

/**
 * Created by PCnya-AINI on 01/05/2016.
 */
public class Constants {
    //public static String ip = "http://"+DependencyInjection.Get(ISessionRepository.class).getIP()+"/sahawareBni";
    public static final String ip = "http://bni.referralsystems.net";
    public static int PRIVATE_MODE = 0;
    public static final String PREF_LOGIN = "LoginSession";
    public static final String PREF_CON = "ConnectionSession";
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_USERNAME = "userName";
    public static final String KEY_RULES = "roles";
    public static final String KEY_FLAG = "flag";
    public static final String KEY_MASTER_EMPTY = "isMasterEmpty";
    public static final String KEY_ID = "id";
    public static final String KEY_IP = "ip";
    public static final String KEY_SUCCESS = "Succeeded";
    public static final String KEY_STATUS = "status";
    public static final String KEY_OBJ = "obj";
    public static final int CAMERA_REQUEST = 0;
    public static final int GALLERY_PICTURE = 1;
    public static final String API_LOGIN = ip+ "/servicesreveral/login";
    public static final String API_VERIFICATION = ip + "/servicesreveral/update_user";
    public static final String API_GET_MASTER_DATA = ip + "/servicesreveral/get_all_master_data";
    public static final String API_SAVE_DATA_NASABAH = ip + "/servicesreveral/new_add_data_reveral";
    public static final String API_UPDATE_STATUS_NASABAH = ip + "/servicesreveral/update_status_nasabah";
    public static final String API_GET_DATA_NASABAH = ip + "/servicesreveral/get_all_data_reveral";
    public static final String API_GET_DATA_NASABAH_DISPOS = ip + "/servicesreveral/get_all_data_reveral_Dispos";
    public static final String API_CHECK_KTP = ip + "/servicesreveral/verifikasi_nasabah";
    public static final String API_GET_NASABAH_DETAIL= ip + "/servicesreveral/get_detail_reveral";
    public static final String API_GET_USER_DETAIL= ip + "/servicesreveral/get_detail_user";
    public static final String API_GET_REPORT =  ip + "/servicesreveral/get_all_Report";
    public static final String API_SAVE_AVATAR_USER =  ip + "/servicesreveral/upload_image_avatar/";
    public static final String API_SAVE_IMAGE_NASABAH =  ip + "/servicesreveral/upload_image_nasabah/";
    public static final String API_GET_MARKETING =  ip + "/servicesreveral/get_all_marketing";
    public static final String API_GET_STATUS =  ip + "/servicesreveral/get_all_status";
    public static final String API_DISPOS_NASABAH =  ip + "/servicesreveral/add_marketing_to_nasabah";
    public static final String API_GET_ALL_GENERAL_INFORMATION = ip + "/servicesreveral/get_all_general_informasi";
    public static final String API_IMAGE_NASABAH_URL = ip + "/Images/imageNasabah/";
    public static final String API_IMAGE_AVATAR_URL = ip + "/Images/Avatar/";
    //var Data Nasabah dari oop
    public static final String KEY_NAMA = "Nama";
    public static final String KEY_TANGGAL_SUBMIT = "tglDibuat";
    public static final String KEY_NAMA_REVERAL = "namaReveral";
    public static final String KEY_ANGGUNAN = "Anggunan";
    public static final String KEY_LAT = "Lan";
    public static final String KEY_LONG = "Lon";
    public static final String KEY_JUMLAH_KREDIT = "JmlhKredit";
    public static final String KEY_ALAMAT = "Alamat";
    public static final String KEY_NO_TELP = "NoTlpn";
    public static final String KEY_NAMA_USER = "namaUser";
    public static final String KEY_NAMA_STATUS = "namaStatus";
    public static final String KEY_IMAGE_1 = "image1";
    public static final String KEY_IMAGE_2 = "image2";
    public static final String KEY_LAMA_USAHA = "LamaUsaha";
    public static final String KEY_KTP = "KTP";
    public static final String KEY_KANTOR = "namaKantor";
    public static final String KEY_SEKTOR_USAHA = "SektorUsaha";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_NIP = "NIP";
    public static final String KEY_POINT = "Point";
    public static final String KEY_TGL_LAHIR = "TglLahir";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_JUMLAH_NASABAH = "jumlahNasabah";
    public static final String KEY_NO_TELP_USER = "noTlpn";
    public static final String KEY_NAMA_MARKETING = "namaMarketing";
    public static final String KEY_ID_MARKETING = "idMarketing";
    public static final String KEY_ID_USER = "idUser";
}
