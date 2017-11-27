package com.example.camera2openglespreview;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.Log;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

/**
 * Created by Jameskun on 2017/11/17.
 */

public class Camera2Render implements GLSurfaceView.Renderer{

    private static final String TAG = "Render";

    private Context mContext;
    Camera2GLSurfaceView mCamera2GLSurfaceView;
    CameraV2 mCamera;

    private boolean bIsPreviewStarted = false;
    private int mTextureId;
    private SurfaceTexture mSurfaceTexture;
    private float[] mTransformMatrix = new float[16];

    private EffectEngine mEffectEngine;

    public void init(Camera2GLSurfaceView surfaceView,CameraV2 camera,Context context){
        mContext = context;
        mCamera2GLSurfaceView = surfaceView;
        mCamera = camera;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 1.0f, 0.0f, 0.0f);

        mTextureId = Utils.CreateTextureObject();
        mEffectEngine = new EffectEngine(mTextureId, mContext);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (mSurfaceTexture != null) {
            mSurfaceTexture.updateTexImage();
            mSurfaceTexture.getTransformMatrix(mTransformMatrix);
        }

        if (!bIsPreviewStarted) {
            bIsPreviewStarted = initSurfaceTexture();
            bIsPreviewStarted = true;
            return;
        }

        glClear(GL_COLOR_BUFFER_BIT);
        mEffectEngine.drawTexture(mTransformMatrix);
    }

    public boolean initSurfaceTexture() {
        if (mCamera == null || mCamera2GLSurfaceView == null) {
            Log.i(TAG, "mCamera or mGLSurfaceView is null!");
            return false;
        }
        mSurfaceTexture = new SurfaceTexture(mTextureId);
        mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                mCamera2GLSurfaceView.requestRender();
            }
        });
        mCamera.setPreviewTexture(mSurfaceTexture);
        mCamera.startPreview();
        return true;
    }
}
