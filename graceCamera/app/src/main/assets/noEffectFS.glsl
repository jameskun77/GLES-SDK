#version 300 es
precision mediump float;

uniform sampler2D uTextureUnit;

in vec2 oTextureCoordinates;

out vec4 FragColor;

 void main()
 {
    FragColor = texture(uTextureUnit, oTextureCoordinates);
 }

