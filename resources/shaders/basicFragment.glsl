#version 460 core

in vec4 passColor;
in vec2 passTexCoord;

out vec4 outColor;

uniform sampler2D tex;
uniform int isTexture;

void main() {
	if (isTexture == 1) outColor = texture(tex, passTexCoord);
	else outColor = passColor;
}
