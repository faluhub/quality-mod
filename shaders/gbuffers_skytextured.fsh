#version 120
//Sun & moon


uniform sampler2D texture;

varying vec4 color;
varying vec4 texcoord;


void main()
{
	gl_FragData[0] = texture2D(texture, texcoord.st) * color;
}