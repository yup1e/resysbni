package com.sahaware.resysbni.view.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sahaware.resysbni.R;
import com.sahaware.resysbni.entity.DataUser;
import com.sahaware.resysbni.helper.DependencyInjection;
import com.sahaware.resysbni.repository.ISessionRepository;
import com.sahaware.resysbni.repository.ISqliteRepository;
import com.sahaware.resysbni.util.Constants;
import com.sahaware.resysbni.view.activity.LoginActivity;
import com.sahaware.resysbni.view.custom.CustomCircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
//import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
//import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import dmax.dialog.SpotsDialog;
import info.hoang8f.widget.FButton;


public class ProfileFragment extends Fragment {
    @BindView(R.id.txt_profile_alamat)
    TextView txt_profile_alamat;
    @BindView(R.id.txt_profile_email)
    TextView txt_profile_email;
    @BindView(R.id.txt_profile_jumlah_nasabah)
    TextView txt_profile_jumlah_nasabah;
    @BindView(R.id.txt_profile_point)
    TextView txt_profile_point;
    @BindView(R.id.txt_profile_nama)
    TextView txt_profile_nama;
    @BindView(R.id.txt_profile_nip)
    TextView txt_profile_nip;
    @BindView(R.id.txt_profile_no_telp)
    TextView txt_profile_no_telp;
    @BindView(R.id.txt_profile_tgl_lahir)
    TextView txt_profile_tgl_lahir;
    @BindView(R.id.txt_image_path)
    TextView txt_image_path;
    @BindView(R.id.user_profile_photo)
    CustomCircularImageView user_profile_photo;
    @BindView(R.id.btn_logout)
    FButton loginButton;
    private Unbinder unbinder;
    private boolean isViewShown = false;
    private SpotsDialog progressDialog;
    int MY_PERMISSIONS_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE;
    FloatingActionButton fab;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private Intent pictureActionIntent = null;
    Bitmap bitmap;
    final Context context = getActivity();
    private static final String TAG = "UploadImage";
    String selectedImagePath;
    ImageView id_img;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        LinearLayout toolbar = (LinearLayout) view.findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("PROFILE AGENT");
        progressDialog = new SpotsDialog(getActivity());
        //TextView txt_profile_nama = ButterKnife.findById(view, R.id.txt_profile_nama);
        ButterKnife.bind(this, view);
        //txt_profile_nama.setText("Noor Shakato Eka Dhana");

        // Inflate the layout for this fragment

        if(!DependencyInjection.Get(ISqliteRepository.class).isDataNasabahEmpty()){
            DataUser dataUser;
            dataUser = DependencyInjection.Get(ISqliteRepository.class).getDetailUser();
            if(dataUser!=null) {
                txt_profile_jumlah_nasabah.setText(dataUser.getJumlahNasabah());
                txt_profile_nama.setText(dataUser.getNama());
                txt_profile_alamat.setText(dataUser.getAlamat());
                txt_profile_email.setText(dataUser.getEmail());
                txt_profile_nip.setText(dataUser.getNIP());
                txt_profile_no_telp.setText(dataUser.getNoTlpn());
                txt_profile_tgl_lahir.setText(dataUser.getTglLahir());
                txt_profile_point.setText(String.valueOf(dataUser.getPoint()));
                if (!dataUser.getAvatar().isEmpty()) {
                    Picasso.with(getContext())
                            .load(Constants.API_IMAGE_AVATAR_URL+dataUser.getAvatar())
                            .error(R.drawable.profile_user)
                            .into(user_profile_photo);
                }
            }
        }

