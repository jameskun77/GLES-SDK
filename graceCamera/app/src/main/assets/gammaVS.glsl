uniform mat4 uMVPMatrix;
attribute vec4 aPosition;
attribute vec2 aTextureCoordinate;

varying vec2 vTextureCoordinates;

void main()
{
    vTextureCoordinates = aTextureCoordinate;
    gl_Position = uMVPMatrix * aPosition;
}