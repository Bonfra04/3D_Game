package gameEngine.biomes;

import gameEngine.terrains.WorldGenerator;

public class PlainGenerator extends WorldGenerator {

	public PlainGenerator(int gridX, int gridZ, long seed) {
		super(gridX, gridZ, 128 / 2, seed, 70, 5, 0.1f);

		super.setBiome(Biome.PLAIN);
		
		super.setHasWater(false);
	}

	@Override
	public float generateHeight(int x, int z) {
		float total = super.generateHeight(x, z);

		if (total < 0)
			total = -total;

		return total;
	}

}
