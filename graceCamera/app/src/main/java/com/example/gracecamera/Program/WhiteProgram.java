package com.example.gracecamera.Program;

import android.content.Context;

import com.example.gracecamera.Util.ShaderHelper;
import com.example.gracecamera.Util.TextResourceReader;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE1;
import static android.opengl.GLES20.GL_TEXTURE2;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;

/**
 * Created by Jameskun on 2017/11/23.
 */

public class WhiteProgram {
    private final int uMVPMatrixLocation;
    private final int uTextureUnit0Location;
    private final int uTextureUnit1Location;
    private final int uTextureUnit2Location;

    private final int widthLocation;
    private final int heightLocation;
    private final int stepLocation;
    private final int radiusLocation;
    private final int epsLocation;

    protected final int mProgram;

    public WhiteProgram(Context context,String vertexPath,String fragmentPath){
        mProgram = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context,vertexPath),
                TextResourceReader.readTextFileFromResource(context,fragmentPath));

        uMVPMatrixLocation = glGetUniformLocation(mProgram,"uMVPMatrix");
        uTextureUnit0Location = glGetUniformLocation(mProgram,"uTextureUnit0");
        uTextureUnit1Location = glGetUniformLocation(mProgram,"uTextureUnit1");
        uTextureUnit2Location = glGetUniformLocation(mProgram,"uTextureUnit2");

        widthLocation = glGetUniformLocation(mProgram,"width");;
        heightLocation = glGetUniformLocation(mProgram,"height");;
        stepLocation = glGetUniformLocation(mProgram,"steps");;
        radiusLocation = glGetUniformLocation(mProgram,"radius");;
        epsLocation = glGetUniformLocation(mProgram,"eps");;
    }

    public void setUniforms(float[] matrix, int texture0,int texture1,int texture2,int width,int height) {
        glUniformMatrix4fv(uMVPMatrixLocation, 1, false, matrix, 0);

        glUniform1f(widthLocation, (float) width);
        glUniform1f(heightLocation, (float) height);
        glUniform1f(stepLocation, (float) 1.0);
        glUniform1f(radiusLocation, (float) 10.0);
        glUniform1f(epsLocation, (float) 0.02);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture0);
        glUniform1i(uTextureUnit0Location, 0);

        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, texture1);
        glUniform1i(uTextureUnit1Location, 1);

        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, texture2);
        glUniform1i(uTextureUnit2Location, 2);

    }

    public void useProgram(){
        glUseProgram(mProgram);
    }
}
