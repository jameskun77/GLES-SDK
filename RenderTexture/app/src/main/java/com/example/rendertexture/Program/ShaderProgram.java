package com.example.rendertexture.Program;

import android.content.Context;

import com.example.rendertexture.Util.ShaderHelper;
import com.example.rendertexture.Util.TextResourceReader;

import static android.opengl.GLES20.glUseProgram;

/**
 * Created by 123 on 2017/11/19.
 */

public class ShaderProgram {

    //uniform
    protected static final String uMVPMatrix = "uMVPMatrix";
    protected static final String uTextureUnit = "uTextureUnit";

    //attribute
    protected static final String aPosition = "aPosition";
    protected static final String aTextureCoordinates = "aTextureCoordinates";

    protected final int mProgram;

    public ShaderProgram(Context context,String vertexPath,String fragmentPath){
        mProgram = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context,vertexPath),
                TextResourceReader.readTextFileFromResource(context,fragmentPath));
    }

    public void useProgram(){
        glUseProgram(mProgram);
    }
}
