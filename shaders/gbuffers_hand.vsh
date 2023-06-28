#version 120


varying vec4 color;
varying vec4 texcoord;
varying vec4 lmcoord;


void main()
{
	gl_Position = ftransform();
	
	
	texcoord = gl_TextureMatrix[0] * gl_MultiTexCoord0;
	
	color = gl_Color;
	
	lmcoord = gl_TextureMatrix[1] * gl_MultiTexCoord1;
}