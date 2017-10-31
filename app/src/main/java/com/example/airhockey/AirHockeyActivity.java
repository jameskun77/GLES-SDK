package com.example.airhockey;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class AirHockeyActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_air_hockey);

        glSurfaceView = new GLSurfaceView(this);

        //check if the system support es2.0
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

        int supportEsVersion = configurationInfo.reqGlEsVersion;

        final boolean supportEs2 = supportEsVersion >= 0x20000;

        final AirHockeyRenderer airHockeyRenderer = new AirHockeyRenderer(this);

        if(supportEs2){
            glSurfaceView.setEGLContextClientVersion(2);
            //glSurfaceView.setEGLConfigChooser(8,8,8,8,16,0);

            glSurfaceView.setRenderer(airHockeyRenderer);
            rendererSet = true;
        }
        else{
            Toast.makeText(this,"This device does not support OpenGL ES 2.0.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event != null){
                    final float normalizedX =
                            (event.getX() / (float)v.getWidth()) * 2 - 1;
                    final float normalizedY =
                            (event.getY() / (float)v.getHeight()) * 2 - 1;

                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        glSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                airHockeyRenderer.handleTouchPress(
                                        normalizedX, normalizedY);
                            }
                        });
                    }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                        glSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                airHockeyRenderer.handleTouchDrag(
                                        normalizedX, normalizedY);
                            }
                        });
                    }
                    return true;
                }else {

                    return false;
                }
            }
        });

        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause(){
        super.onPause();

        if(rendererSet){
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        if(rendererSet){
            glSurfaceView.onResume();
        }
    }
}
