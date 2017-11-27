#version 300 es
precision mediump float;

uniform sampler2D uTextureUnit;
uniform float uGamma;

in vec2 oTextureCoordinates;

out vec4 FragColor;

 void main()
 {
    vec4 textureColor = texture(uTextureUnit, oTextureCoordinates);
    FragColor = vec4(pow(textureColor.rgb, vec3(uGamma)), textureColor.w);
 }

