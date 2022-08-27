#version 460 core

in vec4 pass_color;
in vec2 pass_tex;
//in vec3 surfaceNormal;
//in vec3 toLightVector;

out vec4 out_Color;

uniform sampler2D modelTexture;
uniform int isTexture;
//uniform vec3 lightColor;

void main(void) {
	vec4 color = vec4(1, 1, 1, 1);
	
	if (isTexture == 1) {
		color = texture(modelTexture, pass_tex);
	}
	else {
		color = pass_color;
	}
	
	//vec3 unitNormal = normalize(surfaceNormal);
	//vec3 unitLightVector = normalize(toLightVector);
	
	//float nDot1 = dot(unitNormal, unitLightVector);
	//float brightness = max(nDot1, 1);
	//vec3 diffuse = brightness * lightColor;
	
	//out_Color = vec4(unitLightVector, 1.0) * color;
	out_Color = color;
}
