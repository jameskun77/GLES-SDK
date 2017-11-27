package com.example.texturecombined.Program;


import android.content.Context;

import com.example.texturecombined.Util.ShaderHelper;
import com.example.texturecombined.Util.TextResourceReader;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE1;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static com.example.texturecombined.Util.LoggerConfig.checkGlError;

/**
 * Created by 123 on 2017/11/26.
 */

public class RectangleProgram {

    //uniform
    private final int mTexture1Location;
    private final int mTexture2Location;
    private final int mMVPMtrixLocation;

    protected final int mProgram;

    public RectangleProgram(Context context,String vertexPath,String fragmentPath){

        mProgram = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context,vertexPath),
                TextResourceReader.readTextFileFromResource(context,fragmentPath));

        mTexture1Location = glGetUniformLocation(mProgram,"texture1");
        checkGlError("glGetUniformLocation texture1");

        mTexture2Location = glGetUniformLocation(mProgram,"texture2");
        checkGlError("glGetUniformLocation texture2");

        mMVPMtrixLocation = glGetUniformLocation(mProgram,"uMVPMatrix");
        checkGlError("glGetUniformLocation mMVPMatrix");
    }

    public void useProgram(){
        glUseProgram(mProgram);
        checkGlError("useProgram");
    }

    public void setUniform(float[] matrix,int textureId1,int textureId2){
        glUniformMatrix4fv(mMVPMtrixLocation, 1, false, matrix, 0);

        glActiveTexture(GL_TEXTURE0);
        checkGlError("glActiveTexture texture1");
        glBindTexture(GL_TEXTURE_2D, textureId1);
        checkGlError("glBindTexture texture1");
        glUniform1i(mTexture1Location, 0);
        checkGlError("glUniform1i texture1");

        glActiveTexture(GL_TEXTURE1);
        checkGlError("glActiveTexture texture2");
        glBindTexture(GL_TEXTURE_2D, textureId2);
        checkGlError("glBindTexture texture2");
        glUniform1i(mTexture2Location, 1);
        checkGlError("glUniform1i texture2");
    }
}
