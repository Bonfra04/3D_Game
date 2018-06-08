package cube;

import gameEngine.resourcesManager.ResourceManager;

public class Cube {

	private int texture;

	public Cube(String texture) {

		this.texture = ResourceManager.LOADER
				.loadCubeMap(new String[] { texture, texture, texture, texture, texture, texture });

		ResourceManager.addCube(this);
	}

	public int getTexture() {
		return this.texture;
	}
}
