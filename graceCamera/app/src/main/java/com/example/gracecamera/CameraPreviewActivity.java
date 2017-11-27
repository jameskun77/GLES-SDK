package com.example.gracecamera;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

/**
 * Created by 123 on 2017/11/19.
 */

public class CameraPreviewActivity extends Activity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener  {

    private static final String TAG = "Activity";

    private GLSurfaceView mGLSurfaceView;
    private Camera2Render mCamera2Render;

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

        setContentView(R.layout.activity_main);
        mGLSurfaceView = (GLSurfaceView)findViewById(R.id.glsurfaceview) ;
        ((SeekBar) findViewById(R.id.seekBar1)).setOnSeekBarChangeListener(this);
        ((SeekBar) findViewById(R.id.seekBar2)).setOnSeekBarChangeListener(this);
        ((SeekBar) findViewById(R.id.seekBar3)).setOnSeekBarChangeListener(this);
        //findViewById(R.id.button_choose_filter).setOnClickListener(this);
        //findViewById(R.id.button_capture).setOnClickListener(this);

        //check system support es2.0
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        int supportEsVersion = configurationInfo.reqGlEsVersion;
        final boolean supportEs2 = supportEsVersion >= 0x20000;

        if(supportEs2){
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            mCamera = new CameraV2(this);
            mCamera.setupCamera(dm.widthPixels,dm.heightPixels);

            if(!mCamera.openCamera()){
                Log.e(TAG,"mCamera.openCamera fail");
                return;
            }

            mGLSurfaceView.setEGLContextClientVersion(2);
            mCamera2Render = new Camera2Render();
            mCamera2Render.init(mGLSurfaceView,mCamera,CameraPreviewActivity.this);

            mGLSurfaceView.setRenderer(mCamera2Render);
        }
        else {
            Toast.makeText(this,"This device does not support OpenGL ES 2.0.",
                    Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    protected void onPause(){
        super.onPause();
        mGLSurfaceView.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onProgressChanged(final  SeekBar seekBar,final int progress,
                                  final boolean fromUser){
        //Log.i(TAG,"onProgressChanged");
        switch (seekBar.getId()){
            case R.id.seekBar1:
            {
                Log.i(TAG,"bar1 onProgressChanged" + progress);
                float white = -1.0f + (progress + 19)/30.0f;
                mCamera2Render.setWhiteParam(white );
            }
            break;
            case R.id.seekBar2:
            {
                Log.i(TAG,"bar2 onProgressChanged" + progress);
                float yuhua = 1.01f + (progress + 19)/40.0f;
                mCamera2Render.setYuHuaParam(yuhua);
            }
            break;
            case R.id.seekBar3:
            {
                Log.i(TAG,"bar3 onProgressChanged" + progress);
                float sharp = 0.05f + (float)(progress + 19) / 100.0f * 0.60f;
                mCamera2Render.setSharpParam(sharp);
            }
            break;
        }
    }

    @Override
    public void onStartTrackingTouch(final SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(final SeekBar seekBar) {
    }

    @Override
    public void onClick(final View v){
//        switch (v.getId()){
//            case R.id.button_choose_filter:
//            {
//                Log.i(TAG,"button_choose_filter");
//            }
//            break;
//            case R.id.button_capture:
//            {
//                Log.i(TAG,"button_capture");
//            }
//            break;
//        }
    }
}
