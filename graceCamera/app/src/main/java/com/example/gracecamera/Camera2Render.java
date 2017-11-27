package com.example.gracecamera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.gracecamera.Data.EffectFilter;
import com.example.gracecamera.Data.PreviewQuad;
import com.example.gracecamera.Program.GammaProgram;
import com.example.gracecamera.Program.GammaProgramES3;
import com.example.gracecamera.Program.MRTProgram;
import com.example.gracecamera.Program.PreviewProgram;
import com.example.gracecamera.Program.SharpProgram;
import com.example.gracecamera.Program.SkinProgram;
import com.example.gracecamera.Program.TestMRTProgram;
import com.example.gracecamera.Program.WhiteBlackProgram;
import com.example.gracecamera.Program.WhiteProgram;
import com.example.gracecamera.Util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_COLOR_ATTACHMENT0;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FRAMEBUFFER;
import static android.opengl.GLES20.GL_FRAMEBUFFER_COMPLETE;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_RGB;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT_5_6_5;
import static android.opengl.GLES20.glBindFramebuffer;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glCheckFramebufferStatus;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glFramebufferTexture2D;
import static android.opengl.GLES20.glGenFramebuffers;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glTexImage2D;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLES20.glViewport;
import static android.opengl.GLES30.GL_COLOR_ATTACHMENT1;
import static android.opengl.GLES30.glDrawBuffers;

/**
 * Created by 123 on 2017/11/19.
 */

public class Camera2Render implements GLSurfaceView.Renderer {

    private static final String TAG = "Camera2Render";

    private Context mContex;
    GLSurfaceView mCamera2View;
    CameraV2 mCameraV2;

    private boolean bIsPreviewStarted = false;
    private int mTextureId;
    private SurfaceTexture mSurfaceTexture;

    private float[]  mTransformMatrix = new float[16];
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private PreviewQuad mPreviewQuad;
    private PreviewProgram mPreviewProgram;

    private EffectFilter mEffectFilter;
    private WhiteBlackProgram mWhiteBlackProgram;
    private GammaProgram mGammaProgram;
    private GammaProgramES3 mGammaProgramES3;
    private MRTProgram mMRTProgram;
    private TestMRTProgram mTestMRTProgram;
    private WhiteProgram mWhiteProgram;
    private SkinProgram mSkinProgram;
    private SharpProgram mSharpProgram;

    //frame buffer
    private int[] mFrameBuffer = new int[1];
    private int[] mFrameTexture = new int[1];

    //frame buffer1
    private int[] mFrameBuffer1 = new int[1];
    private int[] mFrameTexture1 = new int[1];

    //multiple render target
    private int[] mFrameBufferMRT = new int[1];
    private int[] mFrameTextureMRT = new int[2];

    //set var from activity seekbar
    private float m_whiteLevel = -0.5f;
    private float m_softSkinLevel = 3.51f;
    private float m_sharpenLevel = 0.35f;

    private int textureSurfaceWidth;
    private int textureSurfaceHeight;

    public void init(GLSurfaceView camera2View,CameraV2 cameraV2,Context context){
        mContex = context;
        mCamera2View = camera2View;
        mCameraV2 = cameraV2;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        mTextureId = TextureHelper.CreateTextureObject();
        mPreviewQuad = new PreviewQuad();
        mPreviewProgram = new PreviewProgram(mContex,"previewVertexShader.glsl","previewFragmentShader.glsl");

        mEffectFilter = new EffectFilter();
        //mWhiteBlackProgram = new WhiteBlackProgram(mContex,"blackWhiteEffectVS.glsl","blackWhiteEffectFS.glsl");
        //mGammaProgram = new GammaProgram(mContex,"gammaVS.glsl","gammaFS.glsl");

        //mGammaProgramES3 = new GammaProgramES3(mContex,"gammaVSes3.0.glsl","gammaFSes3.0.glsl");
        //mMRTProgram = new MRTProgram(mContex,"MRTVS.glsl","MRTFS.glsl");
        mTestMRTProgram = new TestMRTProgram(mContex,"noEffectVS.glsl","noEffectFS.glsl");

        mSkinProgram = new SkinProgram(mContex,"skinBeautyVS.glsl","skinBeautyFS.glsl");
        mWhiteProgram = new WhiteProgram(mContex,"whiteBeautyVS.glsl","whiteBeautyFS.glsl");
        mSharpProgram = new SharpProgram(mContex,"sharpVS.glsl","sharpFS.glsl");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

        //frame buffer config
        configFrameBuffer(width,height);
        configFrameBuffer1(width,height);
        configFrameBufferMRT(width,height);

        textureSurfaceWidth = width;
        textureSurfaceHeight = height;
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        glBindFramebuffer(GL_FRAMEBUFFER, mFrameBuffer[0]);

        if (mSurfaceTexture != null){
            mSurfaceTexture.updateTexImage();
            mSurfaceTexture.getTransformMatrix(mTransformMatrix);
        }

        if(!bIsPreviewStarted){
            bIsPreviewStarted = initSurfaceTexture();
        }

        glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        drawPreview();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        //draw MRT
        glBindFramebuffer(GL_FRAMEBUFFER,mFrameBufferMRT[0]);
        glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        //drawMRT();
        skinBeauty();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        glBindFramebuffer(GL_FRAMEBUFFER,mFrameBuffer1[0]);
        glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        whiteBeauty();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);


