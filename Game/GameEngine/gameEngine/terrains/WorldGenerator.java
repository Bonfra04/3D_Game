package gameEngine.terrains;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import game.Main;
import gameEngine.biomes.Biome;
import gameEngine.entities.Entity;
import gameEngine.textures.TerrainTexture;
import gameEngine.textures.TerrainTexturePack;
import gameEngine.water.WaterTile;

public abstract class WorldGenerator {

	protected final float AMPLITUDE;
	protected final int OCTAVES;
	protected final float ROUGHNESS;

	protected static final int DENSITY = 50;

	protected Random random;
	protected long seed;
	protected int xOffset = 0;
	protected int zOffset = 0;

	protected int gridX;
	protected int gridZ;

	protected int vertexCount;

	protected boolean hasWater = true;

	protected Biome biome = Biome.UNKNOWN;

	public static void generateWorldPortion(int gridX, int gridZ, long seed, WorldGenerator worldGenerator) {
		TerrainTexture backgroundTexture = new TerrainTexture(Main.LOADER.loadTexture("grassy3"));
		TerrainTexture rTexture = new TerrainTexture(Main.LOADER.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(Main.LOADER.loadTexture("grassflowers"));
		TerrainTexture bTexture = new TerrainTexture(Main.LOADER.loadTexture("path"));

		TerrainTexturePack terrainTexturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(Main.LOADER.loadTexture("blendMap"));

		Terrain terrain = new Terrain(gridX, gridZ, Main.LOADER, terrainTexturePack, blendMap, seed, worldGenerator);

		if (worldGenerator.hasWater)
			new WaterTile(gridX != 0 ? gridX * Terrain.SIZE + Terrain.SIZE / 2 : Terrain.SIZE - Terrain.SIZE / 2,
					gridZ != 0 ? gridZ * Terrain.SIZE + Terrain.SIZE / 2 : Terrain.SIZE - Terrain.SIZE / 2, 0);

		Map<Float, Float> coords = new HashMap<Float, Float>();
		Random random = new Random(seed);
		for (int i = 0; i < DENSITY; i++) {
			float x;
			float z;
			float y;
			while (true) {
				x = random.nextInt((int) (gridX * Terrain.SIZE + Terrain.SIZE));
				z = random.nextInt((int) (gridX * Terrain.SIZE + Terrain.SIZE));
				if (coords.get(x) == null & (x > gridX * Terrain.SIZE & x < gridX * Terrain.SIZE + Terrain.SIZE)
						& (z > gridZ * Terrain.SIZE & z < gridZ * Terrain.SIZE + Terrain.SIZE)) {
					y = terrain.getHeightOfTerrain(x, z);
					if (y > 0) {
						coords.put(x, z);
						break;
					} else
						continue;
				} else
					continue;
			}

			int r = random.nextInt();
			if (r % 4 == 0)
				new Entity("fern", "fern", 2, random.nextInt() % 4, Main.SCALE, new Vector3f(x, y, z),
						new Vector3f(0, 0, 0)).setTextureTrasparency(true);
			else if (r % 3 == 0)
				new Entity("toonRocks", "toonRocks", Main.SCALE, new Vector3f(x, y - 0.2f, z), new Vector3f(0, 0, 0));
			else
				new Entity("tree", "tree", Main.SCALE * 7, new Vector3f(x, y, z), new Vector3f(0, 0, 0));

		}
	}

	/**
	 * only works with POSITIVE gridX and gridZ values
	 */
	public WorldGenerator(int gridX, int gridZ, int vertexCount, long seed, float amplitude, int octaves,
			float roughness) {
		this.seed = seed;
		xOffset = gridX * (vertexCount - 1);
		zOffset = gridZ * (vertexCount - 1);

		random = new Random(seed);

		this.AMPLITUDE = amplitude;
		this.OCTAVES = octaves;
		this.ROUGHNESS = roughness;

		this.gridX = gridX;
		this.gridZ = gridZ;

		this.vertexCount = vertexCount;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void setBiome(Biome biome) {
		this.biome = biome;
	}

	public Biome getBiome() {
		return biome;
	}

	public float generateHeight(int x, int z) {
		float total = 0;
		float d = (float) Math.pow(2, OCTAVES - 1);
		for (int i = 0; i < OCTAVES; i++) {
			float freq = (float) (Math.pow(2, i) / d);
			float amp = (float) Math.pow(ROUGHNESS, i) * AMPLITUDE;
			total += getInterpolatedNoise((x + xOffset) * freq, (z + zOffset) * freq) * amp;
		}

		if (x % Terrain.SIZE == 0)
			if (Terrain.getBiome(x + 1, z) != this.getBiome() & Terrain.getBiome(x + 1, z) != null)
				if (Terrain.getHeight(x + 1, z) != null)
					total = Terrain.getHeight(x + 1, z);

		return total;
	}

	protected float getInterpolatedNoise(float x, float z) {
		int intX = (int) x;
		int intZ = (int) z;
		float fracX = x - intX;
		float fracZ = z - intZ;

		float v1 = getSmoothNoise(intX, intZ);
		float v2 = getSmoothNoise(intX + 1, intZ);
		float v3 = getSmoothNoise(intX, intZ + 1);
		float v4 = getSmoothNoise(intX + 1, intZ + 1);
		float i1 = interpolate(v1, v2, fracX);
		float i2 = interpolate(v3, v4, fracX);
		return interpolate(i1, i2, fracZ);
	}

	protected float interpolate(float a, float b, float blend) {
		double theta = blend * Math.PI;
		float f = (float) (1f - Math.cos(theta)) * 0.5f;
		return a * (1f - f) + b * f;
	}

	protected float getSmoothNoise(int x, int z) {
		float corners = (getNoise(x - 1, z - 1) + getNoise(x + 1, z - 1) + getNoise(x - 1, z + 1)
				+ getNoise(x + 1, z + 1)) / 16f;
		float sides = (getNoise(x - 1, z) + getNoise(x + 1, z) + getNoise(x, z - 1) + getNoise(x, z + 1)) / 8f;
		float center = getNoise(x, z) / 4f;
		return corners + sides + center;
	}

	protected float getNoise(int x, int z) {
		random.setSeed(x * 49632 + z * 325176 + seed);
		return random.nextFloat() * 2f - 1f;
	}

	public void setHasWater(boolean hasWater) {
		this.hasWater = hasWater;
	}
}
