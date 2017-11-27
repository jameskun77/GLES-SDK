precision mediump float;

uniform sampler2D uTextureUnit;
uniform float uGamma;

varying vec2 vTextureCoordinates;

void main()
{
    vec4 textureColor = texture2D(uTextureUnit, vTextureCoordinates);
    gl_FragColor = vec4(pow(textureColor.rgb, vec3(uGamma)), textureColor.w);
}