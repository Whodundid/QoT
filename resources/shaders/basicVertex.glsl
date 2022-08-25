#version 460 core
		
in vec3 position;
in vec2 tex;
//in vec3 normal;
		
out vec4 pass_color;
out vec2 pass_tex;
//out vec3 surfaceNormal;
//out vec3 toLightVector;
		
uniform mat4 transform;
uniform mat4 projection;
uniform mat4 view;
//uniform vec3 lightPos;
		
void main(void) {
	vec4 worldPos = transform * vec4(position, 1.0);
	
	gl_Position = projection * view * worldPos;
	pass_color = vec4(1.0, 0.0, 0.0, 1.0);
	pass_tex = tex;
		
	//surfaceNormal = (transform * vec4(normal, 0.0)).xyz;
	//toLightVector = lightPos - worldPos.xyz;
}
