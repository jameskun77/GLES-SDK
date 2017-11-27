package com.example.gracecamera.Program;

import android.content.Context;

import com.example.gracecamera.Util.ShaderHelper;
import com.example.gracecamera.Util.TextResourceReader;

import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glUseProgram;

/**
 * Created by 123 on 2017/11/19.
 */

public class ShaderProgram {

    //uniform
    protected static final String uMVPMatrix = "uMVPMatrix";
    protected static final String uTextureSampler = "uTextureSampler";
    protected static final String uTextureMatrix = "uTextureMatrix";
    protected static final String uTextureUnit = "uTextureUnit";
    protected static final String uGamma = "uGamma";

    //attribute
    protected static final String aPosition = "aPosition";
    protected static final String aTextureCoordinates = "aTextureCoordinate";

    protected final int mProgram;

    //Attribute
    private final int aPositionLocation;
    private final int aTexCoordLocation;

    public ShaderProgram(Context context, String vertexPath, String fragmentPath){
        mProgram = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context,vertexPath),
                TextResourceReader.readTextFileFromResource(context,fragmentPath));


        aPositionLocation = glGetAttribLocation(mProgram,aPosition);
        glEnableVertexAttribArray(aPositionLocation);
        aTexCoordLocation = glGetAttribLocation(mProgram,aTextureCoordinates);
        glEnableVertexAttribArray(aTexCoordLocation);
    }

    public void useProgram(){
        glUseProgram(mProgram);
    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }

    public int getTexCoordAttributeLocation(){
        return aTexCoordLocation;
    }
}
