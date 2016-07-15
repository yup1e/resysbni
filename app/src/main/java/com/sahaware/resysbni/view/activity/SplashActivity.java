package com.sahaware.resysbni.view.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sahaware.resysbni.R;
import com.sahaware.resysbni.helper.DependencyInjection;
import com.sahaware.resysbni.repository.ISessionRepository;
import com.sahaware.resysbni.util.UtilityImageByte;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.ButterKnife;
import butterknife.BindView;


/**
 * A login screen that offers login via email/password.
 */
public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView progressBar;
    @BindView(R.id.tv_splash_status)
    TextView tv_splash_status;
    private UtilityImageByte util;
    private static int SPLASH_TIME_OUT = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        PackageInfo pInfo = null;

        util = new UtilityImageByte();
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                try {
                    //db.addContact(new NasabahEntity("3515040611910003", "Noor Shakato Eka Dhana", "Cikutra Baru", "08114160666", "Warung", "3 Tahun", "KUR", "Rp. 12.000.000", "Rumah", "Kantor Cabang Padalarang",util.getBytes(((R.drawable.camera).getDrawable()).getBitmap()), [234234], "29-04-2016", "Open"));
                    // After 3 seconds redirect to another intent

                    Boolean loggedIn = DependencyInjection.Get(ISessionRepository.class).isLoggedIn();
                    if(loggedIn == true){
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        // Closing all the Activities
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // Add new Flag to start new Activity
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        // Closing all the Activities
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // Add new Flag to start new Activity
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        }, SPLASH_TIME_OUT);


    }


}

