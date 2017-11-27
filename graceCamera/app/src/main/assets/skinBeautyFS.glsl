#version 300 es
precision mediump float;

layout (location = 0) out vec4 color0;
layout (location = 1) out vec4 color1;

in vec2 oTextureCoordinates;

uniform sampler2D uTextureUnit;
uniform float width;
uniform float height;
uniform float steps;
uniform float radius;

void main()
{
    float uBoxHeight = radius / height;
    float uYStep = steps / height;
    vec4 sum = vec4(0.0, 0.0, 0.0, 0.0);
    vec4 sumII = vec4(0.0, 0.0, 0.0, 0.0);
    float halfH = uBoxHeight / 2.0;
    int num = 0;
    for (float j = -halfH; j < halfH; j += uYStep)
    {
        vec4 pixel = texture(uTextureUnit, oTextureCoordinates + vec2(0, j));

        pixel.a = 1.0;
        float skinVal = 0.0;
        float cb = dot(vec4(-37.74, -74.205, 111.945, 128.0), pixel);
        float cr = dot(vec4(111.945, -93.84, -18.105, 128.0), pixel);
        float maxSkinVal = 110.0;
        float skinValueCb = (maxSkinVal - abs(cb - 102.0)) / maxSkinVal;
        float skinValueCr = (maxSkinVal - abs(cr - 153.0)) / maxSkinVal;
        pixel.a = pow((skinValueCb + skinValueCr) * 0.5, 2.0);

        sum += pixel;
        sumII += pixel * pixel;
        num ++;
    }
    color0 = sum / float(num);
    color1 = sum / float(num);

}