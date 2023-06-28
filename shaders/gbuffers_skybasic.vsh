#version 120
//Upper & lower parts of the sky


varying vec4 color;


void main()
{
	vec4 position = gl_ModelViewMatrix * gl_Vertex;
	
	gl_Position = gl_ProjectionMatrix * position;
	
	gl_FogFragCoord = sqrt(position.x * position.x + position.y * position.y + position.z * position.z);
	
	
	color = gl_Color;
}