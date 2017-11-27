package com.example.texturecombined.Data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.example.texturecombined.Util.LoggerConfig.checkGlError;

/**
 * Created by 123 on 2017/11/26.
 */

public class Rectangle {

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;

    static final int COORDS_PER_VERTEX = 5;
    static float squareCoords[] = {
            -1.0f,  1.0f, 0.0f, 0.0f,1.0f,
            -1.0f, -1.0f, 0.0f, 0.0f,0.0f,
             1.0f, -1.0f, 0.0f, 1.0f,0.0f,
             1.0f,  1.0f, 0.0f, 1.0f,1.0f};

    private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 };

    private final int vertexStride = COORDS_PER_VERTEX * 4;

    public Rectangle(){

        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }

    public void draw(){

        vertexBuffer.position(0);
        glVertexAttribPointer(0,3, GL_FLOAT,false,vertexStride,vertexBuffer);
        checkGlError("glEnableVertexAttribArray position");

        glEnableVertexAttribArray(0);
        checkGlError("glEnableVertexAttribArray position");

        vertexBuffer.position(3);
        glVertexAttribPointer(1,2, GL_FLOAT,false,vertexStride,vertexBuffer);
        checkGlError("glEnableVertexAttribArray uv");

        glEnableVertexAttribArray(1);
        checkGlError("glEnableVertexAttribArray uv");

        glDrawElements(GL_TRIANGLES, drawOrder.length, GL_UNSIGNED_SHORT, drawListBuffer);
        checkGlError("glDrawElements");
    }
}
