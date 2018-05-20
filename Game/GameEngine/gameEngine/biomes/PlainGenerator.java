package gameEngine.biomes;

import gameEngine.terrains.WorldGenerator;

public class PlainGenerator extends WorldGenerator {

	public PlainGenerator(int gridX, int gridZ, long seed) {
		super(gridX, gridZ, 128, seed, 70.0f, 5, 0.1f);

		super.setBiome(Biome.PLAIN);
	}

	@Override
	public float generateHeight(int x, int z) {
		float total = super.generateHeight(x, z);

		if (total <= 1)
			total = 1.0f;

		return total;
	}

}
