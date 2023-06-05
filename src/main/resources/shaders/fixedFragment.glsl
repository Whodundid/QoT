#version 460 core

layout (location = 0) out vec4 out_color;

in vec4 pass_color;
in vec2 pass_texCoord;
in float pass_texIndex;

uniform sampler2D texSamplers[16];

void main(void) {
	int index = int(pass_texIndex);
	
	// render the color if index if zero
	if (index == 0) {
		out_color = pass_color;	
	}
	else {
		//offset indexes by 1 to account for array start position
		index = index - 1;
		//multiply texture color by passed color for texture lighting
		out_color = pass_color * texture(texSamplers[index], pass_texCoord);
	}
}