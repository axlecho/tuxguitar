package com.axlecho.tuxguitar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.herac.tuxguitar.android.activity.TGActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.test_btn).setOnClickListener(this);
        verifyStoragePermissions(this);
    }

    private void testShowGtp(String filePath) {
        Uri gtpUri = Uri.parse("file://" + filePath);
        Intent intent = new Intent();
        intent.setData(gtpUri);

        Bundle bundle = new Bundle();
        bundle.putSerializable("title", "test");
        intent.putExtras(bundle);

        intent.setAction(Intent.ACTION_VIEW);
        intent.setClass(this, TGActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.test_btn) {
            testShowGtp(Environment.getExternalStorageDirectory() + File.separator + "test.gp5");
        }
    }


    public static void verifyStoragePermissions(AppCompatActivity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
