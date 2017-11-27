package com.example.airhockey.objects;

import com.example.airhockey.data.VertexArray;
import com.example.airhockey.programs.ColorShaderProgram;
import com.example.airhockey.objects.ObjectBuilder.DrawCommand;
import com.example.airhockey.objects.ObjectBuilder.GeneratedData;
import com.example.airhockey.util.Geometry.Point;

import java.util.List;


/**
 * Created by Jameskun on 2017/10/26.
 */

public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius;
    public final float height;

    private final VertexArray vertexArray;
    private final List<DrawCommand> drawList;

    public Mallet(float radius, float height, int numPointsAroundMallet) {
        GeneratedData generatedData = ObjectBuilder.createMallet(new Point(0f,
                0f, 0f), radius, height, numPointsAroundMallet);

        this.radius = radius;
        this.height = height;

        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }
    public void bindData(ColorShaderProgram colorProgram) {
        vertexArray.setVertexAttribPointer(0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0);
    }
    public void draw() {
        for (DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }
}
