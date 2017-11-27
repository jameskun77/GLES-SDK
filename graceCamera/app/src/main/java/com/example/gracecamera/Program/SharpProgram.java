package com.example.gracecamera.Program;

import android.content.Context;

import com.example.gracecamera.Util.ShaderHelper;
import com.example.gracecamera.Util.TextResourceReader;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE1;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static com.example.gracecamera.Util.LoggerConfig.checkGlError;

/**
 * Created by 123 on 2017/11/22.
 */

public class SharpProgram {

    private final int uMVPMatrixLocation;
    private final int uTextureUnit0Location;
    private final int uTextureUnit1Location;

    private final int softSkinLocation;
    private final int sharpnessLocation;
    private final int whitenessLocation;

    protected final int mProgram;

    public SharpProgram(Context context, String vertexPath, String fragmentPath){
        mProgram = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context,vertexPath),
                TextResourceReader.readTextFileFromResource(context,fragmentPath));

        uMVPMatrixLocation = glGetUniformLocation(mProgram,"uMVPMatrix");
        uTextureUnit0Location = glGetUniformLocation(mProgram,"uTextureUnit0");
        uTextureUnit1Location = glGetUniformLocation(mProgram,"uTextureUnit1");

        softSkinLocation = glGetUniformLocation(mProgram,"softSkin");
        sharpnessLocation = glGetUniformLocation(mProgram,"sharpness");
        whitenessLocation = glGetUniformLocation(mProgram,"whiteness");
    }

    public void setUniforms(float[] matrix, int textureId1,int textureId2,float skin,float sharp,float white) {
        glUniformMatrix4fv(uMVPMatrixLocation, 1, false, matrix, 0);
        checkGlError("glUniformMatrix4fv(uMVPMatrixLocation, 1, false, matrix, 0);");

        glUniform1f(softSkinLocation, skin);
        checkGlError("glUniform1f(softSkinLocation, skin);");
        glUniform1f(sharpnessLocation, sharp);
        checkGlError(" glUniform1f(sharpnessLocation, sharp);");
        glUniform1f(whitenessLocation, white);
        checkGlError(" glUniform1f(whitenessLocation, white);");

        glActiveTexture(GL_TEXTURE0);
        checkGlError(" glActiveTexture(GL_TEXTURE0);");
        glBindTexture(GL_TEXTURE_2D, textureId1);
        checkGlError("   glBindTexture(GL_TEXTURE_2D, textureId1);");
        glUniform1i(uTextureUnit0Location, 0);
        checkGlError("     glUniform1i(uTextureUnit0Location, 0);");

        glActiveTexture(GL_TEXTURE1);
        checkGlError("glActiveTexture(GL_TEXTURE1);");
        glBindTexture(GL_TEXTURE_2D, textureId2);
        checkGlError(" glBindTexture(GL_TEXTURE_2D, textureId2);");
        glUniform1i(uTextureUnit1Location, 1);
        checkGlError("  glUniform1i(uTextureUnit1Location, 1);");
    }

    public void useProgram(){
        glUseProgram(mProgram);
        checkGlError("glUseProgram(mProgram)");
    }
}
