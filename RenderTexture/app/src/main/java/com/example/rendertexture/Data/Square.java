package com.example.rendertexture.Data;

import com.example.rendertexture.Program.SquareProgram;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by 123 on 2017/11/19.
 */

public class Square {

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;

    static final int COORDS_PER_VERTEX = 5;
    static float squareCoords[] = {
            -0.5f,  0.5f, 0.0f, 0.0f,1.0f,
            -0.5f, -0.5f, 0.0f, 0.0f,0.0f,
             0.5f, -0.5f, 0.0f, 1.0f,0.0f,
             0.5f,  0.5f, 0.0f, 1.0f,1.0f};

    private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 };

    private final int vertexStride = COORDS_PER_VERTEX * 4;

    public Square(){

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

    public void draw(SquareProgram squareProgram){

        vertexBuffer.position(0);
        glVertexAttribPointer(squareProgram.getPositionAttributeLocation(),3,
                GL_FLOAT,false,vertexStride,vertexBuffer);

        vertexBuffer.position(3);
        glVertexAttribPointer(squareProgram.getTexCoordAttributeLocation(),2,
                GL_FLOAT,false,vertexStride,vertexBuffer);

        glDrawElements(GL_TRIANGLES, drawOrder.length, GL_UNSIGNED_SHORT, drawListBuffer);

    }
}
