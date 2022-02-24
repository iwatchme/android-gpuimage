precision mediump float;
//uniform vec4 u_color;
varying vec2 v_TextureCoordinates;
uniform sampler2D u_TextureUnit;

void main() {
//    gl_FragColor = u_color;
    gl_FragColor = texture2D(u_TextureUnit,v_TextureCoordinates);
}
