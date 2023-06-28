#version 120


//#define WORLD_SPACE		//Implements the deformation in World Space. Uses Screen Space if not enabled.


uniform mat4 gbufferModelView;
uniform mat4 gbufferModelViewInverse;

uniform float frameTimeCounter;

varying vec4 color;


void main()
{
	vec4 position = gl_ModelViewMatrix * gl_Vertex;
	
	#ifdef WORLD_SPACE
		position = gbufferModelViewInverse * position;
	#endif
	
	float distance2D = position.x * position.x + position.z * position.z;
	
	position.y += 5.0 * sin(distance2D * sin((frameTimeCounter * 20 + 36000.0) / 143.0) / 1000.0);
	
	float y = position.y;
	float x = position.x;
	
	float om = sin(distance2D * sin((frameTimeCounter * 20.0 + 36000.0) / 256.0) / 5000.0) * sin((frameTimeCounter * 20.0 + 36000.0) / 200.0);
	
	position.y = x * sin(om) + y * cos(om);
	position.x = x * cos(om) - y * sin(om);
	
	
	#ifdef WORLD_SPACE
		position = gbufferModelView * position;
	#endif
	
	gl_Position = gl_ProjectionMatrix * position;
	
	
	gl_FogFragCoord = sqrt(position.x * position.x + position.y * position.y + position.z * position.z);
	
	
	color = gl_Color;
}