#version 300 es
precision mediump float;

in vec2 oTextureCoordinates;

uniform float softSkin;
uniform float sharpness;
uniform float whiteness;

uniform sampler2D uTextureUnit0;
uniform sampler2D uTextureUnit1;

out vec4 FragColor;

void main()
{
    vec4 blured = texture(uTextureUnit0, oTextureCoordinates);
    vec4 oral = texture(uTextureUnit1, oTextureCoordinates);
    vec4 curve = log(oral * (softSkin - 1.0) + 1.0) / log(softSkin);

    float G = oral.g;
    float G1 = 1.0 - blured.g;
    G1 = (min(1.0, max(0.0, ((G)+2.0*(G1)-1.0))));
    float G2 = mix(G, G1, 0.5);

    G2 = ((G2) <= 0.5 ? (pow(G2, 2.0) * 2.0) : (1.0 - pow((1.0 - G2), 2.0) * 2.0));
    G2 = ((G2) <= 0.5 ? (pow(G2, 2.0) * 2.0) : (1.0 - pow((1.0 - G2), 2.0) * 2.0));
    G2 = ((G2) <= 0.5 ? (pow(G2, 2.0) * 2.0) : (1.0 - pow((1.0 - G2), 2.0) * 2.0));

    float skinVal = blured.a;
    vec4 res = mix(curve, oral, G2);
    res = vec4(mix(oral.rgb, res.rgb, skinVal), 1.0);

    //
    // USM.
    //
    res = clamp(res + sharpness * (res - vec4(blured.rgb, 1.0)), 0.0, 1.0);

    //
    // Contrast light enhance.
    //
    vec3 contrast_adjusted = max(vec3(0.0, 0.0, 0.0), (res.rgb - 0.031372) * 1.0324);
    vec3 ones = vec3(1.0, 1.0, 1.0);
    vec3 zeros = vec3(0.0, 0.0, 0.0);
    float w = whiteness < 0.0 ? 1.0 - skinVal : skinVal;
    vec3 screen_weight_multiplier = vec3(0.45, 0.39, 0.39) * (whiteness * w);
    vec3 source_multiplier = vec3(8.0, 8.0, 8.0) * contrast_adjusted;
    vec3 screen_weight = (ones - max(ones - source_multiplier, zeros)) * screen_weight_multiplier;
    vec3 minus_src = ones - contrast_adjusted;
    vec3 minus_src_square = minus_src * minus_src;
    res = vec4(mix(res.rgb, ones - minus_src_square, screen_weight), 1.0);
    // return float4(blured.a, blured.a,blured.a, 1.0);

    FragColor = res;
}