package com.example.rendertexture;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by 123 on 2017/11/19.
 */

public class FBOActivity extends Activity {
    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);

        //check if the system support es2.0
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        int supportEsVersion = configurationInfo.reqGlEsVersion;
        final boolean supportEs2 = supportEsVersion >= 0x20000;

        if(supportEs2){
            glSurfaceView.setEGLContextClientVersion(2);
            //glSurfaceView.setEGLConfigChooser(8,8,8,8,16,0);

            glSurfaceView.setRenderer(new FBORender(this));
            rendererSet = true;
        }
        else{
            Toast.makeText(this,"This device does not support OpenGL ES 2.0.",
                    Toast.LENGTH_LONG).show();
            return;
        }
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
