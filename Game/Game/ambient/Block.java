package ambient;

import org.lwjgl.util.vector.Vector3f;

import gameEngine.collision.CollisionBox;
import gameEngine.entities.Entity;
import gameEngine.models.RawModel;
import gameEngine.models.TexturedModel;
import gameEngine.renderEngine.Loader;
import gameEngine.textures.ModelTexture;

public class Block extends Entity {

	private static final Loader LOADER = new Loader();

	private static final float[] VERTICES = { -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f,
			-0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f,
			-0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f,
			0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f,
			-0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f };

	private static final float[] TEXTURE_COORDS = { 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1,
			0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0 };
	
	private static final int[] INDICES = { 0, 1, 3, 3, 1, 2, 4, 5, 7, 7, 5, 6, 8, 9, 11, 11, 9, 10, 12, 13, 15, 15, 13,
			14, 16, 17, 19, 19, 17, 18, 20, 21, 23, 23, 21, 22 };

	private static final RawModel MODEL = LOADER.loadToVAO(VERTICES, TEXTURE_COORDS, INDICES);

	private CollisionBox collisionBox = new CollisionBox(1, 1, 1, new Vector3f(0, -2, 0));

	public Block(String texture, int x, int y, int z) {
		super(new TexturedModel(MODEL, new ModelTexture(LOADER.loadTexture(texture))), 1, new Vector3f(x, y, z),
				new Vector3f(0, 0, 0));

		super.addCollisionBox(collisionBox);
		super.setTextureTrasparency(true);
		super.setTextureFakeLighting(true);
	}

}
