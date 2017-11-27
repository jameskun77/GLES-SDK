package com.example.texturecombined;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.example.texturecombined.Data.Rectangle;
import com.example.texturecombined.Data.Triangle;
import com.example.texturecombined.Program.RectangleProgram;
import com.example.texturecombined.Program.TriangleProgram;
import com.example.texturecombined.Util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

/**
 * Created by 123 on 2017/11/26.
 */

public class TextureCombinedRender implements GLSurfaceView.Renderer {

    private static final String TAG = "DrawRectRender";

    private Triangle mTriangle;
    private TriangleProgram mTriangleProgram;

    private Rectangle mRectangle;
    private RectangleProgram mRectangleProgram;
    private int mRectTexture1;
    private int mRectTexture2;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private Context mContex;

    public TextureCombinedRender(Context context){
        mContex = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        mTriangle = new Triangle();
        mTriangleProgram = new TriangleProgram(mContex,"triangleES3_VS.glsl","triangleES3_FS.glsl");

        mRectangle = new Rectangle();
        mRectangleProgram = new RectangleProgram(mContex,"rectangleES3_VS.glsl","rectangleES3_FS.glsl");

        mRectTexture1 = TextureHelper.loadTexture(mContex,R.drawable.container);
        mRectTexture2 = TextureHelper.loadTexture(mContex,R.drawable.awesomeface);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        //drawTriangle();
        drawRect();
    }

    void drawTriangle(){
        mTriangleProgram.useProgram();
        mTriangle.draw(mTriangleProgram);
    }

    void drawRect(){
        mRectangleProgram.useProgram();
        mRectangleProgram.setUniform(mMVPMatrix,mRectTexture1,mRectTexture2);
        mRectangle.draw();
    }
}
