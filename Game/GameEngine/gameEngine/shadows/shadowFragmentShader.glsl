#version 150

in vec2 textrueCoords;

out vec4 out_colour;

uniform sampler2D modelTexture;

void main(void) {

	float alpha = texture(modelTexture, textrueCoords).a;
	if (alpha < 0.5) {
		discard;
	}

	out_colour = vec4(1.0);

}
