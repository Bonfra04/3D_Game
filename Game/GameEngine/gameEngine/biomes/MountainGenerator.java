package gameEngine.biomes;

import gameEngine.terrains.WorldGenerator;

public class MountainGenerator extends WorldGenerator {

	public MountainGenerator(int gridX, int gridZ, long seed) {
		super(gridX, gridZ, 128, seed, 500.0f, 5, 0.1f);

		super.setBiome(Biome.MOUNTAIN);

		super.setHasWater(false);
	}

	@Override
	public float generateHeight(int x, int z) {
		float total = super.generateHeight(x, z);

		if (total < 0)
			total = 0;

		return total;
	}

}
