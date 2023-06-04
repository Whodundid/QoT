#version 460 core

// https://stackoverflow.com/questions/33972699/how-to-make-2d-lighting-better-in-opengl

layout (location = 0) out vec4 out_color;

in vec4 pass_color;
in vec2 pass_texCoord;
in float pass_texIndex;

uniform int u_underground;
uniform vec2 u_playerPos;
uniform float u_lightDist;
uniform vec4 u_bezierVals;
uniform sampler2D texSamplers[16];

//============
// Prototypes
//============

float bezier4(float p1, float p2, float p3, float p4, float t);

//======
// Main
//======

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
		vec4 texture_color = texture(texSamplers[index], pass_texCoord);
		//out_color = pass_color * texture(texSamplers[index], pass_texCoord);
		
		// spicy lighting
		vec2 pixel = gl_FragCoord.xy;
		float dist = distance(u_playerPos, pixel);
		float max_dist = u_lightDist;
		float percent = clamp(1.0f - dist / max_dist, 0.0f, 1.0f);
		percent = bezier4(u_bezierVals.x, u_bezierVals.y, u_bezierVals.z, u_bezierVals.w, percent);
		
		out_color = texture_color * pass_color * vec4(percent, percent, percent, 1.0);
	}
}

//===========
// Functions
//===========

float bezier4(float p1, float p2, float p3, float p4, float t) {
    const float mum1 = 1.0f - t;
    const float mum13 = mum1 * mum1 * mum1;
    const float mu3 = t * t * t;
    return mum13 * p1 + 3 * t * mum1 * mum1 * p2 + 3 * t * t * mum1 * p3 + mu3 * p4;
}
