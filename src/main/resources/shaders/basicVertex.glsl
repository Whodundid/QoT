#version 450 core

layout(location = 0) in vec3 in_position;
layout(location = 1) in vec4 in_color;
layout(location = 2) in vec2 in_texCoord;
layout(location = 3) in float in_texIndex;

uniform mat4 u_projection;
uniform mat4 u_view;
uniform mat4 u_transform;
uniform vec2 u_playerPos;

out vec4 pass_color;
out vec2 pass_texCoord;
out float pass_texIndex;

out float debugX;
out float debugY;
out float debugZ;

void main(void) {
	gl_Position = u_projection * u_view * vec4(in_position, 1.0);
	
	debugX = gl_Position.x;
	debugY = gl_Position.y;
	debugZ = gl_Position.z;
	
	pass_color = in_color;
	pass_texCoord = in_texCoord;
	pass_texIndex = in_texIndex;
}
