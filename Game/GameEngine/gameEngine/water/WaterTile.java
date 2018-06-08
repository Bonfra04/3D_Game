package gameEngine.water;

import gameEngine.resourcesManager.ResourceManager;

public class WaterTile {

	public static final float TILE_SIZE = 800;

	private float height;
	private float x, z;

	public WaterTile(float centerX, float centerZ, float height) {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
		
		ResourceManager.addWater(this);
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

}
