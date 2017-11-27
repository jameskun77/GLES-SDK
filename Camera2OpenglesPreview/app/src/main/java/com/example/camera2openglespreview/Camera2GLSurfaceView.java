package com.example.camera2openglespreview;

import android.content.Context;
import android.opengl.GLSurfaceView;


/**
 * Created by Jameskun on 2017/11/17.
 */

public class Camera2GLSurfaceView extends GLSurfaceView {

    public static final String TAG = "Camera2GLSurfaceView";
    private Camera2Render mCamera2Render;

    public Camera2GLSurfaceView(Context context){
        super(context);
    }

    public void init(CameraV2 camera,Context context)
    {
        setEGLContextClientVersion(2);
        mCamera2Render = new Camera2Render();
        mCamera2Render.init(this,camera,context);

        setRenderer(mCamera2Render);
    }
}
