#version 150

in vec2 position;

in mat4 modelViewMatrix;
in vec4 textOffsets;
in float blendFactor;

out vec2 textureCoords1;
out vec2 textureCoords2;
out float blend;

uniform mat4 projectionMatrix;
uniform float numberOfRows;

void main(void) {

	vec2 textureCoords = position + vec2(0.5, 0.5);
	textureCoords.y = 1.0 - textureCoords.y;
	textureCoords /= numberOfRows;
	textureCoords1 = textureCoords + textOffsets.xy;
	textureCoords2 = textureCoords + textOffsets.zw;
	blend = blendFactor;

	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);

}