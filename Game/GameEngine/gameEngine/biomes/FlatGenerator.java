package gameEngine.biomes;

import gameEngine.terrains.WorldGenerator;

public class FlatGenerator extends WorldGenerator {

	public FlatGenerator(int gridX, int gridZ, long seed) {
		super(gridX, gridZ, 128, seed, 1, 1, 0);
	}

}