        user_profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if((ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        || (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))

                {
                    ActivityCompat.requestPermissions
                            (getActivity(), new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            },MY_PERMISSIONS_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE);
                }
                id_img = user_profile_photo;
                id_img.setTag("img_submit_location_1");
                startDialog();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logout();

            }
        });

        return view;
    }

    public  String httpPostFile(byte[] file, String url) throws Exception {
        System.out.println("Start Connection " + url);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("Content-Type", "application/octet-stream");

        httppost.setEntity(new ByteArrayEntity(file));

        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        String responseText = EntityUtils.toString(entity);

        System.out.println("Fetch Data");
        System.out.println(responseText);

        return responseText;
    }

    public void initData(){
        DataUser dataUser;
        dataUser = DependencyInjection.Get(ISqliteRepository.class).getDetailUser();
        txt_profile_jumlah_nasabah.setText(dataUser.getJumlahNasabah());
        //txt_profile_nama.setText(dataUser.getNama());
        txt_profile_alamat.setText(dataUser.getAlamat());
        txt_profile_email.setText(dataUser.getEmail());
        txt_profile_nip.setText(dataUser.getNIP());
        txt_profile_no_telp.setText(dataUser.getNoTlpn());
        txt_profile_tgl_lahir.setText(dataUser.getTglLahir());
        txt_profile_point.setText(String.valueOf(dataUser.getPoint()));
    }

    public void logout() {

        Log.d(TAG, "Logout");
        DependencyInjection.Get(ISessionRepository.class).logoutUser();
        DependencyInjection.Get(ISqliteRepository.class).resetDatabase();
        Intent intent = new Intent(getContext(),LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }
    @Override public void onDestroyView() {
        super.onDestroyView();

        //unbinder.unbind();
    }

    private class AddImageTask extends AsyncTask<Void, Void, Void> {
        //  ProgressDialog dialog;
        public String dataResult = "";
        public Bitmap bPhoto;
        String poiId = "";
        File file ;
        public AddImageTask(File URI)
        {

            this.file = URI;
        }
        public AddImageTask(String poiId) {
            this.poiId = poiId;
        }
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
           /* dialog = ProgressDialog.show(activity, "", "Insert Image", true,
                    false);
            dialog.show();*/
            progressDialog.show();
        }
        // Referensi : http://stackoverflow.com/questions/17388240/posting-image-file-to-wcf-rest-service-from-android
        /*
        Erorr dan Bug :
        1. Upload Avatar via galerry bisa
        2. Upload Avatar Via Camera bisa tapi masih ada BUG (harus 2x tekan tombol upload)

        3. Upload Gambar Nasabah via Gallery --> Blom Bisa
        4. Upload Gambar Nasabah via Camera --> Blum Bisa
         */


        @Override
        protected Void doInBackground(Void... params) {

            try {
                byte[] streamByteArray;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                try {
                    FileInputStream fis = new FileInputStream(this.file);
                    bPhoto = BitmapFactory.decodeStream(fis);
                } catch (Exception e) { }
                if (bPhoto != null) {
                    bPhoto.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    bPhoto.recycle();
                    streamByteArray = stream.toByteArray();
                    stream.close();
                    stream = null;
                    String id = DependencyInjection.Get(ISessionRepository.class).getId();
                    String token = DependencyInjection.Get(ISessionRepository.class).getToken();
                    dataResult = httpPostFile(
                            streamByteArray,
                            Constants.API_SAVE_AVATAR_USER + token + "/"+id);
                            //"http://bni.yapyek.com/servicesreveral/upload_image_nasabah/"+token+"/"+id);
                    //"http://bni.yapyek.com/servicesreveral/upload_image_avatar/"+token+"/"+id);
                    Log.d("response",dataResult);
                    JSONObject dataUser = null, status, result=null;
                    result = new JSONObject(dataResult);
                    int point;
                    try {
                        status = result.getJSONObject("status");
                        if (status.getInt("code") == 200) {
                            dataUser = result.getJSONObject("obj");
                        } else {
                            Toast.makeText(getActivity(), status.getString("description"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (dataUser != null) {
                        try {
                            String image = dataUser.getString("image");

                            DependencyInjection.Get(ISqliteRepository.class).updateAvatarUser(image, DependencyInjection.Get(ISessionRepository.class).getUsername());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Data tidak tersedia !", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());

            }
            Log.d(TAG, "onSuccessObject: sukses mereun");

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if (dataResult != "") {
                try {
                    JSONObject jo = new JSONObject(dataResult);
                    JSONObject status = jo.getJSONObject("status");

                    Log.d(TAG, "onSuccessObject: startup onPostExecute");
                    Log.d(TAG, "Data Response Code  =" + status.getInt("code"));
                } catch (Exception e) {

                }
            } else
            {
                Log.d(TAG, "Dataresult: kosong");

            }
            progressDialog.dismiss();
        }
    }

    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = null;

                        pictureActionIntent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(
                                pictureActionIntent,
                                GALLERY_PICTURE);

                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment
                                .getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(f));

                        startActivityForResult(intent,
                                CAMERA_REQUEST);

                    }
                });
        myAlertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        bitmap = null;
        selectedImagePath = null;

        if (resultCode == getActivity().RESULT_OK && requestCode == CAMERA_REQUEST) {

            File f = new File(Environment.getExternalStorageDirectory()
                    .toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }

            if (!f.exists()) {

                Toast.makeText(getActivity(),

                        "Error while capturing image", Toast.LENGTH_LONG)

                        .show();

                return;
            }

            try {

                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

                bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);

                int rotate = 0;
                try {
                    ExifInterface exif = new ExifInterface(f.getAbsolutePath());
                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotate = 270;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotate = 90;
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);

                user_profile_photo.setImageBitmap(bitmap);
                if (f.getAbsolutePath() != null) {

                    txt_image_path.setText(f.getAbsolutePath());
                }
                //storeImageTosdCard(bitmap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (resultCode == getActivity().RESULT_OK && requestCode == GALLERY_PICTURE) {
            if (data != null) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                selectedImagePath = c.getString(columnIndex);
                c.close();

                if (selectedImagePath != null) {

                    txt_image_path.setText(selectedImagePath);
                }

                bitmap = BitmapFactory.decodeFile(selectedImagePath); // load
                // preview image
                bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                user_profile_photo.setImageBitmap(bitmap);

            } else {
                Toast.makeText(getActivity(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }
        File myFile = new File(txt_image_path.getText().toString());
        //upload(myFile);
        (new AddImageTask(myFile)).execute();
    }

}