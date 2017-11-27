package com.example.rendertexture;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.rendertexture.Data.Square;
import com.example.rendertexture.Data.Triangle;
import com.example.rendertexture.Program.SquareProgram;
import com.example.rendertexture.Program.TriangleProgram;
import com.example.rendertexture.Util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
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
import static android.opengl.GLES20.glGenFramebuffers;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glTexImage2D;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLES20.glViewport;

/**
 * Created by 123 on 2017/11/19.
 */

public class FBORender implements GLSurfaceView.Renderer {

    private static final String TAG = "FBORenderer";

    private final Context mActivityContext;

    private Triangle mTriangle;
    private TriangleProgram mTriangleProgram;

    private Square mSquare;
    private SquareProgram mSquareProgram;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private int squareTexture;

    private int[] mFrameBuffer = new int[1];
    private int[] mTexture = new int[1];

    public FBORender(Context context){
        mActivityContext  = context;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        mTriangle = new Triangle();
        mTriangleProgram = new TriangleProgram(mActivityContext,"triangleVertexShader.glsl","triangleFragmentShader.glsl");

        mSquare = new Square();
        mSquareProgram = new SquareProgram(mActivityContext,"squareVertexShader.glsl","squareFragmentShader.glsl");

        squareTexture = TextureHelper.loadTexture(mActivityContext,R.drawable.left);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {

        glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

        //frame buffer config
        glGenFramebuffers(1,mFrameBuffer,0);
        glBindFramebuffer(GL_FRAMEBUFFER, mFrameBuffer[0]);

        glGenTextures(1,mTexture,0);
        glBindTexture(GL_TEXTURE_2D, mTexture[0]);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_SHORT_5_6_5, null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        GLES20.glFramebufferTexture2D(GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, mTexture[0], 0);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
        {
            Log.e(TAG,"Frame buffer config fail.");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    @Override
    public void onDrawFrame(GL10 unused) {

        glBindFramebuffer(GL_FRAMEBUFFER, mFrameBuffer[0]);
        glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        drawTriangle();

        //draw to window
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        drawSquare();
    }

    private void drawTriangle()
    {
        mTriangleProgram.useProgram();
        mTriangleProgram.setUniform(mMVPMatrix);
        mTriangle.draw(mTriangleProgram);
    }

    private void drawSquare()
    {
        mSquareProgram.useProgram();
        mSquareProgram.setUniforms(mMVPMatrix,mTexture[0]);
        mSquare.draw(mSquareProgram);
    }
}
