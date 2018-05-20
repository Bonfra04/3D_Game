package gameEngine.biomes;

import java.util.HashMap;
import java.util.Map;

public class BiomesResources {

	public static final Map<float[], Biome> BIOME_POSITION = new HashMap<float[], Biome>();

	public static void setBiome(float x, float z, Biome biome) {
		BIOME_POSITION.put(new float[] {x, z}, biome);
	}

	public static Biome getBiome(float x, float z) {
		return BIOME_POSITION.get(new float[] {x, z});
	}
}
