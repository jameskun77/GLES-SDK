package com.example.rendertexture.Program;

import android.content.Context;

import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by 123 on 2017/11/19.
 */

public class TriangleProgram extends ShaderProgram {
    //Attribute
    private final int aPositionLocation;

    //Uniform
    private final int uMVPMatrixLocation;


    public TriangleProgram(Context context,String vertexPath,String fragmentPath) {

        super(context,vertexPath,fragmentPath);

        aPositionLocation = glGetAttribLocation(mProgram,aPosition);
        glEnableVertexAttribArray(aPositionLocation);
        uMVPMatrixLocation = glGetUniformLocation(mProgram,uMVPMatrix);
    }

    public void setUniform(float[] matrix){
        glUniformMatrix4fv(uMVPMatrixLocation, 1, false, matrix, 0);
    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }

}
