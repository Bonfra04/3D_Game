package gameEngine.biomes;

import gameEngine.terrains.WorldGenerator;

public class MountainGenerator extends WorldGenerator {

	public MountainGenerator(int gridX, int gridZ, long seed) {
		super(gridX, gridZ, 128 / 2, seed, 500.0f, 5, 0.1f);
		
		super.setBiome(Biome.PLAIN);
	}
	
	@Override
	public float generateHeight(int x, int z) {
		float total = super.generateHeight(x, z);
		
		if(total <= 1)
			total = 1.0f;
		
		return total;
	}

}
