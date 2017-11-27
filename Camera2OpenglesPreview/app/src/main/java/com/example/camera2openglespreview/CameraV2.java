package com.example.camera2openglespreview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jameskun on 2017/11/17.
 */

public class CameraV2 {

    public static final String TAG = "CameraV2";

    private Activity mActivity;
    private CameraDevice mCameraDevice;
    private String mCameraId;
    private Size mPreviewSize;
    private HandlerThread mCameraThread;
    private Handler mCameraHandler;
    private SurfaceTexture mSurfaceTexture;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    private CaptureRequest mCaptureRequest;
    private CameraCaptureSession mCameraCaptureSession;

    public CameraV2(Activity activity) {
        mActivity = activity;
        startCameraThread();
    }

    public void startCameraThread(){
        mCameraThread = new HandlerThread("CameraThread");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());
    }

    public String setupCamera(int width,int height){
        CameraManager cameraManager = (CameraManager) mActivity.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String id : cameraManager.getCameraIdList()){
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(id);
                if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK)
                    continue;
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                mPreviewSize = getOptimalSize(map.getOutputSizes(SurfaceTexture.class),width,height);
                mCameraId = id;
                Log.i(TAG, "preview width = " + mPreviewSize.getWidth() + ", height = " + mPreviewSize.getHeight() + ", cameraId = " + mCameraId);
            }

        }catch (CameraAccessException e){
            Log.e(TAG,"Setup Camera Exception");
            e.printStackTrace();
        }
        return mCameraId;
    }

    private Size getOptimalSize(Size[] sizeMap, int width, int height) {
        List<Size> sizeList = new ArrayList<>();
        for (Size option : sizeMap) {
            if (width > height) {
                if (option.getWidth() > width && option.getHeight() > height) {
                    sizeList.add(option);
                }
            } else {
                if (option.getWidth() > height && option.getHeight() > width) {
                    sizeList.add(option);
                }
            }
        }
        if (sizeList.size() > 0) {
            return Collections.min(sizeList, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                }
            });
        }
        return sizeMap[1];
    }

    public boolean openCamera(){
        CameraManager cameraManager = (CameraManager) mActivity.getSystemService(Context.CAMERA_SERVICE);
        try{
            if(ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                return  false;
            cameraManager.openCamera(mCameraId,mStateCallback,mCameraHandler);
        }catch (CameraAccessException e){
            Log.e(TAG,"open Camera Exception");
            e.printStackTrace();
            return false;
        }
        return  true;
    }

    public CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback(){

        @Override
        public void onOpened(CameraDevice camera){
            Log.i(TAG,"CameraDevice.StateCallback onOpened ");
            mCameraDevice = camera;
        }

        @Override
        public void onDisconnected(CameraDevice camera){
            Log.i(TAG,"CameraDevice.StateCallback onDisconnected ");
            camera.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice camera,int error){
            Log.i(TAG,"CameraDevice.StateCallback onError ");
            camera.close();
            mCameraDevice = null;
        }
    };

    public void setPreviewTexture(SurfaceTexture surfaceTexture){
        mSurfaceTexture = surfaceTexture;
    }

    public void startPreview(){
        mSurfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(),mPreviewSize.getHeight());

        Surface surface = new Surface(mSurfaceTexture);
        try{
            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mCaptureRequestBuilder.addTarget(surface);
            mCameraDevice.createCaptureSession(Arrays.asList(surface),new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        mCaptureRequest = mCaptureRequestBuilder.build();
                        mCameraCaptureSession = session;
                        mCameraCaptureSession.setRepeatingRequest(mCaptureRequest, null, mCameraHandler);
                    } catch (CameraAccessException e) {
                        Log.e(TAG,"startPreview onConfigured Exception ");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                    Log.e(TAG,"startPreview onConfigureFailed Exception ");
                }
            },mCameraHandler);

        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }
}
