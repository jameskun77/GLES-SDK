package com.example.gracecamera.Program;

import android.content.Context;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by Jameskun on 2017/11/20.
 */

public class GammaProgram extends ShaderProgram {

    //uniform
    private final int uMVPMatrixLocation;
    private final int uTextureUnitLocation;
    private final int uGammaLocation;

    public GammaProgram(Context context,String vertexPath,String fragmentPath){
        super(context,vertexPath,fragmentPath);

        uMVPMatrixLocation = glGetUniformLocation(mProgram,uMVPMatrix);
        uTextureUnitLocation = glGetUniformLocation(mProgram,uTextureUnit);
        uGammaLocation = glGetUniformLocation(mProgram,uGamma);
    }

    public void setUniforms(float[] matrix, int textureId,float gammaVal) {
        glUniformMatrix4fv(uMVPMatrixLocation, 1, false, matrix, 0);

        glUniform1f(uGammaLocation, gammaVal);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glUniform1i(uTextureUnitLocation, 0);
    }
}
