package com.example.airhockey.objects;

import com.example.airhockey.data.VertexArray;
import com.example.airhockey.objects.ObjectBuilder.DrawCommand;
import com.example.airhockey.objects.ObjectBuilder.GeneratedData;
import com.example.airhockey.programs.ColorShaderProgram;
import com.example.airhockey.util.Geometry.Cylinder;
import com.example.airhockey.util.Geometry.Circle;
import com.example.airhockey.util.Geometry.Point;

import java.util.List;

/**
 * Created by Jameskun on 2017/10/26.
 */

public class Puck {
    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius, height;

    private final VertexArray vertexArray;
    private final List<DrawCommand> drawList;

    public Puck(float radius, float height, int numPointsAroundPuck) {
        GeneratedData generatedData = ObjectBuilder.createPuck(new Cylinder(
                new Point(0f, 0f, 0f), radius, height), numPointsAroundPuck);
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
