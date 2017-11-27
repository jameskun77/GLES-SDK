#version 300 es
precision mediump float;

in vec2 oTextureCoordinates;

uniform sampler2D uTextureUnit0;
uniform sampler2D uTextureUnit1;
uniform sampler2D uTextureUnit2;

uniform float width;
uniform float height;
uniform float steps;
uniform float radius;
uniform float eps;

out vec4 FragColor;

void main()
{
    float uBoxWidth = radius / width;
    float uXStep  = steps / width;
    vec4 sum = vec4(0.0, 0.0, 0.0, 0.0);
    vec4 sumII = vec4(0.0, 0.0, 0.0, 0.0);
    float halfW = uBoxWidth / 2.0;
    int num = 0;
    for (float i = -halfW; i < halfW; i += uXStep)
    {
        vec4 pixel = texture(uTextureUnit0, oTextureCoordinates + vec2(i, 0));
        vec4 pixel2 = texture(uTextureUnit1, oTextureCoordinates + vec2(i, 0));
        sum += pixel;
        sumII += pixel2;
        num ++;
    }

    vec4 oral = texture(uTextureUnit2, oTextureCoordinates);
    vec4 mean_I = sum / float(num);
    vec3 mean_II = vec3(sumII / float(num));
    vec3 var_I = mean_II - mean_I.rgb * mean_I.rgb;
    vec3 uA = var_I / (var_I + eps);
    vec3 uB = mean_I.rgb - uA * mean_I.rgb;
    vec3 res = uA * oral.rgb + uB;

    FragColor = vec4(res,mean_I.a);
}