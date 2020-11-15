#version 460 core

in vec2 passTextureCoord;

out vec4 outColor;

uniform sampler2D tex;

void main() {
	vec4 c = texture(tex, passTextureCoord);
	
	if (c.a < 0.1) {
		discard;
	}
	
	outColor = c;
	
}