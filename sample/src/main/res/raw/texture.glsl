
attribute vec4 a_position;
attribute vec2 a_TextureCoordinates;
uniform mat4 u_ModelMatrix;
uniform mat4 u_ProjectionMatrix;
uniform mat4 u_ViewMatrix;
varying vec2 v_TextureCoordinates;


void main() {
    v_TextureCoordinates = a_TextureCoordinates;
    gl_Position = u_ProjectionMatrix * u_ViewMatrix * u_ModelMatrix * a_position;
}
