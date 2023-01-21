#version 460 core

layout (location = 0) out vec4 out_color;

in vec4 pass_color;
in vec2 pass_texCoord;

uniform sampler2D modelTexture;

void main(void) {
	//color = texture(modelTexture, pass_tex);
	out_color = pass_color;
}
