#version 450 core

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec4 in_color;
layout (location = 2) in vec2 in_texCoord;

uniform mat4 u_projection;
uniform mat4 u_model;
uniform mat4 u_transform;

out vec4 pass_color;
out vec2 pass_texCoord;
		
void main(void) {
	gl_Position = u_projection * vec4(in_position, 1.0);
	pass_color = in_color;
	pass_texCoord = in_texCoord;
}
