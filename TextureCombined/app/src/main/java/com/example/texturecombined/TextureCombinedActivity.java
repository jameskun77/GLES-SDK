package com.example.texturecombined;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by 123 on 2017/11/26.
 */

public class TextureCombinedActivity extends Activity {

    private GLSurfaceView mGLSurfaceView;
    private TextureCombinedRender mRectangleRender;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        mGLSurfaceView = new GLSurfaceView(this);

        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        int supportEsVersion = configurationInfo.reqGlEsVersion;
        final boolean supportEs2 = supportEsVersion >= 0x20000;

        if(supportEs2){
            mGLSurfaceView.setEGLContextClientVersion(2);
            //glSurfaceView.setEGLConfigChooser(8,8,8,8,16,0);

            mRectangleRender = new TextureCombinedRender(this);

            mGLSurfaceView.setRenderer(mRectangleRender);
        }
        else{
            Toast.makeText(this,"This device does not support OpenGL ES 2.0.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mGLSurfaceView.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mGLSurfaceView.onResume();
    }
}
