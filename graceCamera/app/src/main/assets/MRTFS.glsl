#version 300 es
precision mediump float;

layout (location = 0) out vec4 gammaColor;
layout (location = 1) out vec4 blackWhiteColor;

uniform sampler2D uTextureUnit;
uniform float uGamma;
in vec2 oTextureCoordinates;

void main()
{
     vec4 textureColor = texture(uTextureUnit, oTextureCoordinates);
     gammaColor = vec4(pow(textureColor.rgb, vec3(uGamma)), textureColor.w);

     float fGrayColor = (0.3*textureColor.r + 0.59*textureColor.g + 0.11*textureColor.b);
     blackWhiteColor = vec4(fGrayColor, fGrayColor, fGrayColor, 1.0);
}
