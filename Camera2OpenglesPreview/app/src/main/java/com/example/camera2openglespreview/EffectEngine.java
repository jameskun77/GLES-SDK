package com.example.camera2openglespreview;

import android.content.Context;
import android.opengl.GLES11Ext;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetError;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.example.camera2openglespreview.Utils.checkGlError;

/**
 * Created by Jameskun on 2017/11/17.
 */

public class EffectEngine {

    private static EffectEngine effectEngine = null;

    private Context mContext;
    private FloatBuffer mBuffer;
    private int mTextureId;
    private int mVertexShader;
    private int mFragmentShader;

    private int mShaderProgram;

    private int aPositionLocation;
    private int aTextureCoordLocation;
    private int uTextureMatrixLocation;
    private int uTextureSamplerLocation;

    public EffectEngine(int textureId,Context context){
        mContext = context;
        mTextureId = textureId;
        mBuffer = createBuffer(vertexData);
        mVertexShader = loadShader(GL_VERTEX_SHADER,Utils.readShader(context,"base_vertex_shader.glsl"));
        mFragmentShader = loadShader(GL_FRAGMENT_SHADER,Utils.readShader(context,"base_fragment_shader.glsl"));
        mShaderProgram = linkProgram(mVertexShader,mFragmentShader);
    }

    private static final float[] vertexData = {
             1f,  1f, 1f, 1f,
            -1f,  1f, 0f, 1f,
            -1f, -1f, 0f, 0f,
             1f,  1f, 1f, 1f,
            -1f, -1f, 0f, 0f,
             1f, -1f, 1f, 0f
    };

    public static final String POSITION_ATTRIBUTE = "aPosition";
    public static final String TEXTURE_COORD_ATTRIBUTE = "aTextureCoordinate";
    public static final String TEXTURE_MATRIX_UNIFORM = "uTextureMatrix";
    public static final String TEXTURE_SAMPLER_UNIFORM = "uTextureSampler";

    public FloatBuffer createBuffer(float[] vertexData) {
        FloatBuffer buffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        buffer.put(vertexData, 0, vertexData.length).position(0);
        return buffer;
    }

    public int loadShader(int type, String shaderSource) {
        int shader = glCreateShader(type);
        if (shader == 0) {
            throw new RuntimeException("Create Shader Failed!" + glGetError());
        }
        glShaderSource(shader, shaderSource);
        glCompileShader(shader);
        return shader;
    }

    public int linkProgram(int verShader, int fragShader) {
        int program = glCreateProgram();
        if (program == 0) {
            throw new RuntimeException("Create Program Failed!" + glGetError());
        }
        glAttachShader(program, verShader);
        glAttachShader(program, fragShader);
        glLinkProgram(program);

        glUseProgram(program);
        return program;
    }

    public void drawTexture(float[] transformMatrix) {
        aPositionLocation = glGetAttribLocation(mShaderProgram, EffectEngine.POSITION_ATTRIBUTE);
        aTextureCoordLocation = glGetAttribLocation(mShaderProgram, EffectEngine.TEXTURE_COORD_ATTRIBUTE);
        uTextureMatrixLocation = glGetUniformLocation(mShaderProgram, EffectEngine.TEXTURE_MATRIX_UNIFORM);
        uTextureSamplerLocation = glGetUniformLocation(mShaderProgram, EffectEngine.TEXTURE_SAMPLER_UNIFORM);

        glActiveTexture(GL_TEXTURE_EXTERNAL_OES);
        glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureId);
        glUniform1i(uTextureSamplerLocation, 0);
        glUniformMatrix4fv(uTextureMatrixLocation, 1, false, transformMatrix, 0);

        if (mBuffer != null) {
            mBuffer.position(0);
            glEnableVertexAttribArray(aPositionLocation);
            glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 16, mBuffer);

            mBuffer.position(2);
            glEnableVertexAttribArray(aTextureCoordLocation);
            glVertexAttribPointer(aTextureCoordLocation, 2, GL_FLOAT, false, 16, mBuffer);

            glDrawArrays(GL_TRIANGLES, 0, 6);
        }
    }
}
