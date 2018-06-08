package gameEngine.biomes;

import org.lwjgl.util.vector.Vector4f;

public enum Biome {
	UNKNOWN, PLAIN, HILL, MOUNTAIN, BEACH, SEA;
	
	public Vector4f getBlend() {
		if (this == Biome.PLAIN)
		return new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);
		if (this == Biome.MOUNTAIN)
			return new Vector4f(-0.051f, 0.0f, -0.051f, 0.0f);
		
		return new Vector4f(-1.0f, -1.0f, -1.0f, 0.0f);
	}
}
