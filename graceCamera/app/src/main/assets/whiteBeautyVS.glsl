#version 300 es

layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec2 aTextureCoordinate;

uniform mat4 uMVPMatrix;

out vec2 oTextureCoordinates;

void main()
{
    oTextureCoordinates = aTextureCoordinate;
    gl_Position = uMVPMatrix * vec4(aPosition,1.0);
}
