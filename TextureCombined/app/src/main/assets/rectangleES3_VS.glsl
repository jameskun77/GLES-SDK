#version 300 es

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;

uniform mat4 uMVPMatrix;

out vec2 TexCoord;

void main()
{
    gl_Position = uMVPMatrix * vec4(aPos,1.0);
    TexCoord = aTexCoord;
}