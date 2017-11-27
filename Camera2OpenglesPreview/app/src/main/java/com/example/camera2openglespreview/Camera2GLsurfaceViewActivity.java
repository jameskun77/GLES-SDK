package com.example.camera2openglespreview;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by Jameskun on 2017/11/17.
 */

public class Camera2GLsurfaceViewActivity extends Activity {

    private static final String TAG = "Activity";

    private Camera2GLSurfaceView mCamera2GLSurfaceView;
    private CameraV2 mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //开启相机权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 10);
        }

        mCamera2GLSurfaceView = new Camera2GLSurfaceView(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mCamera = new CameraV2(this);
        mCamera.setupCamera(dm.widthPixels,dm.heightPixels);

        if(!mCamera.openCamera()){
            Log.e(TAG,"mCamera.openCamera fail");
            return;
        }

        mCamera2GLSurfaceView.init(mCamera,Camera2GLsurfaceViewActivity.this);
        setContentView(mCamera2GLSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera2GLSurfaceView.onResume();
    }

    protected void onPause(){
        super.onPause();
        mCamera2GLSurfaceView.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
