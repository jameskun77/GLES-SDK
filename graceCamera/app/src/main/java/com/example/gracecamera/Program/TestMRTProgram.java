package com.example.gracecamera.Program;

import android.content.Context;

import com.example.gracecamera.Util.ShaderHelper;
import com.example.gracecamera.Util.TextResourceReader;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUseProgram;

/**
 * Created by Jameskun on 2017/11/21.
 */

public class TestMRTProgram {

    private final int uTextureUnitLocation;

    protected final int mProgram;

    public TestMRTProgram(Context context, String vertexPath, String fragmentPath){
        mProgram = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context,vertexPath),
                TextResourceReader.readTextFileFromResource(context,fragmentPath));

        uTextureUnitLocation = glGetUniformLocation(mProgram,"uTextureUnit");
    }

    public void setUniforms(int textureId){

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glUniform1i(uTextureUnitLocation, 0);
    }

    public void useProgram(){
        glUseProgram(mProgram);
    }
}
