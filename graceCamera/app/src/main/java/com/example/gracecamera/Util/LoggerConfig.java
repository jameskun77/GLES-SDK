package com.example.gracecamera.Util;

import android.util.Log;

import static android.opengl.GLES20.GL_NO_ERROR;
import static android.opengl.GLES20.glGetError;

/**
 * Created by 123 on 2017/11/19.
 */

public class LoggerConfig {
    public static final boolean ON = true;

    private static final String TAG = "checkGLError";

    public static void checkGlError(String glOperation) {
        int error;
        while ((error = glGetError()) != GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            //throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
}
