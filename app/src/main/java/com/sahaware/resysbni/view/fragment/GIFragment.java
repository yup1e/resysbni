package com.sahaware.resysbni.view.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sahaware.resysbni.R;
import com.sahaware.resysbni.util.Constants;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.entity.InputStreamEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;
import dmax.dialog.SpotsDialog;


public class GIFragment extends Fragment {
    TextView txt_image_path;
    FloatingActionButton fab;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private Intent pictureActionIntent = null;
    Bitmap bitmap;
    final Context context = getActivity();
    private static final String TAG = "UploadImage";
    String selectedImagePath;
    private SpotsDialog progressDialog;
    private ImageView img_coba;
    private SharedPreferences pref_login;
    private SharedPreferences.Editor editor_login;
    int MY_PERMISSIONS_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE;
    public GIFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_upload_image, container, false);
        ButterKnife.bind(view);
        LinearLayout toolbar = (LinearLayout) view.findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        pref_login = this.getActivity().getSharedPreferences(Constants.PREF_LOGIN, Constants.PRIVATE_MODE);
        editor_login = pref_login.edit();
        progressDialog = new SpotsDialog(getActivity(), "Mengunggah Gambar…");
        // Inflate the layout for this fragment
        img_coba=(ImageView)view.findViewById(R.id.img_coba);

        img_coba.setOnClickListener(new View.OnClickListener() {
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
                startDialog();
            }
            });
        txt_image_path = (TextView)view.findViewById(R.id.txt_image_path);
        fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                File myFile = new File(txt_image_path.getText().toString());
                //upload(myFile);
                (new AddImageTask(myFile)).execute();
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
                    String id = pref_login.getString(Constants.KEY_ID, null);
                    String id_nasabah = "15";
                    String token = pref_login.getString(Constants.KEY_TOKEN, null);
                    dataResult = httpPostFile(
                            streamByteArray,
                            "http://bni.yapyek.com/servicesreveral/upload_image_nasabah/"+token+"/"+id);
                            //"http://bni.yapyek.com/servicesreveral/upload_image_avatar/"+token+"/"+id);
                    Log.d("response",dataResult);
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
            progressDialog.hide();
        }
    }

    /*public void upload(File URI){
        Bitmap bPhoto = null;
        final int DEFAULT_TIMEOUT = 20 * 1000;
        RequestParams params = new RequestParams();
        InputStreamEntity reqEntity = null;
        ByteArrayOutputStream stream;
        try {
            byte[] streamByteArray = new byte[0];
            stream = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(URI);
            bPhoto = BitmapFactory.decodeStream(fis);

            if (bPhoto != null) {
                bPhoto.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                bPhoto.recycle();
            }
                streamByteArray = stream.toByteArray();
                stream.close();
                String id = pref_login.getString(Constants.KEY_ID, null);
                String id_nasabah = "15";
                String token = pref_login.getString(Constants.KEY_TOKEN, null);

            reqEntity = new InputStreamEntity(stream, streamByteArray.length) ;
            reqEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/octet-stream"));
            reqEntity.setChunked(false);
            Log.d(TAG, "upload: >>>>>>>>>>>>>>>>>>>>>>>"+reqEntity.getContent());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String token = pref_login.getString(Constants.KEY_TOKEN, null);
        String id = pref_login.getString(Constants.KEY_ID, null);
        String url_login = "http://bni.yapyek.com/servicesreveral/upload_image_nasabah/"+token+"/"+id;
        Log.d(TAG, "URL: " + url_login);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT);
        client.setConnectTimeout(DEFAULT_TIMEOUT);
        client.setResponseTimeout(DEFAULT_TIMEOUT);
        client.post(context, url_login, reqEntity, "application/json", new JsonHttpResponseHandler() {
        private ProgressDialog pDialog;

            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Log.d(TAG, "onSuccessObject: "+String.valueOf(statusCode));
                Log.d(TAG, "onSuccessObject: "+headers);
                Log.d(TAG, "onSuccessObject: "+response);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.String responseString) {
                Log.d(TAG, "onSuccessString: "+String.valueOf(statusCode));
                Log.d(TAG, "onSuccessString: "+headers);
                Log.d(TAG, "onSuccessString: "+responseString);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.Throwable throwable, org.json.JSONArray errorResponse){
                Log.d(TAG, "onFailureArray: "+String.valueOf(statusCode));
                Log.d(TAG, "onFailureArray: "+headers);
                Log.d(TAG, "onFailureArray: "+throwable);
                Log.d(TAG, "onFailureArray: "+errorResponse);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.String responseString, java.lang.Throwable throwable){
                progressDialog.dismiss();
                Log.d(TAG, "onFailureString: "+String.valueOf(statusCode));
                Log.d(TAG, "onFailureString: "+headers);
                Log.d(TAG, "onFailureString: "+throwable);
                Log.d(TAG, "onFailureString: "+responseString);
                Toast.makeText(getActivity(), "Error Code : "+ String.valueOf(statusCode) + "\n Error : " + throwable, Toast.LENGTH_LONG).show();
            }


            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, java.lang.Throwable throwable, org.json.JSONObject errorResponse){
                Log.d(TAG, "onFailureObject: "+String.valueOf(statusCode));
                Log.d(TAG, "onFailureObject: "+headers);
                Log.d(TAG, "onFailureObject: "+throwable);
                Log.d(TAG, "onFailureObject: "+errorResponse);
            }
            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }*/
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



                img_coba.setImageBitmap(bitmap);
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



                img_coba.setImageBitmap(bitmap);

            } else {
                Toast.makeText(getActivity(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }
}