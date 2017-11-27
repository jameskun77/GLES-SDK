precision mediump float;

uniform sampler2D uTextureUnit;
varying vec2 vTextureCoordinates;

void main()
{
     vec4 vCameraColor = texture2D(uTextureUnit, vTextureCoordinates);
     float fGrayColor = (0.3*vCameraColor.r + 0.59*vCameraColor.g + 0.11*vCameraColor.b);
     gl_FragColor = vec4(fGrayColor, fGrayColor, fGrayColor, 1.0);
}