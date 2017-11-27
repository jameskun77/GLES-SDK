package com.example.texturecombined.Program;

import android.content.Context;

import com.example.texturecombined.Util.ShaderHelper;
import com.example.texturecombined.Util.TextResourceReader;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glUseProgram;
import static com.example.texturecombined.Util.LoggerConfig.checkGlError;

/**
 * Created by 123 on 2017/11/25.
 */

public class ShaderProgram {

    //attribute
    protected static final String aPosition = "aPos";

    //Attribute
    private final int aPositionLocation;

    protected final int mProgram;

    public ShaderProgram(Context context, String vertexPath, String fragmentPath){
        mProgram = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context,vertexPath),
                TextResourceReader.readTextFileFromResource(context,fragmentPath));

        aPositionLocation = glGetAttribLocation(mProgram,aPosition);
        checkGlError("glGetAttribLocation");
    }

    public void useProgram(){
        glUseProgram(mProgram);
        checkGlError("useProgram");
    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }

}
