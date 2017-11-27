package com.example.rendertexture.Data;

import com.example.rendertexture.Program.TriangleProgram;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by 123 on 2017/11/19.
 */

public class Triangle {

    private final FloatBuffer vertexBuffer;

    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {

             0.0f,  0.622008459f, 0.0f,
            -0.5f, -0.311004243f, 0.0f,
             0.5f, -0.311004243f, 0.0f
    };
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    public Triangle(){

        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);
    }

    public void draw(TriangleProgram triangleProgram){

        glVertexAttribPointer(triangleProgram.getPositionAttributeLocation(),COORDS_PER_VERTEX,
        GL_FLOAT,false,vertexStride,vertexBuffer);

        glDrawArrays(GL_TRIANGLES, 0, vertexCount);

    }
}
