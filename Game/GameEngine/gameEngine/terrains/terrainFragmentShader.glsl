#version 150

in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibility;
in vec4 shadowCoords;

out vec4 out_Color;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;
uniform sampler2D shadowMap;

uniform vec3 lightColour[4];
uniform vec3 attenuation[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

uniform float mapSize;

uniform vec4 biomeBlend;

const int pcfCount = 5;
const float totalTexels = (pcfCount * 2.0 + 1.0) * (pcfCount * 2.0 + 1.0);

void main(void) {

	float texelSize = 1.0 / mapSize;
	float total = 0.0;

	for (int x = -pcfCount; x <= pcfCount; x++) {
		for (int y = -pcfCount; y <= pcfCount; y++) {
			float objectNearestLigh = texture(shadowMap,
					shadowCoords.xy + vec2(x, y) * texelSize).r;
			if (shadowCoords.z > objectNearestLigh + 0.002) {
				total += 1.0;
			}
		}
	}

	total /= totalTexels;

	float lightFactor = 1.0 - (total * shadowCoords.w);

	vec4 blendMapColour = texture(blendMap, pass_textureCoordinates);

	float backTextureAmount = 1
			- (blendMapColour.r + blendMapColour.g + blendMapColour.b);
	vec2 tiledCoords = pass_textureCoordinates * 40.0;
	vec4 backgroundTextureColour = texture(backgroundTexture, tiledCoords)
			* backTextureAmount;
	vec4 rTextureColour = texture(rTexture, tiledCoords) * blendMapColour.r;
	vec4 gTextureColour = texture(gTexture, tiledCoords) * blendMapColour.g;
	vec4 bTextureColour = texture(bTexture, tiledCoords) * blendMapColour.b;

	vec4 totalColour = backgroundTextureColour + rTextureColour + gTextureColour
			+ bTextureColour;

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectortoCamera = normalize(toCameraVector);

	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);

	for (int i = 0; i < 4; i++) {
		float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance)
				+ (attenuation[i].z * distance * distance);
		vec3 unitLightVector = normalize(toLightVector[i]);
		float nDotl = dot(unitNormal, unitLightVector);
		float brightness = max(nDotl, 0.0);
		vec3 lighrDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lighrDirection, unitNormal);
		float specularFactor = dot(reflectedLightDirection, unitVectortoCamera);
		specularFactor = max(specularFactor, 0.0);
		float damperFactor = pow(specularFactor, shineDamper);
		totalDiffuse = totalDiffuse + (brightness * lightColour[i]) / attFactor;
		totalSpecular = totalSpecular
				+ (specularFactor * reflectivity * lightColour[i]) / attFactor;
	}
	totalDiffuse = max(totalDiffuse * lightFactor, 0.4);

	out_Color = vec4(totalDiffuse, 1.0) * totalColour
			+ vec4(totalSpecular, 1.0);

	out_Color += biomeBlend;

	out_Color = mix(vec4(skyColour, 1.0), out_Color, visibility);
}