        //draw to window
        glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        sharpBeauty();
        //drawWhiteBlack();
        //drawGammaES3();
        //drawTestMRT();
    }

    private void drawPreview(){
        mPreviewProgram.useProgram();
        mPreviewProgram.setUniforms(mTransformMatrix,mTextureId);
        mPreviewQuad.draw(mPreviewProgram);
    }

    private void drawWhiteBlack(){
        mWhiteBlackProgram.useProgram();
        mWhiteBlackProgram.setUniforms(mMVPMatrix,mFrameTexture[0]);
        mEffectFilter.draw(mWhiteBlackProgram);
    }

    private void drawGamma(){
        mGammaProgram.useProgram();
        mGammaProgram.setUniforms(mMVPMatrix,mFrameTexture[0],2.58f);
        mEffectFilter.draw(mGammaProgram);
    }

    private void drawMRT(){
        mMRTProgram.useProgram();
        mMRTProgram.setUniforms(mMVPMatrix,mFrameTexture[0],2.58f);
        mEffectFilter.drawES3();
    }

    private void skinBeauty(){
        mSkinProgram.useProgram();
        mSkinProgram.setUniforms(mMVPMatrix,mFrameTexture[0],textureSurfaceWidth,textureSurfaceHeight);
        mEffectFilter.drawES3();
    }

    private void whiteBeauty(){
        mWhiteProgram.useProgram();
        mWhiteProgram.setUniforms(mMVPMatrix,mFrameTextureMRT[0],mFrameTextureMRT[1],mFrameTexture[0],textureSurfaceWidth,textureSurfaceHeight);
        mEffectFilter.drawES3();
    }

    private void sharpBeauty()
    {
        mSharpProgram.useProgram();
        mSharpProgram.setUniforms(mMVPMatrix,mFrameTexture1[0],mFrameTexture[0],m_softSkinLevel,m_sharpenLevel,m_whiteLevel);
        mEffectFilter.drawES3();
    }

    private void drawTestMRT(){
        mTestMRTProgram.useProgram();
        mTestMRTProgram.setUniforms(mFrameTexture[0]);
        mEffectFilter.drawES3();
    }

    private void drawGammaES3(){
        mGammaProgramES3.useProgram();
        mGammaProgramES3.setUniforms(mMVPMatrix,mFrameTexture[0],2.58f);
        mEffectFilter.drawES3();
    }

    private boolean initSurfaceTexture() {
        if (mCameraV2 == null || mCamera2View == null) {
            Log.e(TAG, "mCamera or mGLSurfaceView is null!");
            return false;
        }

        mSurfaceTexture = new SurfaceTexture(mTextureId);
        mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                mCamera2View.requestRender();
            }
        });
        mCameraV2.setPreviewTexture(mSurfaceTexture);
        mCameraV2.startPreview();
        return true;
    }

    private void configFrameBuffer(int w,int h){
        glGenFramebuffers(1,mFrameBuffer,0);
        glBindFramebuffer(GL_FRAMEBUFFER, mFrameBuffer[0]);

        glGenTextures(1,mFrameTexture,0);
        glBindTexture(GL_TEXTURE_2D, mFrameTexture[0]);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w, h, 0, GL_RGB, GL_UNSIGNED_SHORT_5_6_5, null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, mFrameTexture[0], 0);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
        {
            Log.e(TAG,"Frame buffer config fail.");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private void configFrameBuffer1(int w,int h){
        glGenFramebuffers(1,mFrameBuffer1,0);
        glBindFramebuffer(GL_FRAMEBUFFER, mFrameBuffer1[0]);

        glGenTextures(1,mFrameTexture1,0);
        glBindTexture(GL_TEXTURE_2D, mFrameTexture1[0]);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w, h, 0, GL_RGB, GL_UNSIGNED_SHORT_5_6_5, null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, mFrameTexture1[0], 0);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
        {
            Log.e(TAG,"Frame buffer1 config fail.");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private void configFrameBufferMRT(int w,int h){
        glGenFramebuffers(1,mFrameBufferMRT,0);
        glBindFramebuffer(GL_FRAMEBUFFER,mFrameBufferMRT[0]);

        glGenTextures(2,mFrameTextureMRT,0);
        for (int i = 0; i < 2; i++){

            glBindTexture(GL_TEXTURE_2D, mFrameTextureMRT[i]);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w, h, 0, GL_RGB, GL_UNSIGNED_SHORT_5_6_5, null);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + i, GL_TEXTURE_2D, mFrameTextureMRT[i], 0);
        }

        int[] DrawBuffers = new int[2];
        DrawBuffers[0] = GL_COLOR_ATTACHMENT0;
        DrawBuffers[1] = GL_COLOR_ATTACHMENT1;
        glDrawBuffers(2,DrawBuffers,0);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
        {
            Log.e(TAG,"Frame buffer MRT config fail.");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void setWhiteParam(float val){
        m_whiteLevel = val;
    }

    public void setYuHuaParam(float val){
        m_softSkinLevel = val;
    }

    public void setSharpParam(float val){
        m_sharpenLevel = val;
    }
}
